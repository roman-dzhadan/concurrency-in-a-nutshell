# Async/Await: The Alternative Java Didn't Choose

**The Thesis:** When faced with the concurrency crisis of the multicore era, many programming languages adopted **async/await** as their solution. Java could have gone this route—but didn't. And now, with Project Loom delivering Virtual Threads, Java will **never** need to.

---

## The Async/Await Revolution in Other Languages

### What is Async/Await?

**Async/await** is a programming pattern that allows developers to write asynchronous code that **looks** synchronous,
using special keywords to mark functions and await points.

**The Core Concept:**

- Mark functions as `async` to indicate they perform asynchronous operations
- Use `await` keyword to wait for asynchronous operations without blocking the thread
- The compiler/runtime transforms the code into a state machine

**The Goal:** Solve the "callback hell" problem while achieving non-blocking I/O efficiency.

---

## Languages That Adopted Async/Await

### 1. C# / .NET (2012)

**When:** C# 5.0, introduced in 2012  
**Why:** Microsoft needed a way to write responsive GUI applications and efficient server code

**Example:**

```csharp
// C# async/await
public async Task<string> FetchUserDataAsync(int userId)
{
    // await suspends the method without blocking the thread
    var user = await database.QueryAsync("SELECT * FROM users WHERE id = ?", userId);
    var profile = await api.GetProfileAsync(user.ProfileId);
    return profile.Name;
}
```

**How it works:**

- `async` keyword marks the method as asynchronous
- `await` yields control back to the caller
- The runtime schedules continuation when the operation completes
- Original thread is freed to do other work

**Result:** C# developers got simple syntax with efficient non-blocking I/O.

---

### 2. JavaScript / Node.js (2017)

**When:** ES2017 (ES8)  
**Why:** JavaScript had been drowning in "callback hell" for years

**Before (Callback Hell):**

```javascript
// JavaScript - callback hell
function fetchUserData(userId, callback) {
    database.query("SELECT * FROM users WHERE id = ?", userId, function (err, user) {
        if (err) return callback(err);

        api.getProfile(user.profileId, function (err, profile) {
            if (err) return callback(err);

            callback(null, profile.name);
        });
    });
}
```

**After (Async/Await):**

```javascript
// JavaScript - async/await (ES2017+)
async function fetchUserData(userId) {
    const user = await database.query("SELECT * FROM users WHERE id = ?", userId);
    const profile = await api.getProfile(user.profileId);
    return profile.name;
}
```

**Result:** JavaScript went from the worst async syntax to one of the best.

---

### 3. Python (2015)

**When:** Python 3.5, introduced in 2015 with `async`/`await` keywords  
**Why:** Python needed efficient I/O for web servers and async frameworks

**Example:**

```python
# Python async/await
async def fetch_user_data(user_id):
    # await suspends the coroutine
    user = await database.query("SELECT * FROM users WHERE id = ?", user_id)
    profile = await api.get_profile(user['profile_id'])
    return profile['name']
```

**How it works:**

- Functions marked with `async def` return coroutines
- `await` suspends the coroutine until the awaited operation completes
- Event loop manages scheduling and execution

---

### 4. Rust (2019)

**When:** Rust 1.39, stabilized in 2019  
**Why:** Rust needed zero-cost async I/O without sacrificing safety

**Example:**

```rust
// Rust async/await
async fn fetch_user_data(user_id: i32) -> Result<String, Error> {
    let user = database.query("SELECT * FROM users WHERE id = ?", user_id).await?;
    let profile = api.get_profile(user.profile_id).await?;
    Ok(profile.name)
}
```

**Unique feature:** Async is **zero-cost abstraction**—compiled to efficient state machines with no runtime overhead.

---

### 5. Kotlin (2017)

**When:** Kotlin 1.1, introduced coroutines in 2017  
**Why:** JVM needed modern async without reactive complexity

**Example:**

```kotlin
// Kotlin coroutines (suspend functions)
suspend fun fetchUserData(userId: Int): String {
    val user = database.query("SELECT * FROM users WHERE id = ?", userId)
    val profile = api.getProfile(user.profileId)
    return profile.name
}
```

**Note:** Kotlin runs on the JVM but had to implement its own solution because **Java didn't provide one**.

---

### 6. Swift (2021)

