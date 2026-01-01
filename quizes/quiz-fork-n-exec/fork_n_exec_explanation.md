# The power of combining fork() + execve() system calls

- The combination of these 2 system calls for a long time was the only possible way to write concurrent programs
- These system calls were a building blocks that allowed engineers to break down software on a multiple "logical flows"
- Initially, these "logical flows" were executed only concurrently (one at a time)
- Later (multiprocessor/multicore computers), these "logical flows" were executed in parallel (multiple at a time)
- Various IPC techniques were used to organize coordination among those "logical flows" (pipes, sockets, signals, etc.)
- "Berkeley POSTGRES" (June 1989) is the classical example of an old product that has such "multi-process" design
- https://github.com/postgres/postgres/blob/master/src/backend/postmaster/fork_process.c#L66
- Postgres only uses fork() system calls and don't use execve() system calls
- They are doing this to maximize the reaction speed on a new established network connection:
    - Single user-space/kernel-space roundtrip is cheaper than two such round trips
    - Full re-initialization of a virtual memory addresses is an expensive exercise
- Java community was innovative and was annoyed by existing IPC-coordination burdens
    - That's how a new "building-block" was introduced for concurrent programming (so-called "Green Thread")
    - At that moment in time, OS kernels were not have native kernel threads yet
    - NPTL (Native POSIX Thread Library) finalized in Linux 2.6 (17 December 2003)
    - Later, kernel native threads were introduced as a new "building-block" for concurrent programming
    - Obviously, in that world there were no place for "Green Threads" anymore

# Conclusions:

- Before Linux (and UNIX-like systems in general) had native kernel threads, software engineers still did
  concurrency, but they did it using cooperative and process-based techniques rather than today’s preemptive,
  shared-memory threading model.

# Chronology:

1. Multiple processes (the original UNIX model) : fork() + IPC
   Early UNIX had processes only, no threads.
   Software engineers achieved concurrency by fork()-ing multiple processes and coordinating them using pipes, signals,
   shared memory (SysV IPC), semaphores, sockets.

   Pros:
    - True parallelism on multiprocessor systems (later)
    - Strong isolation (crashes don’t corrupt siblings)

   Cons:
    - Heavyweight (process creation is expensive)
    - IPC is slower than shared memory
    - Complex coordination

   Examples:
    - Apache Prefork
    - Postgres
    - Mail Servers
    - Shells

2. Event loops + non-blocking I/O (cooperative multitasking)
   Before threads, many systems used single-process & event-driven designs.
   select(), poll() (and earlier equivalents)

   Key Traits:
    - Used non-blocking I/O
    - Waited for events
    - Explicitly switched between tasks
    - The program never blocks
    - Tasks voluntarily give up control

   Pros:
    - No locking
    - Very memory-efficient

   Cons:
    - Complex control flow
    - One blocking call can freeze everything
    - Hard to scale on multiple CPUs

   Examples:
    - Early network servers
    - GUIs
    - Embedded systems
    - This is the ancestor of Node.js, async/await, and reactive frameworks !!!

3. User-level threads ("green threads")
   Before kernel threads, many systems implemented threads entirely in user space !!!
   A runtime library managed multiple "threads"
   Context switching done by saving registers & stack
   The kernel saw one process !!!

   Scheduling:
    - Cooperative (yield points)
    - Sometimes timer-based via signals

   Examples:
    - GNU Portable Threads (GNU Pth)
    - Solaris green threads (early)
    - Java green threads (pre-1.3)
    - Many Lisp runtimes

   Pros:
    - Extremely fast context switches
    - Portable across OSes
    - No kernel involvement

   Cons:
    - Blocking syscall blocks all threads
    - No true parallelism on multiple CPUs
    - Hard to integrate with system libraries

4. Coroutines and Continuations
   Coroutines are functions that can suspend and resume with explicit yield control
   Characteristics:
    - Cooperative
    - Deterministic scheduling
    - No preemption

   Examples:
    - Simula (1960s!)
    - Modula-2
    - Early C libraries
    - Game loops
    - Modern revival: Kotlin coroutines, Python generators, C++20 coroutines !!!

5. Signals as a "poor man's concurrency"
   Software engineers abused OS signals to interrupt execution, run small handlers and emulate asynchronous events
   Signals influenced async I/O APIs but are rarely used directly now

   Problems:
    - Extremely limited
    - Reentrancy nightmares
    - Hard to reason about

6. Hardware-level parallelism without threads.
   Even before OS native threads, multiprocessor machines existed.
   OS scheduled processes on multiple processing units.
   Parallelism came from multiple processes, not threads.
   Threads were introduced mainly to reduce overhead, enable shared-memory programming and improve scalability.

7. When Linux actually got threads
   Linux originally had:
    - Processes only (task_struct)
    - clone() introduced in mid-90s
    - NPTL (Native POSIX Thread Library) finalized in Linux 2.6 (17 December 2003)

   After that:
    - Threads became cheap
    - POSIX pthread became practical
    - User-space threading mostly vanished !!!
    - Programming languages massively refused their user-space threading models in favor of "new shine kernel thing"

# CONCLUSIONS

- Key insight: Concurrency existed long before threads. Threads merely made it easier and cheaper.
- Modern systems actually combine old ideas:
  a. Event loops (async)
  b. Coroutines (structured concurrency)
  c. Kernel threads (parallel execution)
