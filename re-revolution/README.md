# Parallelism is Alive

To pinpoint the revolutionary moments when concurrent programs actually started running in parallel, we must distinguish between two separate eras: the
**Multiprocessor Era** (multiple physical chips connected by wires) and the **Multicore Era** (multiple processing units on a single piece of silicon).

# Timeline

1. **The First Multiprocessor (True Parallelism begins)**
   **Date:** 1962
   **System:** Burroughs D825

While early mainframes often had separate "I/O processors" to handle tape drives, the **Burroughs D825** (built for the US Navy) is widely considered the first
true
**Symmetric Multiprocessor (SMP)**.

The Breakthrough: It connected up to four CPU modules to a shared memory pool using a crossbar switch.

**The "Parallel" Reality:** This allowed up to four instruction streams to execute at the exact same nanosecond. However, at this time, parallelism was used for
throughput (running four different batch jobs for four different people) rather than speeding up a single concurrent program.

2. The First Multicore Processor (Parallelism goes Micro)
   **Date:** 2001 (Server) / 2005 (Consumer) Chips: IBM POWER4 and AMD Athlon 64 X2

This is the era that likely impacts your day-to-day experience. Before this, "concurrent" code on a home PC was an illusion created by the OS switching tasks
very quickly (time-slicing).

**The First Commercial Multicore (2001):** The **IBM POWER4** was the first non-embedded dual-core processor. It was a monster chip designed for high-end
servers,
featuring two cores on a single die.

**The "Consumer" Revolution (2005):** This is the specific moment concurrent desktop software became parallel.

**Intel** rushed the **Pentium D** to market in April 2005. It was technically a "multi-chip module" (two separate single-core dies glued into one package).

**AMD** released the **Athlon 64 X2** in **May 2005**. This was the first "native" dual-core desktop processor (two cores printed on the same silicon die),
allowing much
faster communication between cores.

3. **The "Linux Thread" Revolution (The clone era)**
   **Date:** June 9, 1996 **Event:** Release of Linux Kernel 2.0

This is the exact moment you asked for: when the Linux kernel gained the ability to handle threads, but in a uniquely "Linux" way.

- **The System Call:** This kernel introduced clone().

- **The Concept:** Before this, Linux only had fork() (which creates a distinct copy of a process). clone() was revolutionary because it allowed a new process
  to be created that shared parts of its execution context (like the virtual address space, file descriptor table, and signal handlers) with its parent.

- **The Philosophy:** Uniquely, Linux did not actually create a separate "Thread" entity. To the Linux kernel, a thread is just a "process that shares memory."
  They are both schedulable entities called tasks.


4. The "Modern" Threading Era (NPTL)
   **Date:** December 17, 2003 **Event:** Release of Linux Kernel 2.6

While clone() existed since 1996, early Linux threading (via a library called **LinuxThreads**) was clumsy. It suffered from the "giant lock" problem and didn't
fully comply with POSIX standards (signals were a nightmare).

The release of Kernel 2.6 introduced the **Native POSIX Thread Library (NPTL)** support.

- **The Change:** This optimized clone() to handle thousands of threads efficiently and introduced the futex (fast userspace mutex) system call.

- **The Result:** This was the moment Linux threading became "enterprise-ready," capable of running complex concurrent applications (like Java virtual machines
  or
  massive database servers) in true parallel parity with proprietary Unix systems like Solaris.