**When:** Swift 5.5, introduced in 2021  
**Why:** Apple needed structured concurrency for iOS/macOS development

**Example:**

```swift
// Swift async/await
func fetchUserData(userId: Int) async throws -> String {
    let user = try await database.query("SELECT * FROM users WHERE id = ?", userId)
    let profile = try await api.getProfile(user.profileId)
    return profile.name
}
```

---

## The Pattern: How Async/Await Works

### The Compiler Magic

When you write async/await code, the compiler transforms it into a **state machine**.

**Original Code:**

```javascript
async function example() {
    const a = await operation1();  // Suspension point 1
    const b = await operation2(a); // Suspension point 2
    return b;
}
```

**Compiler Transformation (conceptual):**

```javascript
function example() {
    return {
        state: 0,
        resume: function (value) {
            switch (this.state) {
                case 0:
                    this.state = 1;
                    return operation1().then(result => {
                        this.a = result;
                        return this.resume();
                    });
                case 1:
                    this.state = 2;
                    return operation2(this.a).then(result => {
                        this.b = result;
                        return this.resume();
                    });
                case 2:
                    return Promise.resolve(this.b);
            }
        }
    };
}
```

**The Key Insight:** The function is split into segments between `await` points. State is preserved across suspensions.

---

## Why This Was Attractive

### The Benefits of Async/Await

1. **Readable Code:** Looks like synchronous code
2. **Efficient:** Non-blocking I/O without wasting threads
3. **Composable:** Easy to combine async operations
4. **Debuggable:** Stack traces are mostly reasonable
5. **Ecosystem:** Forces the entire language ecosystem to adopt async patterns

### The Trade-offs

1. **Viral Nature:** `async` infects the entire call chain
2. **Color Functions:** Functions are either "sync" or "async"—mixing is painful
3. **Complexity:** Under the hood, it's still state machines
4. **Learning Curve:** Developers need to understand when to use `await`
5. **Runtime Overhead:** State machines and scheduling add complexity

---

## Why Java Didn't Choose This Path

### Historical Context

Between 2010-2020, when many languages adopted async/await, Java was stuck:

1. **CompletableFuture (2014):** Java 8 introduced this, but it was clunky
2. **Reactive Streams (2015):** Community tried RxJava, Project Reactor
3. **Debate:** Should Java add async/await keywords?

### The Reasons Java Said "No"

#### 1. **Backward Compatibility Nightmare**

Java's strength is backward compatibility. Adding `async`/`await` would require:

```java
// This would break millions of existing APIs
public async String

fetchUser(int id) {
    String user = await database.query("...");
    return user;
}
```

**The Problem:**

- Existing code doesn't use `async`/`await`
- Entire standard library would need async versions
- `java.io.*`, `java.net.*`, JDBC—all need reimplementation
- Two worlds: sync and async, incompatible

**Java's Philosophy:** "Don't break the world for a feature."

---

#### 2. **The "Function Color" Problem**

Adding async/await creates **two types of functions**:

```java
// Synchronous functions (red)
String fetchUserSync(int id) {
    return database.query("...");
}

// Asynchronous functions (blue)
async String

fetchUserAsync(int id) {
    return await database.query("...");
}

// You can't easily mix them!
async String

processUser(int id) {
    // Can call blue functions with await
    String user = await fetchUserAsync(id);

    // What about red functions? Do they block? Do they need wrapping?
    String result = fetchUserSync(id); // ???
}
```

**The Function Color Problem:**

- Red functions can't call blue functions (can't use `await` in sync context)
- Blue functions calling red functions might block the async runtime
- You end up with duplicated APIs: `query()` and `queryAsync()`

**Java's Concern:** Splitting the ecosystem into sync/async worlds is harmful.

---

#### 3. **Project Loom Was Already in the Works**

By the time async/await was seriously considered (mid-2010s), the OpenJDK team was already exploring **Project Loom**.

**The Key Insight:** Why add async/await when you can fix the **root problem**—heavy threads?

| Approach         | Solution Strategy                             | Outcome                |
|------------------|-----------------------------------------------|------------------------|
| **Async/Await**  | Write code that looks sync but executes async | Complex compiler magic |
| **Project Loom** | Make threads so cheap you don't need async    | Fix the platform       |

**Java's Bet:** "Let's fix threads instead of adding syntax."

