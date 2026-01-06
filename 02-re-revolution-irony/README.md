# Project Loom: A Re:Revolution, Not a Revolution

**The Thesis:** Project Loom (JDK 21, 2023) is not an evolution or revolution - it's a **Re:Revolution**. Java had the
right concurrency model in 1996, abandoned it in 2000, watched Go resurrect it in 2009, and finally circled back 27
years later. This is the story of that journey.

---

## The Parallelism Timeline

To understand why Loom is a return to roots, we must trace when concurrent programs actually started running in
parallel. The journey spans two eras: the **Multiprocessor Era** (multiple physical chips connected by wires) and the *
*Multicore Era** (multiple processing units on a single piece of silicon).

---

## Historical Milestones

### 1. The First Multiprocessor (True Parallelism Begins)

**Date:** 1962  
**System:** Burroughs D825

While early mainframes often had separate "I/O processors" to handle tape drives, the **Burroughs D825** (built for the
US Navy) is widely considered the first true **Symmetric Multiprocessor (SMP)**.

**The Breakthrough:** It connected up to four CPU modules to a shared memory pool using a crossbar switch.

**The "Parallel" Reality:** This allowed up to four instruction streams to execute at the exact same nanosecond.
However, at this time, parallelism was used for throughput (running four different batch jobs for four different people)
rather than speeding up a single concurrent program.

### 2. The First Multicore Processor (Parallelism Goes Micro)

**Date:** 2001 (Server) / 2005 (Consumer)  
**Chips:** IBM POWER4 and AMD Athlon 64 X2

This is the era that likely impacts your day-to-day experience. Before this, "concurrent" code on a home PC was an
illusion created by the OS switching tasks very quickly (time-slicing).

**The First Commercial Multicore (2001):** The **IBM POWER4** was the first non-embedded dual-core processor. It was a
monster chip designed for high-end servers, featuring two cores on a single die.

**The "Consumer" Revolution (2005):** This is the specific moment concurrent desktop software became parallel.

- **Intel** rushed the **Pentium D** to market in April 2005. It was technically a "multi-chip module" (two separate
  single-core dies glued into one package).
- **AMD** released the **Athlon 64 X2** in **May 2005**. This was the first "native" dual-core desktop processor (two
  cores printed on the same silicon die), allowing much faster communication between cores.

### 3. The "Linux Thread" Revolution (The clone Era)

**Date:** June 9, 1996  
**Event:** Release of Linux Kernel 2.0

This is when the Linux kernel gained the ability to handle threads, but in a uniquely "Linux" way.

- **The System Call:** This kernel introduced `clone()`.
- **The Concept:** Before this, Linux only had `fork()` (which creates a distinct copy of a process). `clone()` was
  revolutionary because it allowed a new process to be created that shared parts of its execution context (like the
  virtual address space, file descriptor table, and signal handlers) with its parent.
- **The Philosophy:** Uniquely, Linux did not actually create a separate "Thread" entity. To the Linux kernel, a thread
  is just a "process that shares memory." They are both schedulable entities called tasks.

### 4. The "Modern" Threading Era (NPTL)

**Date:** December 17, 2003  
**Event:** Release of Linux Kernel 2.6

While `clone()` existed since 1996, early Linux threading (via a library called **LinuxThreads**) was clumsy. It
suffered from the "giant lock" problem and didn't fully comply with POSIX standards (signals were a nightmare).

The release of Kernel 2.6 introduced the **Native POSIX Thread Library (NPTL)** support.

- **The Change:** This optimized `clone()` to handle thousands of threads efficiently and introduced the `futex` (fast
  userspace mutex) system call.
- **The Result:** This was the moment Linux threading became "enterprise-ready," capable of running complex concurrent
  applications (like Java virtual machines or massive database servers) in true parallel parity with proprietary Unix
  systems like Solaris.

### 5. The "Green" Era (Java's Original Innovation)

**Date:** 1991 (Oak Project) / January 23, 1996 (JDK 1.0 Released)  
**Event:** Green Threads Introduced

