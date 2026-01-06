# Platform Thread-Based Java Concurrency

## What are Platform Threads?

Platform threads (also called "Native Threads" or "Kernel Threads") are Java threads that map 1:1 to OS threads. Each `Thread` object in Java creates a corresponding OS thread via the `clone()` system call on Linux.

## Key System Calls to Watch

- `clone()` - Creates a new OS thread (Linux's way of creating threads)
- `futex()` - Fast userspace mutex (used for thread synchronization)

## How to Run and Trace

### Step 1: Compile and Run
```bash
# Navigate to the directory
cd 08-live-demos-and-quizzes/concurrency-primitives/02-platform-thread-based-java-concurrency

# Run the Java program (it will print its PID)
java src/PlatformThreadBasedJavaConcurrency.java
```

### Step 2: Trace with strace (in another terminal)
```bash
# Get the PID from Step 1 output, then:
strace -f -p <PID> -e trace=clone,clone3,futex

# Or trace clone calls only:
strace -f -p <PID> -e trace=clone,clone3

# More verbose (see all thread-related syscalls):
strace -f -p <PID> -e trace=clone,clone3,futex,mmap,mprotect,set_robust_list
```

### What You'll See

When the program creates platform threads, you'll see:
```
[pid XXXXX] clone(child_stack=0x7f1234567000, flags=CLONE_VM|CLONE_FS|CLONE_FILES|CLONE_SIGHAND|CLONE_THREAD|CLONE_SYSVSEM|CLONE_SETTLS|CLONE_PARENT_SETTID|CLONE_CHILD_CLEARTID, parent_tid=[YYYYY], tls=0x7f1234568700, child_tidptr=0x7f12345689d0) = YYYYY
```

**Key Observation:** Each `new Thread().start()` results in a `clone()` system call that creates a **new OS thread**.

## Expected Output

```
[STARTED] Platform Thread Demo - PID = 12345
Creating 3 platform threads...
[Platform Thread-1] Started - TID = 12346
[Platform Thread-2] Started - TID = 12347
[Platform Thread-3] Started - TID = 12348
[Platform Thread-1] Doing work...
[Platform Thread-2] Doing work...
[Platform Thread-3] Doing work...
[Platform Thread-1] Completed
[Platform Thread-2] Completed
[Platform Thread-3] Completed
All platform threads completed
[COMPLETED] Platform Thread Demo - PID = 12345
```

## The Morale

- **Platform threads are HEAVY** - Each thread consumes ~2MB of stack (native memory)
- **clone() system call** - Each thread creation requires OS involvement
- **Limited scalability** - You can't create millions of platform threads
- **Context switching overhead** - OS scheduler manages all threads
- **This is what Java used for 20+ years** (from JDK 1.3 onwards)

## Comparison: Platform Threads vs Virtual Threads

| Aspect | Platform Threads | Virtual Threads |
|--------|------------------|-----------------|
| **System Call** | `clone()` per thread | `clone()` for carrier pool only |
| **OS Threads Created** | N threads → N OS threads | N threads → ~CPU count OS threads |
| **Stack Location** | Native memory (off-heap) | JVM heap |
| **Max Threads** | ~Thousands | ~Millions |
| **Use Case** | Traditional Java apps (JDK 1.3-20) | Modern Java apps (JDK 21+) |

## Further Reading

- [Man page: clone(2)](https://linux.die.net/man/2/clone) - Linux system call for creating threads
- [Man page: futex(2)](https://linux.die.net/man/2/futex) - Fast userspace mutex
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444) - Explains why platform threads are being replaced

