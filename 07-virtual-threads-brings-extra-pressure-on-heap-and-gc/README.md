# Virtual Threads Bring Extra Pressure on Heap and GC

**The Trade-off:** Virtual Threads deliver simplicity and scalability, but they come with a cost—**increased heap memory
usage** and **GC pressure**. Understanding this trade-off is crucial for production deployments.

---

## The Fundamental Difference: Where Stacks Live

### Platform Threads (Traditional Model)

**Stack Location:** OS-allocated memory (off-heap, native memory)

```
┌─────────────────────────────────┐
│   Process Memory Layout         │
├─────────────────────────────────┤
│  JVM Heap (managed by GC)       │
│  - Objects                      │
│  - Arrays                       │
│                                 │
├─────────────────────────────────┤
│  Native Memory (off-heap)       │
│  - Thread Stack 1 (~2 MB)       │
│  - Thread Stack 2 (~2 MB)       │
│  - Thread Stack 3 (~2 MB)       │
│  - ...                          │
└─────────────────────────────────┘
```

**Characteristics:**

- Fixed size (typically 1-2 MB per thread)
- Allocated in native memory (outside JVM heap)
- Not managed by GC
- Limit: Thousands of threads before running out of memory

### Virtual Threads (Loom Model)

**Stack Location:** JVM heap (managed by GC)

```
┌─────────────────────────────────┐
│   Process Memory Layout         │
├─────────────────────────────────┤
│  JVM Heap (managed by GC)       │
│  - Objects                      │
│  - Arrays                       │
│  - Virtual Thread Stacks        │
│    (stored as heap objects)     │
│    • VThread1 Stack (~few KB)   │
│    • VThread2 Stack (~few KB)   │
│    • VThread3 Stack (~few KB)   │
│    • ... (millions possible)    │
├─────────────────────────────────┤
│  Native Memory (off-heap)       │
│  - Carrier Thread Stacks        │
│    (few threads, ~2 MB each)    │
└─────────────────────────────────┘
```

**Characteristics:**

- Variable size (starts small, grows as needed)
- Allocated on JVM heap (managed by GC)
- Subject to garbage collection
- Limit: Millions of threads (heap permitting)

---

## Mount, Unmount, and Stack Management

### The Virtual Thread Lifecycle

Virtual threads achieve their scalability through **mounting** and **unmounting** from carrier (platform) threads.

**Key Operations:**

1. **Mount:** Virtual thread is assigned to a carrier thread and can execute
2. **Unmount:** Virtual thread blocks on I/O, its stack is saved to heap, carrier thread is freed
3. **Remount:** When I/O completes, virtual thread is assigned to an available carrier thread

