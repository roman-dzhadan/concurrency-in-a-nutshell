# Virtual Thread-Based Java Concurrency

## What are Virtual Threads?

Virtual threads (introduced in JDK 21) are lightweight threads managed by the JVM. Unlike platform threads, virtual threads use an **M:N mapping** - millions of virtual threads are multiplexed onto a small number of OS threads (carrier threads).

## Key Difference in System Calls

- **Platform Threads:** Each `new Thread().start()` → `clone()` system call
- **Virtual Threads:** Initial carrier pool creation → few `clone()` calls, then **NO MORE** clone() calls for new virtual threads

## How to Run and Trace

### Step 1: Compile and Run
```bash
# Navigate to the directory
cd 08-live-demos-and-quizzes/concurrency-primitives/03-virtual-thread-based-java-concurrency

# Run the Java program (requires JDK 21+)
java src/VirtualThreadBasedJavaConcurrency.java
```

### Step 2: Trace with strace (in another terminal)
```bash
# Get the PID from Step 1 output, then:
strace -f -p <PID> -e trace=clone,clone3

# You can also watch futex (thread synchronization):
strace -f -p <PID> -e trace=clone,clone3,futex
```

### What You'll See

**Initial Phase (Carrier Pool Creation):**
```
[pid XXXXX] clone(...) = YYYYY  // Carrier thread 1
[pid XXXXX] clone(...) = ZZZZZ  // Carrier thread 2
... (a few more, typically equal to CPU count)
```

**When Creating Virtual Threads:**
```
>>> Creating 1000 virtual threads <<<
... NO clone() SYSTEM CALLS! ...
```

**Key Observation:** Creating thousands/millions of virtual threads does **NOT** create new OS threads. Virtual threads are scheduled on the existing carrier thread pool.

## Expected Output

```
[STARTED] Virtual Thread Demo - PID = 12345
Attach strace now: strace -f -p 12345 -e trace=clone,clone3
Waiting 5 seconds for you to attach strace...

Available processors: 8
Carrier pool size: ~8 threads

>>> Watch strace: Carrier pool being created (expect ~8 clone() calls) <<<

Creating 1000 virtual threads...

>>> Watch strace: NO new clone() calls despite creating 1000 threads! <<<

[VirtualThread-1] Started - Is Virtual: true
[VirtualThread-2] Started - Is Virtual: true
[VirtualThread-3] Started - Is Virtual: true
...
[VirtualThread-1000] Started - Is Virtual: true

All 1000 virtual threads created successfully
Waiting for threads to complete...

[VirtualThread-1] Completed
[VirtualThread-2] Completed
...
[VirtualThread-1000] Completed

All virtual threads completed
Total virtual threads created: 1000
Total OS threads (clone() calls): ~8 (carrier pool)
[COMPLETED] Virtual Thread Demo - PID = 12345
```

## The Breakthrough

### Platform Threads (Old Model - JDK 1.3 to 20)
```
1,000 Java threads → 1,000 clone() calls → 1,000 OS threads → ~2 GB memory
```

### Virtual Threads (New Model - JDK 21+)
```
1,000,000 Java threads → ~8 clone() calls → ~8 OS threads → ~1 GB memory
```

## Proof via strace

### Platform Threads (02-platform-thread-based)
```bash
$ strace -f -p <PID> -e trace=clone,clone3
clone(...) = 12346  # Thread 1
clone(...) = 12347  # Thread 2
clone(...) = 12348  # Thread 3
# 3 threads = 3 clone() calls
```

### Virtual Threads (03-virtual-thread-based)
```bash
$ strace -f -p <PID> -e trace=clone,clone3
clone(...) = 12346  # Carrier 1
clone(...) = 12347  # Carrier 2
clone(...) = 12348  # Carrier 3
clone(...) = 12349  # Carrier 4
... (few more for carrier pool)
# Then NOTHING - even after creating 1000 virtual threads!
```

## How Virtual Threads Work

```
┌─────────────────────────────────────────────────┐
│  1,000,000 Virtual Threads (JVM Heap)          │
│  - Lightweight                                  │
│  - Managed by JVM scheduler                     │
│  - Stack stored on heap                         │
└─────────────────────────────────────────────────┘
                    │
                    │ Multiplexed onto
                    ▼
┌─────────────────────────────────────────────────┐
│  ~8 Carrier Threads (OS Threads)                │
│  - Created via clone()                          │
│  - One per CPU core                             │
│  - Heavy (2MB stack each)                       │
└─────────────────────────────────────────────────┘
```

When a virtual thread blocks on I/O:
1. It **unmounts** from the carrier thread
2. Its stack is saved to the heap
3. The carrier thread picks up another virtual thread
4. When I/O completes, the virtual thread **remounts**

**No OS threads are created or destroyed** during this process.

## The Morale

- **Virtual threads are LIGHTWEIGHT** - Each thread consumes ~few KB of heap
- **No clone() per thread** - Only carrier threads trigger clone()
- **Massive scalability** - You can create millions of virtual threads
- **No context switching overhead** - JVM scheduler manages threads in userspace
- **This is the future of Java concurrency** (JDK 21 onwards)

## Comparison Table

| Aspect | Platform Threads | Virtual Threads |
|--------|------------------|-----------------|
| **System Call per Thread** | Yes (`clone()`) | No (only for carriers) |
| **OS Threads Created** | N threads → N OS threads | N threads → ~CPU count OS threads |
| **Stack Location** | Native memory | JVM heap |
| **Stack Size** | ~2 MB fixed | ~Few KB, grows as needed |
| **Max Threads** | ~10,000 | ~10,000,000 |
| **Creation Overhead** | High (syscall + OS setup) | Low (heap allocation) |
| **Context Switch** | OS scheduler (expensive) | JVM scheduler (cheap) |
| **Blocking I/O** | Blocks OS thread | Unmounts, carrier freed |

## Requirements

- **JDK 21 or higher** - Virtual threads are not available in earlier versions
- For production: **JDK 21 (LTS)** or **JDK 25** recommended

## Further Reading

- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444) - Official specification
- [JEP 491: Synchronize Virtual Threads without Pinning](https://openjdk.org/jeps/491) - JDK 24+ enhancement
- [Man page: clone(2)](https://linux.die.net/man/2/clone) - Linux system call
- [Inside Java: Virtual Threads](https://inside.java/tag/virtual-threads) - Deep dives and updates