---

#### 4. **Async/Await Doesn't Actually Solve the Problem**

Async/await is a **workaround** for expensive threads. It still has issues:

```javascript
// JavaScript async/await
async function handleRequest(req) {
    const user = await db.getUser(req.userId);
    const posts = await db.getPosts(user.id);

    // This STILL runs on a single-threaded event loop
    // CPU-bound work blocks everything:
    const processed = heavyComputation(posts); // BLOCKS!

    return processed;
}
```

**Limitations:**

- Still one event loop (in single-threaded languages like JavaScript)
- CPU-bound work blocks the entire runtime
- Doesn't utilize multiple cores without explicit worker threads
- Stack traces are still weird

**Java's Perspective:** "If we're going to fix concurrency, let's fix it properly."

---

#### 5. **The Java Standard Library is Huge**

Java has one of the largest standard libraries in existence:

- `java.io.*` - File I/O
- `java.net.*` - Networking
- `java.sql.*` - JDBC database access
- `java.nio.*` - New I/O (already has selectors, but still blocking APIs)
- Thousands of third-party libraries

**Adding async/await means:**

- Rewriting or duplicating the entire standard library
- `FileInputStream` + `FileInputStreamAsync`
- `Socket` + `SocketAsync`
- `Connection` + `ConnectionAsync`

**Estimated Cost:** Tens of thousands of engineer-hours, years of migration.

**Project Loom Alternative:** Existing APIs work **as-is** with Virtual Threads. Zero migration cost.

---

## The Virtual Threads Advantage

### Why Virtual Threads Are Superior to Async/Await

| Aspect               | Async/Await           | Virtual Threads (Loom)   |
|----------------------|-----------------------|--------------------------|
| **Syntax Changes**   | New keywords required | No syntax changes        |
| **Backward Compat**  | Breaks existing APIs  | 100% compatible          |
| **Function Color**   | Sync vs Async split   | No split—all code works  |
| **Standard Library** | Needs async versions  | Works as-is              |
| **Stack Traces**     | State machine traces  | Normal stack traces      |
| **Debugger Support** | Complex               | Standard debugging works |
| **Third-Party Libs** | Need async versions   | Work unchanged           |
| **Learning Curve**   | Medium (new concepts) | Low (familiar threads)   |
| **Performance**      | Good                  | Good                     |
| **Scalability**      | Millions of tasks     | Millions of threads      |

**The Verdict:** Virtual Threads achieve the same goal (scalable concurrency) **without** the complexity.

---

## Code Comparison: The Same Task

### Scenario: Fetch user, query database, call API, return result

**JavaScript (Async/Await):**

```javascript
async function processUser(userId) {
    try {
        const user = await db.query("SELECT * FROM users WHERE id = ?", userId);
        const profile = await api.getProfile(user.profileId);
        const orders = await db.query("SELECT * FROM orders WHERE user_id = ?", userId);
        return {user, profile, orders};
    } catch (error) {
        console.error("Error:", error);
        throw error;
    }
}
```

**Java with Virtual Threads:**

```java
public UserData processUser(int userId) {
    try {
        User user = db.query("SELECT * FROM users WHERE id = ?", userId);
        Profile profile = api.getProfile(user.getProfileId());
        List<Order> orders = db.query("SELECT * FROM orders WHERE user_id = ?", userId);
        return new UserData(user, profile, orders);
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        throw e;
    }
}
```

**Observations:**

- **No `async`/`await` keywords** in Java
- **Same readability** as async/await code
- **Standard try/catch** error handling
- **Normal stack traces** when exceptions occur
- **Runs efficiently** on Virtual Threads automatically

---

## The Kotlin Exception: JVM's Async/Await

### Kotlin Had No Choice

**Context:** Kotlin was designed in the early 2010s, before Project Loom existed.

**Problem:** Running on the JVM, Kotlin faced the same concurrency challenges as Java.

**Solution:** Kotlin introduced **coroutines** (their version of async/await) in 2017.

```kotlin
// Kotlin coroutines
suspend fun processUser(userId: Int): UserData = coroutineScope {
    val user = async { db.query("SELECT * FROM users WHERE id = ?", userId) }
    val profile = async { api.getProfile(user.await().profileId) }
    val orders = async { db.query("SELECT * FROM orders WHERE user_id = ?", userId) }

    UserData(user.await(), profile.await(), orders.await())
}
```