### Example: HTTP Request with Database Call

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VirtualThreadExample {

    public static void main(String[] args) {
        // Create a virtual thread
        Thread.startVirtualThread(() -> {
            processRequest(123);
        });

        // Simulate main thread waiting
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

    public static void processRequest(int userId) {
        System.out.println("1. Starting request processing");
        // Virtual thread is MOUNTED on carrier thread

        // Simulate some CPU work
        int result = performCalculation(userId);
        System.out.println("2. Calculation complete: " + result);
        // Still MOUNTED - no blocking occurred

        // Database call - BLOCKING I/O
        String userData = fetchFromDatabase(userId);
        System.out.println("3. Database fetch complete: " + userData);
        // During fetchFromDatabase, the virtual thread was UNMOUNTED
        // Stack was saved to heap, carrier thread freed
        // After query completed, thread was REMOUNTED

        // HTTP call - BLOCKING I/O
        String apiResponse = callExternalAPI(userData);
        System.out.println("4. API call complete: " + apiResponse);
        // Again: UNMOUNTED during I/O, REMOUNTED after

        // Final processing
        String finalResult = processData(apiResponse);
        System.out.println("5. Final result: " + finalResult);
        // MOUNTED for CPU work

        // Thread completes - stack memory eligible for GC
    }

    private static int performCalculation(int input) {
        // CPU-bound work - thread stays mounted
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += input * i;
        }
        return sum;
    }

    private static String fetchFromDatabase(int userId) {
        // BLOCKING I/O - triggers unmount
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost/mydb", "user", "pass")) {

            // At this point: virtual thread UNMOUNTS
            // Stack contents saved to heap as a continuation object
            // Carrier thread returns to pool

            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT name FROM users WHERE id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            // When query completes: virtual thread REMOUNTS
            // Stack restored from heap
            // Execution continues on (possibly different) carrier thread

            if (rs.next()) {
                return rs.getString("name");
            }
            return "Unknown";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String callExternalAPI(String userData) {
        // BLOCKING I/O - triggers unmount
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.example.com/process"))
                    .POST(HttpRequest.BodyPublishers.ofString(userData))
                    .build();

            // During send: UNMOUNT → stack to heap → carrier freed
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            // After response: REMOUNT → stack from heap → continue execution

            return response.body();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String processData(String input) {
        // CPU-bound work - thread stays mounted
        return input.toUpperCase().trim();
    }
}
```

---

## What Happens Under the Hood

### The Mount/Unmount Cycle Explained

```
Timeline of Virtual Thread Execution:
═══════════════════════════════════════════════════════════════

Step 1: MOUNTED (CPU Work)
┌─────────────────────────────────────┐
│ Carrier Thread 1                    │
│ ┌─────────────────────────────────┐ │
│ │ Virtual Thread Stack            │ │
│ │ • performCalculation()          │ │
│ │ • Local variables               │ │
│ │ • Return addresses              │ │
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘
Status: Running on Carrier Thread 1
Heap Impact: Stack is on carrier's native stack

═══════════════════════════════════════════════════════════════

Step 2: UNMOUNT (Blocking I/O - Database Call)
┌─────────────────────────────────────┐      ┌──────────────────────┐
│ Carrier Thread 1                    │      │ JVM Heap             │
│ (now free, can run other VThreads) │      │ ┌──────────────────┐ │
│                                     │      │ │ Continuation Obj │ │
└─────────────────────────────────────┘      │ │ • Stack frames   │ │
                                              │ │ • Local vars     │ │
                                              │ │ • Return addrs   │ │
                                              │ │ • Program counter│ │
                                              │ └──────────────────┘ │
                                              └──────────────────────┘
Status: Waiting for I/O, stack saved to heap
Heap Impact: Stack copied to heap as continuation object (~few KB)

I/O Operation in progress (database query executing)...

═══════════════════════════════════════════════════════════════

Step 3: REMOUNT (I/O Completes)
┌─────────────────────────────────────┐      ┌──────────────────────┐
│ Carrier Thread 2 (might be diff)   │      │ JVM Heap             │
│ ┌─────────────────────────────────┐ │      │ ┌──────────────────┐ │
│ │ Virtual Thread Stack (restored) │ │      │ │ Continuation Obj │ │
│ │ • fetchFromDatabase() resumes   │ │      │ │ (eligible for GC)│ │
│ │ • Local variables restored      │ │      │ └──────────────────┘ │
│ │ • Execution continues           │ │      └──────────────────────┘
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘
Status: Running on Carrier Thread 2
Heap Impact: Stack restored, continuation object awaits GC

═══════════════════════════════════════════════════════════════

Step 4: UNMOUNT AGAIN (HTTP API Call)
Same cycle repeats:
• Stack → Heap (continuation object created)
• Carrier thread freed
• I/O executes
• Stack ← Heap (continuation restored)
• Carrier thread assigned

═══════════════════════════════════════════════════════════════

Step 5: COMPLETION
Virtual thread completes execution
• Continuation object becomes garbage
• GC will reclaim memory during next collection cycle
```

---

## The Heap Pressure Reality

### Memory Consumption Comparison

**Scenario:** 100,000 concurrent I/O-bound tasks

```
Platform Threads:
─────────────────
100,000 threads × 2 MB/thread = 200 GB native memory
Result: IMPOSSIBLE - will crash with OutOfMemoryError

Virtual Threads:
────────────────
100,000 virtual threads × ~10 KB/thread (avg stack) = ~1 GB heap
+ Carrier threads: 16 threads × 2 MB = 32 MB native memory
Result: Feasible, but adds 1 GB to heap
```

### GC Impact

**The Challenge:**

1. **More Heap Objects:** Each virtual thread's stack is a heap object
2. **Frequent Allocation:** Mount/unmount creates continuation objects
3. **Short-Lived Objects:** Many stacks are temporary (request/response cycle)
4. **GC Pressure:** More objects = more GC cycles

**Example GC Behavior:**

```
Without Virtual Threads (Reactive):
────────────────────────────────────
Heap Usage: ~500 MB
GC Frequency: Every 30 seconds (Young Gen)
GC Pause: ~50ms

With Virtual Threads (1M concurrent):
─────────────────────────────────────
Heap Usage: ~5-10 GB (includes stack objects)
GC Frequency: Every 10-15 seconds (Young Gen)
GC Pause: ~100-200ms
```

---

## The Pinning Problem

### What is Pinning?

**Pinning** occurs when a virtual thread **cannot unmount** from its carrier thread, even when blocked.

**Why It Matters:** A pinned virtual thread defeats the purpose of virtual threads—the carrier thread sits idle,
waiting.

### Causes of Pinning (Pre-JDK 24)

1. **`synchronized` blocks** (fixed in JDK 24+ via JEP 491)
2. **Native method calls** (unfixable by design)

### Example: Pinning with `synchronized`

```java
public class PinningExample {
    private final Object lock = new Object();

    public void pinnedExample() {
        synchronized (lock) {  // Virtual thread is now PINNED

            // Any blocking call here PINS the carrier thread
            String data = fetchFromDatabase();
            // ⚠️ PINNED: Carrier thread waits idle
            // ⚠️ Cannot be used by other virtual threads

            processData(data);
        }
        // Unpinned after exiting synchronized block
    }

    public void notPinnedExample() {
        // Use ReentrantLock instead
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            // Blocking call here allows unmounting
            String data = fetchFromDatabase();
            // ✅ NOT PINNED: Carrier thread freed during I/O
            // ✅ Other virtual threads can use it

            processData(data);
        } finally {
            lock.unlock();
        }
    }
}
```

---

## Practical Implications

### When Virtual Threads Add More Pressure

**Bad Fit:**

- **CPU-bound workloads:** Virtual threads add heap overhead without benefits
- **Few concurrent tasks:** <1000 threads—platform threads are fine
- **Synchronous blocking everywhere:** Defeats the purpose

**Good Fit:**

- **I/O-bound workloads:** Database queries, HTTP calls, file I/O
- **High concurrency:** 10,000+ concurrent operations
- **Simple code preferred:** Value maintainability over reactive complexity

### Tuning Recommendations

```bash
# Increase heap size for virtual threads
java -Xmx8g -Xms8g \
     -XX:+UseZGC \              # Low-latency GC
     -XX:+UnlockExperimentalVMOptions \
     -XX:ZCollectionInterval=5  # GC more frequently
     MyApplication
```

**GC Recommendations:**

- **ZGC** or **Shenandoah:** Low-pause collectors handle heap pressure better
- **G1GC:** Also viable, tune young generation size
- **Avoid Serial/Parallel GC:** Not optimized for high allocation rates

---

## The Bottom Line

### The Trade-off

| Aspect              | Platform Threads  | Reactive       | Virtual Threads    |
|---------------------|-------------------|----------------|--------------------|
| **Code Complexity** | Simple            | Complex        | Simple             |
| **Max Concurrency** | ~Thousands        | ~Millions      | ~Millions          |
| **Memory Location** | Native (off-heap) | Heap (minimal) | Heap (significant) |
| **GC Pressure**     | None              | Low            | **High**           |
| **Scalability**     | Limited           | Excellent      | Excellent          |
| **Maintainability** | High              | Low            | High               |

### The Verdict

Virtual Threads bring **heap pressure** and **GC overhead**, but:

✅ **Worth it** for I/O-bound, high-concurrency workloads  
✅ **Worth it** when simplicity accelerates development  
✅ **Worth it** when reactive complexity is too high

❌ **Not worth it** for CPU-bound workloads  
❌ **Not worth it** for low-concurrency applications  
❌ **Not worth it** if heap size is severely constrained

**The Key Insight:** Modern GCs (ZGC, Shenandoah) are designed for high allocation rates. The heap pressure is **manageable** with proper tuning.

---

## Monitoring Virtual Threads

### Key Metrics to Watch

```java
// Enable JFR monitoring
jcmd<PID> JFR.
start name = vthreads
settings=profile

// Watch for:
// 1. Virtual thread count
// 2. Carrier thread pool size
// 3. Pinning events (pre-JDK 24)
// 4. Heap usage growth
// 5. GC frequency and pause times
```

**JFR Events:**

- `jdk.VirtualThreadStart` - Virtual thread creation
- `jdk.VirtualThreadEnd` - Virtual thread completion
- `jdk.VirtualThreadPinned` - Pinning detected (JDK 21-23)

### VisualVM / JConsole

Monitor these:

- **Heap usage trend:** Should stabilize after warmup
- **GC activity:** Frequency and duration
- **Thread count:** Virtual vs. platform threads

---

## Conclusion

Virtual Threads shift the complexity from **your code** to the **JVM heap**. This is a **deliberate trade-off**:

- **Before (Reactive):** Simple heap, complex code
- **After (Virtual Threads):** Complex heap (more objects), simple code

For most I/O-bound applications, **simple code is worth the heap pressure**. Modern GCs can handle it. But you must:

1. **Size heap appropriately** (expect 2-3x growth for high-concurrency workloads)
2. **Choose the right GC** (ZGC, Shenandoah, or tuned G1GC)
3. **Monitor GC metrics** (don't guess, measure)
4. **Avoid unnecessary virtual threads** (don't use them for CPU-bound work)

**The paradigm shift:** Accept that scalability comes from **heap-managed stacks**, not native threads. The JVM does the
heavy lifting—you write simple code.

---

**[← Previous: Project Loom Production Readiness](../06-project-loom-production-readiness/README.md)** | **[Next: Live Demos and Quizzes →](../08-live-demos-and-quizzes/README.md)**