**Context:** James Gosling, Mike Sheridan, and Patrick Naughton initiated the "Green Project" (which became Oak, then
Java) in 1991. They were designing for embedded devices (set-top boxes), not massive servers.

**The Model (M:1):** The JVM created threads in user space. The underlying OS (Windows 95, Solaris, Linux) only saw one
single process.

**The Innovation:** Lightweight, cheap threads that could be spawned by the thousands without OS overhead.

**The Problem:**

- If that one process hit a blocking system call (like I/O), the entire JVM paused.
- On the few multiprocessor machines that existed (like the Burroughs D825 successors), Java could effectively use only
  one CPU because the OS didn't know the other threads existed.

**Verdict:** The right idea for high-concurrency workloads, but too early for the hardware landscape of the mid-1990s.

### 6. The "Native" Pivot (Java Abandons the Innovation)

**Date:** May 8, 2000  
**Event:** JDK 1.3 Released - Green Threads Effectively Deprecated

While native threads were introduced optionally in JDK 1.2, **JDK 1.3 was the turning point** where Sun Microsystems
effectively declared Green Threads dead in favor of "Native Threads."

**The Shift:** Java moved to a **1:1 Model**. Every Java thread became a real OS thread (using libraries like
LinuxThreads initially, and later NPTL).

**Why They Did It:**

- To utilize the multiprocessor hardware of the late 1990s
- To avoid the "blocking I/O" problem of early Green Threads
- Pressure from enterprise customers running on expensive SMP servers

**The "Bad Luck" Timing:** Java committed fully to heavy OS threads (1:1) just a few years before the **C10K problem** (
handling 10,000 concurrent connections) became famous in 1999. They optimized for CPU parallelism right before the world
needed massive I/O concurrency.

**The Cost:** Each Java thread now consumed ~2MB of stack space. Spawning 100,000 threads became impossible.

### 7. The Go Renaissance (Re-Introducing the Innovation)

**Date:** September 21, 2007 (Design Starts) / November 10, 2009 (Open Source Release)  
**Event:** Goroutines Introduced

**Context:** Rob Pike, Ken Thompson, and Robert Griesemer started sketching Go at Google in 2007. They were living in
the future—Google had massive multicore servers and needed to handle hundreds of thousands of concurrent requests.

**The Model (M:N):** This is the **evolution of Green Threads**. Go multiplexes thousands of Goroutines (M) onto a
smaller number of OS threads (N).

**The Glory:** Because they launched in 2009 (post-multicore revolution, post-NPTL stability), they could build a
scheduler that:

