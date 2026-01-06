# Concurrency Primitives Demonstrations

This directory contains hands-on demonstrations of different concurrency models in Java, traced with `strace` to show the underlying system calls.

## Overview

| Example | Concurrency Model | System Calls | OS Threads Created | Scalability |
|---------|-------------------|--------------|-------------------|-------------|
| **01-process-based** | Multiple processes | `fork()`, `execve()` | N processes | Low (hundreds) |
| **02-platform-thread-based** | Platform threads (1:1) | `clone()` per thread | N threads → N OS threads | Medium (thousands) |
| **03-virtual-thread-based** | Virtual threads (M:N) | `clone()` for carriers only | N threads → ~CPU count OS threads | High (millions) |

## Quick Start

### 1. Process-Based Concurrency
```bash
cd 01-process-based-java-concurrency
java src/ProcessBasedJavaConcurrency.java

# In another terminal:
strace -f -p <PID> -e trace=fork,execve,clone
```
**What to observe:** `fork()` and `execve()` system calls for each new process

---

### 2. Platform Thread-Based Concurrency
```bash
cd 02-platform-thread-based-java-concurrency
java src/PlatformThreadBasedJavaConcurrency.java

# In another terminal:
strace -f -p <PID> -e trace=clone,clone3
```
**What to observe:** One `clone()` system call per thread creation

---

### 3. Virtual Thread-Based Concurrency
```bash
cd 03-virtual-thread-based-java-concurrency
java src/VirtualThreadBasedJavaConcurrency.java

# In another terminal:
strace -f -p <PID> -e trace=clone,clone3
```
**What to observe:** 
- Initial `clone()` calls (carrier pool creation)
- **NO additional** `clone()` calls when creating 1000 virtual threads!

---

## The Evolution of Java Concurrency

### Era 1: Process-Based (Pre-1995)
- **Model:** Each concurrent task = separate process
- **System Call:** `fork()` + `execve()`
- **Problem:** Heavy weight, slow IPC, high memory usage
- **Example:** CGI scripts, traditional Unix daemons

### Era 2: Platform Threads (1995-2023)
- **Model:** Each concurrent task = OS thread (1:1 mapping)
- **System Call:** `clone()` per thread
- **Java Version:** JDK 1.0 (Green Threads) → JDK 1.3+ (Native Threads)
- **Problem:** Limited scalability (~10K threads max), high memory usage (~2MB per thread)
- **Workaround:** Reactive programming, async/await patterns

### Era 3: Virtual Threads (2023+)
- **Model:** Millions of virtual threads on few OS threads (M:N mapping)
- **System Call:** `clone()` only for carrier pool
- **Java Version:** JDK 21+
- **Benefit:** Millions of threads, simple synchronous code, low memory usage
- **Success:** Return to simple, readable code without reactive complexity

---

## Key System Calls Explained

### `fork()` - Create a new process
```c
pid_t fork(void);
```
- Creates a **complete copy** of the calling process
- Child gets its own memory space (copy-on-write)
- Returns 0 to child, child PID to parent

### `execve()` - Execute a new program
```c
int execve(const char *pathname, char *const argv[], char *const envp[]);
```
- Replaces current process image with a new program
- Used after `fork()` to run a different program
- Memory space is replaced, but PID remains the same

### `clone()` - Create a new thread
```c
int clone(int (*fn)(void *), void *stack, int flags, void *arg, ...);
```
- Creates a new thread (or process, depending on flags)
- Shares memory space with parent (when `CLONE_VM` flag is set)
- This is how Java platform threads are created

### `clone3()` - Modern variant of clone
- Newer, more extensible version of `clone()`
- Used in recent Linux kernels
- Same purpose as `clone()` but with better API

---

## Tracing Tips

### See all concurrency-related syscalls:
```bash
strace -f -p <PID> -e trace=fork,vfork,clone,clone3,execve,futex
```

### Focus on thread creation only:
```bash
strace -f -p <PID> -e trace=clone,clone3
```

### Count syscalls:
```bash
strace -f -p <PID> -e trace=clone,clone3 -c
```

### Follow specific child thread:
```bash
strace -f -p <PID> -e trace=all --trace=<TID>
```

---

## Hands-On Exercise

**Goal:** Prove that virtual threads don't create OS threads

1. **Run platform thread demo:**
   ```bash
   cd 02-platform-thread-based-java-concurrency
   java src/PlatformThreadBasedJavaConcurrency.java
   ```
   In another terminal:
   ```bash
   strace -f -p <PID> -e trace=clone,clone3 2>&1 | tee platform_trace.log
   ```
   **Count** the `clone()` calls: Should be **3** (for 3 threads)

2. **Run virtual thread demo:**
   ```bash
   cd 03-virtual-thread-based-java-concurrency
   java src/VirtualThreadBasedJavaConcurrency.java
   ```
   In another terminal:
   ```bash
   strace -f -p <PID> -e trace=clone,clone3 2>&1 | tee virtual_trace.log
   ```
   **Count** the `clone()` calls: Should be **~8-16** (carrier pool only), despite creating 1000 threads!

3. **Compare:**
   ```bash
   echo "Platform threads - clone() calls:"
   grep -c "clone(" platform_trace.log
   
   echo "Virtual threads - clone() calls:"
   grep -c "clone(" virtual_trace.log
   ```

---

## The Proof

### Platform Threads (02-platform-thread-based)
```
Creating 3 threads...
strace output:
  clone(...) = 12346  ← Thread 1 created
  clone(...) = 12347  ← Thread 2 created
  clone(...) = 12348  ← Thread 3 created

Result: 3 threads = 3 OS threads = 3 clone() calls ✅
```

### Virtual Threads (03-virtual-thread-based)
```
Creating 1000 threads...
strace output:
  clone(...) = 12346  ← Carrier 1
  clone(...) = 12347  ← Carrier 2
  ... (~8 total for carrier pool)
  [NO MORE clone() calls!]

Result: 1000 threads = ~8 OS threads = ~8 clone() calls ✅
```

**Conclusion:** Virtual threads achieve **125x fewer OS threads** (1000 / 8) for the same workload!

---

## Requirements

- **Linux** - `strace` is a Linux tool
- **JDK 11+** - For examples 01-02
- **JDK 21+** - For example 03 (virtual threads)

---

## Further Reading

- [Man pages for fork, clone, execve](https://linux.die.net/man/)
- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [Baeldung: Linux fork, vfork, exec, clone](https://www.baeldung.com/linux/fork-vfork-exec-clone)
- [Inside Java: Virtual Threads Deep Dive](https://inside.java/tag/virtual-threads)