**Why Kotlin Did It:**

- Project Loom wasn't available yet
- Kotlin needed a solution **now**
- They couldn't wait for Java to fix threads

**The Irony:** Now that Project Loom exists (JDK 21+), **Kotlin's coroutines are less necessary**. Kotlin developers can
use Virtual Threads directly from the JVM.

---

## Why Java Will Never Add Async/Await

### 1. **Virtual Threads Solve the Problem**

With Project Loom delivering Virtual Threads in JDK 21 (2023), there's **no need** for async/await:

- Millions of threads? ✅ Virtual Threads handle it
- Non-blocking I/O? ✅ Automatic with Virtual Threads
- Simple syntax? ✅ Standard Java code
- No library changes? ✅ Everything works as-is

**Conclusion:** The problem async/await was meant to solve is **already solved**.

---

### 2. **Too Late in Java's Evolution**

Java is 30 years old (1995-2025). Adding async/await now would:

- Break backward compatibility (or force two worlds to coexist)
- Require reimplementing the standard library
- Fragment the ecosystem (sync vs async)
- Confuse developers (when to use threads vs async?)

**Java's Decision:** "We missed the async/await window. Virtual Threads are the better solution anyway."

---

### 3. **The Community Doesn't Want It**

After experiencing reactive programming's complexity (2015-2023), the Java community is **eager** to return to simple,
synchronous code.

**Developer Sentiment:**

- 2015: "We need async/await like JavaScript!"
- 2018: "Actually, reactive programming is painful..."
- 2023: "Virtual Threads! Finally, simple code again!"
- 2025: "Why would we ever want async/await now?"

---

### 4. **Go and Virtual Threads Proved the Alternative**

**Go's Success (2009-2025):**

- No async/await keywords
- Just goroutines (lightweight threads)
- Simple, readable code
- Scales to millions of concurrent operations

**Java's Virtual Threads (2023+):**

- Same model as Go's goroutines
- No new syntax needed
- Scales to millions of concurrent operations
- Backward compatible with all existing code

**The Lesson:** You don't need async/await if your threads are cheap enough.

---

## The Final Comparison

### JavaScript Developer (Async/Await)

```javascript
async function handleRequest(req) {
    const user = await fetchUser(req.userId);
    const orders = await fetchOrders(user.id);
    return {user, orders};
}
```

**Trade-offs:**

- ✅ Looks simple
- ❌ Everything is async (viral)
- ❌ Stack traces are weird
- ❌ Entire ecosystem needs async versions

---

### Java Developer (Virtual Threads)

```java
public Response handleRequest(Request req) {
    User user = fetchUser(req.getUserId());
    List<Order> orders = fetchOrders(user.getId());
    return new Response(user, orders);
}
```

**Trade-offs:**

- ✅ Looks simple
- ✅ Standard Java code
- ✅ Normal stack traces
- ✅ All existing libraries work
- ✅ No "async infection"

---

## Conclusion: Java Made the Right Call

### The Timeline

- **2010s:** Other languages adopt async/await as a workaround for expensive threads
- **2012-2017:** Java experiments with CompletableFuture and reactive programming
- **2018:** Project Loom development accelerates
- **2023:** Virtual Threads ship in JDK 21
- **2025:** Async/await is no longer attractive to Java

### The Verdict

**Java didn't choose async/await—and it was the right decision.**

**Why?**

1. **Virtual Threads are simpler** - No new syntax, no learning curve
2. **Virtual Threads are compatible** - All existing code works
3. **Virtual Threads are sufficient** - Achieve the same performance goals
4. **Async/await is obsolete** - The problem it solved is gone

**The Irony:** While other languages adopted async/await to **work around** expensive threads, Java chose to **fix
threads instead**. It took longer, but the result is superior.

**The Future:** Java will never need async/await. Virtual Threads are here to stay, and they're **enough**.

---

**Other languages:** "We need async/await to scale!"  
**Java:** "We'll just make threads cheap."

**That's the Java way.**

---

**[← Previous: Rob Pike Persona & Go's Influence](../03-rob-pike-persona-and-go-influence/README.md)** | **[Next: Project Loom State →](../05-project-loom-state/README.md)**
