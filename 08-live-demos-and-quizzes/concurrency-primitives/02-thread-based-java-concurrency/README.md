# Platform Thread-Based Java Concurrency

## What are Platform Threads?

Platform threads (also called "Native Threads" or "Kernel Threads") are Java threads that map 1:1 to OS threads. Each `Thread` object in Java creates a corresponding OS thread via the `clone()` system call on Linux.

## Quick: Run in two modes (platform or virtual)

This demo can be run in two modes:

- Platform threads (classic, 1:1 to OS threads)
- Virtual threads (lightweight threads introduced with Project Loom)

From the repository root, run these short commands:

```bash
# Run platform threads (default count 1000 if you omit a number)
java /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/concurrency-primitives/02-thread-based-java-concurrency/src/ThreadBasedJavaConcurrency.java platform 1000

# Run virtual threads (default count 1_000_000 if you omit a number)
java /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/concurrency-primitives/02-thread-based-java-concurrency/src/ThreadBasedJavaConcurrency.java virtual 1000000
```

Notes:
- Defaults: platform → 1_000 threads, virtual → 1_000_000 threads.
- Adjust counts for your machine; creating many platform threads can exhaust OS resources.

## Tracing platform threads with strace

To observe OS thread creation when running in platform mode, run `strace` in another terminal against the demo PID and trace `clone`/`futex` calls:

```bash
# In terminal A: run the demo (platform mode)
java -cp out ThreadBasedJavaConcurrency platform 10

# In terminal B: trace thread-related syscalls for the PID printed by the demo
strace -f -p <PID> -e trace=clone,clone3,futex
```

You will see `clone()` calls as new platform threads are created.

## The Morale

- **Platform threads are HEAVY** - Each thread consumes native stack memory
- **clone() system call** - Each thread creation requires OS involvement
- **Limited scalability** - You can't create millions of platform threads
- **Context switching overhead** - OS scheduler manages all threads

## JDK / compatibility note

- Virtual threads require a Loom-enabled JDK (JDK 21+ recommended). If your `java`/`javac` do not support `Thread.ofVirtual()`/`Thread.Builder`, upgrade to JDK 21 or newer.

## Further Reading

- [Man page: clone(2)](https://linux.die.net/man/2/clone) - Linux system call for creating threads
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444) - Explains why virtual threads were introduced
