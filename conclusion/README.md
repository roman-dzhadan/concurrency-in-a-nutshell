# Q.: What is the "concurrency"? What is the "parallelism"?

## A.: "Concurrency is not Parallelism" insightful talk by "Rob Pike" at "Waza 2012"

Why is worth to watch it? - https://youtu.be/oV9rvDllKEg

- Go was designed at Google in 2007 by Robert Griesemer, Rob Pike, and Ken Thompson, and publicly announced in November 2009
- Go is relatively young programming language and had privilege to not repeat mistakes of it's predecessors
- Rob Pike was one of the main inventors of Go and he is the speaker of the talk I'm recommending to watch
- Go has first-class support for concurrency through goroutines and channels
- Goroutines are lightweight threads managed by the Go runtime, allowing millions of concurrent tasks with minimal overhead
- Go shines in high-performance backends by efficiently utilizing hardware resources, especially the CPU

Thoughts & Statements:

- Universe/Reality is not object-oriented, it's parallel
- Software engineering is an attempt to design a simplified model (a specific subset of a reality) and program it
- Concurrency is about dealing with lot of things at once
- Parallelism is about doing a lot of things at once
- Concurrency is about how are we writing/organizing code to keep our codebase extensible/maintainable
- Parallelism is about how our code will be executed against hardware/laws-of-nature
- Programming languages have influence on each other (innovate/borrow cycles)

Separation of Duties (SoD):

- Scientists (mathematicians, physicists, and chemists) extend our understanding of reality through exploration
- Hardware vendors (e.g., Intel) translate scientific discoveries into hardware
- Programming language designers create toolchains for application developers
- Application developers implement domain-specific models guided by business requirements

Ripple Effects:

- Changes in laws-of-nature are triggering changes in hardware architectures
- Changes in hardware architectures are triggering changes in programming language designs
- Changes in programming language designs are triggering changes in how application developers are writing/organizing code

P.S.: These represent 4 abstraction layers
Each layer should focus on its own concerns, and communication should occur only between neighboring layers
As "application developers", we have only one neighbor above, which are "programming language designers"

Q.: As "application developers", should we be worried about "concurrency"?
No, technically, we don't have control over it (say hello to Stoics)
"Programming language designers" already designed "concurrency-building-blocks" and gave them to us
We can only apply them to program our "reality models" (a.k.a "software")
In meantime, we can give a feedback to "programming language designers" on how are we satisfied by DevEx

Q.: As "application developers", should we be worried about "parallelism"?
No, technically, we don't have control over it either
"Hardware vendors" already built hardware and we already bought/rent it
We can only run our code against hardware we have access to

Q.: As "application developers", what we should be worried about?
We have to keep our understanding of reality up-to-date
We have to be focused on a business and master our ability to find the best existing tool for a specific job and apply it
We must know what's happening in "programming languages" space as it has direct influence on us
It's good to know what's happening in "hardware" space, but it's less relevant
It's good to know what's happening in "science" space, but it's even less relevant
We have to be professionals in our own duties and let other professionals to do their work in their zone of responsibilities

Q.: How "hardware vendors" delivered "parallelism" in past?
A.: Historically, hardware vendors delivered parallelism long before modern multicore CPUs,
using several techniques at different levels of the hardware stack:

    - Bit-Level Parallelism
    - Instruction-Level Parallelism (ILP)
    - Vector and SIMD Processing
    - Multiple Functional Units
    - Hardware Multithreading (Pre-Multicore)
    - Multiprocessor Systems (SMP)
    - Specialized Parallel Hardware (GPUs, DSPs, FPGAs)

Q.: How "hardware vendors" are delivering "parallelism" nowadays?
A.: Nowadays, hardware vendors deliver parallelism in several more explicit and scalable ways than in the past.
Here’s a structured breakdown:

    - Multicore CPUs
    - Simultaneous Multithreading (SMT) / Hyper-Threading
    - Vector and SIMD Extensions
    - GPUs and Many-Core Accelerators
    - Heterogeneous Computing
    - Cache-Coherent and NUMA Architectures
    - On-Chip Interconnects & High-Bandwidth Memory
    - Hardware Support for Synchronization

Summary: Before multicore CPUs became mainstream, "hardware vendors" focused on extracting parallelism automatically,
without burdening "application developers"

	 Historically, hardware vendors delivered parallelism by exploiting instruction-level, data-level,
	 and pipeline parallelism inside CPUs—largely invisible to software - long before explicit
	 multi-threading and multicore systems became common.

Q.: How "application developers" did "concurrency" before native kernel threads were invented?
A.: Before Linux (and Unix-like systems in general) had native kernel threads,
"application developers" still did concurrency, but they did it using cooperative and process-based techniques
rather than today’s preemptive, shared-memory threading model

    Here’s the historical progression and the main techniques, roughly in the order they appeared
    
    1. Multiple processes (the original Unix model)
       fork() + IPC
       Early Unix had processes only, no threads
       Developers achieved concurrency by:
       	- fork()-ing multiple processes
	- Coordinating them using: pipes, signals, shared memory (SysV IPC), semaphores and sockets
       Pros
        - True parallelism on multiprocessor systems (later)
        - Strong isolation (crashes don’t corrupt siblings)
       Cons
        - Heavyweight (process creation is expensive)
 	- IPC is slower than shared memory
 	- Complex coordination

LET ME SHOW YOU POSTGRES EXAMPLE

Morale: There is no need to buy more hardware to handle the same workload if we can achieve the same results using fewer computational resources by programming
differently or using a different programming language

Q.: Why I'm talking all this time about Go
     
------------------------------------------------------------------------------------------------------------------------------------------
Statement 0:
One language might introduce innovation, and others will borrow it later
------------------------------------------------------------------------------------------------------------------------------------------




Q.: How did application developers implement parallelism or concurrency before the concept of threads was introduced in operating systems?

Before Linux (and Unix-like systems in general) had native kernel threads,
application developers still did concurrency,
but they did it using cooperative and process-based techniques
rather than today’s preemptive, shared-memory threading model.