- Used multiple cores effectively (unlike Java's old Green Threads)
- Kept threads lightweight (unlike Java's Native Threads)

**The Irony:** Go was hailed as **revolutionary** for "Goroutines"—but it was effectively what Java had invented and
then deleted, now fixed for the multicore era.

---

### 8. The Re:Revolution (Java Comes Full Circle)

**Date:** September 19, 2023  
**Event:** JDK 21 Released with Project Loom (Virtual Threads)

**The Return:** After 27 years, Java brings back the Green Thread model under the name "Virtual Threads."

**The Model (M:N):** Just like Go's Goroutines, Virtual Threads multiplex many lightweight threads onto a pool of OS
threads.

**The Features:**

- Millions of virtual threads can run concurrently (vs. thousands of platform threads)
- Automatic yielding on blocking I/O operations
- Seamless integration with existing Java APIs

**The Reality:** This is **not** an evolution or revolution. This is Java:

1. Inventing the right solution in 1996
2. Abandoning it in 2000 for short-term SMP performance
3. Watching Go resurrect and perfect it in 2009
4. Finally returning to roots in 2023

**The Competitive Angle:** Virtual Threads allow Java to compete with Go again in the high-concurrency,
microservices-dominated world. But it's not innovation—it's admitting the original path was correct all along.

---

## Summary: The Java Concurrency Journey

| Year     | Java's State              | Context                                                                                                                                                               |
|----------|---------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **1996** | Green Threads (M:1)       | Single-core CPUs dominated. Good for simple concurrency, but couldn't utilize multiple CPUs.                                                                          |
| **2000** | Native Threads (1:1)      | SMP (Multi-CPU) servers appeared. Java wins performance on multiprocessor machines but sacrifices concurrency scalability.                                            |
| **2005** | Stuck with Native Threads | Multicore explosion (Athlon 64 X2 / Core 2 Duo). Java threads are now too "heavy" (~2MB stack size). You can't spawn 100,000 threads.                                 |
| **2009** | Still Struggling          | Cloud/Microservices boom. Java requires complex frameworks (Netty/NIO) to handle high concurrency. **Go launches with Goroutines** and gets praised for "innovation." |
| **2023** | Virtual Threads (M:N)     | **Project Loom arrives.** Java re-invents what it deleted—effectively what Go started with, completing a 27-year circle.                                              |

---

## The Hardware Context: When Did Code Become Parallel?

**System Level (1962):**

- **Burroughs D825** - First Multiprocessor (Hardware)
- The computer can run the OS and a user program simultaneously

**Prosumer Level (1990s):**

- **Dual-Socket PCs** - High-end workstations (like the dual Pentium Pro) allowed enthusiasts to run parallel threads,
  but required an expensive motherboard with two physical sockets

**Linux Level:**

- **1996** - Linux Kernel 2.0: `clone()` introduced. Linux gains the ability to run "threads" (processes that share
  memory)
- **2003** - Linux Kernel 2.6: NPTL introduced. Linux threading becomes efficient and standard-compliant

**Application Level (2005):**

- **Athlon 64 X2 / Core 2 Duo** - The "free lunch" of clock speed increases ended. Suddenly, standard software had to be
  written concurrently to run faster. This forced the entire software industry to shift from single-threaded to
  multi-threaded programming

**Verdict:** Concurrency became Parallelism for the military in 1962, but it didn't become parallel for the rest of us
until 2005.

---

## Technical Distinctions

### Concurrency vs. Parallelism

- **Concurrency** is about structure: Writing a program so that tasks can happen independently (e.g., handling a mouse
  click while calculating a spreadsheet).
- **Parallelism** is about execution: Actually executing those independent tasks at the exact same instant.

### Threading Models

- **M:1 (Green Threads)** - Many user-space threads mapped to one OS thread. Lightweight but can't use multiple CPUs.
- **1:1 (Native Threads)** - Each user thread maps to one OS thread. Can use multiple CPUs but heavy (~2MB stack per
  thread).
- **M:N (Virtual Threads/Goroutines)** - Many user-space threads mapped to N OS threads. Best of both worlds:
  lightweight AND multi-core capable.

---

## The C10K Problem (1999)

In 1999, Dan Kegel posed the **C10K problem**: How do you handle 10,000 concurrent connections on a single server?

**The Challenge:** With Native Threads (1:1 model), each connection = one OS thread = ~2MB of memory. 10,000
connections = 20GB just for thread stacks.

**The Proof:** This was the specific benchmark that proved Native Threads were a dead end for high concurrency. It's
what drove:

- Event-driven frameworks (Node.js, Netty)
- Async/await patterns
- And eventually... a return to Green Threads (Go, then Java)

**The Irony:** Java abandoned Green Threads in 2000, right before the C10K problem made them essential.

---

## The Final Verdict

**Project Loom is not a revolution.** It's Java finally admitting that:

1. The 1996 Green Threads idea was right for modern workloads
2. The 2000 pivot to Native Threads was a strategic mistake
3. Go's "revolutionary" Goroutines were just Green Threads done correctly
4. After 23 years of watching Go dominate high-concurrency use cases, Java has come back to where it started

**This is a Re:Revolution** - a return to roots with the benefit of modern hardware and hindsight.

That's not innovation - that's coming full circle. Java invented the future in 1996, deleted it in 2000, watched Go
resurrect it in 2009, and finally brought it back in 2023.

---

**[← Previous: Java's Long Struggle with Concurrency Primitives](../01-java-long-struggling-with-concurrency-primitives/README.md)** | **[Next: Rob Pike Persona & Go Influence →](../03-rob-pike-persona-and-go-influence/README.md)**