5. The "Green" Era (Java's Early Days)
   Date: June 1991 Event: Oak Project Starts James Gosling, Mike Sheridan, and Patrick Naughton initiated the "Green Project" (which became Oak, then Java).

Context: They were designing for embedded devices (set-top boxes), not massive servers.

Date: January 23, 1996 Event: JDK 1.0 Released (Green Threads Introduced) Java 1.0 launched with Green Threads as the default.

The Model (M:1): The JVM created the threads in user space. The underlying OS (Windows 95, Solaris, Linux) only saw one single process.

The Problem: If that one process hit a blocking system call (like I/O), the entire JVM paused. Furthermore, on the few multiprocessor machines that existed (
like the Burroughs D825 successors), Java could effectively use only one CPU because the OS didn't know the other threads existed.

6. The "Native" Pivot (Java abandons the innovation)
   Date: April 2000 (JDK 1.3 Release) Event: Green Threads Dropped for Native Threads (1:1) While introduced optionally in 1.2, JDK 1.3 was the turning point
   where Sun Microsystems effectively declared Green Threads dead in favor of "Native Threads."

The Shift: Java moved to a 1:1 Model. Every Java thread became a real OS thread (using libraries like LinuxThreads initially, and later NPTL).

Why they did it: To utilize the multiprocessor hardware of the late 90s and avoid the "blocking I/O" problem of early Green Threads.

The "Bad Luck" Timing: Java committed fully to heavy OS threads (1:1) just a few years before the "C10K problem" (handling 10k connections) became famous. They
optimized for CPU parallelism right before the world needed massive I/O concurrency.

7. The Go Renaissance (Re-introducing the innovation)
   Date: September 21, 2007 Event: Design of Go Starts Rob Pike, Ken Thompson, and Robert Griesemer started sketching Go at Google.

Context: They were living in the future. Google had massive multicore servers and needed to handle hundreds of thousands of concurrent requests.

Date: November 10, 2009 Event: Go Open Source Release Go is released to the public with Goroutines.

The Model (M:N): This is the evolution of Green Threads. Go multiplexes thousands of Goroutines (M) onto a smaller number of OS threads (N).

The Glory: Because they launched in 2009 (post-multicore revolution, post-NPTL stability), they could build a scheduler that used multiple cores (unlike Java's
old Green Threads) but kept threads lightweight (unlike Java's Native Threads).

# Summary: When did code become parallel?

- 1962 Burroughs D825 System Level:
  First Multiprocessor (Hardware)
  The computer can run the OS and a user program simultaneously.

- 1990s Dual-Socket PCs
  Prosumer Level: High-end workstations (like the dual Pentium Pro) allowed enthusiasts to run parallel threads, but it required an
  expensive motherboard with two physical sockets.

- 1996 Linux Kernel 2.0
  clone() introduced. Linux gains the ability to run "threads" (processes that share memory).

- 1996 Single-core CPUs dominated. Uses Green Threads (M:1). Good for simple concurrency, but slow on servers.
- 2000 SMP (Multi-CPU) servers appear. Switches to Native Threads (1:1). Java wins performance, effectively utilizing multiple CPUs.

- 2003 Linux Kernel 2.6
  NPTL introduced. Linux threading becomes efficient and standard-compliant.

- 2005 Athlon 64 X2 / Core 2 Duo
  Application Level: The "free lunch" of clock speed increases ended. Suddenly, standard software had to be written concurrently to
  run faster. This forced the entire software industry to shift from single-threaded to multi-threaded programming.

- 2005 Multicore Explosion. Stuck with Native Threads. Java threads are now too "heavy" (2MB stack size). You can't spawn 100k threads.
- 2009 Cloud/Microservices Boom. Struggling. Java requires complex frameworks (Netty/NIO) to handle concurrency.
- 2009 Go Launches. Re-invents Green Threads (M:N). Go is hailed as revolutionary for "Goroutines"—effectively what Java deleted, but fixed for multicore.

# Technical Distinction

- Concurrency is about structure: Writing a program so that tasks can happen independently (e.g., handling a mouse click while calculating a spreadsheet).
- Parallelism is about execution: Actually executing those independent tasks at the exact same instant.

**Verdict:** Concurrency became Parallelism for the military in 1962, but it didn't become parallel for the rest of us until 2005.

# The Irony

Java is currently working on Project Loom (Virtual Threads), released in JDK 21 (Sept 2023). This effectively brings Green Threads back to Java,
completing a 27-year full circle to reach the state Go started with.

# C10K Problem (1999)

- Specific benchmark that proved Native Threads were a dead end for high concurrency