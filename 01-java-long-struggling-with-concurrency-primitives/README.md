# Java's Long Struggle: How Programming Language Designers Failed to Adapt (Until Project Loom)

**Core Thesis:** For over two decades, Java's programming language designers struggled to adapt the language to the
post-multicore era. Multiple attempts by both the JDK team and the community failed to provide a simple, efficient
solution - until Project Loom finally delivered what software engineers had been waiting for.

---

## Understanding the Abstraction Layers

Before diving into Java's struggle, we must understand the **separation of duties** in our industry. There are **four
distinct abstraction layers**, each with its own responsibilities:

### The Four Layers

1. **Scientists** (Mathematicians, Physicists, Chemists)
    - Extend our understanding of reality through exploration
    - Discover the laws of nature

2. **Hardware Vendors** (Intel, AMD, ARM, etc.)
    - Follow scientific discoveries
    - Build hardware that obeys the laws of nature

3. **Programming Language Designers**
    - Create toolchains for software engineers
    - Provide concurrency building blocks and abstractions

4. **Software Engineers** (Us)
    - Implement domain-specific models
    - Guided by business requirements

### The Ripple Effect

Changes cascade down through these layers:

- Changes in **laws of nature** → trigger changes in **hardware architectures**
- Changes in **hardware architectures** → trigger changes in **programming language designs**
- Changes in **programming language designs** → trigger changes in **how software engineers write code**

**Critical Point:** Each layer should focus on its own concerns. Communication should occur **only between neighboring
layers**. As software engineers, we have **one neighbor above us**: programming language designers.

---

## Fundamental Concepts

### Reality is Parallel, Not Object-Oriented

- The universe/reality is inherently **parallel**, not object-oriented.
- Software engineering is an attempt to design and program a simplified model of a specific subset of reality.

### Concurrency vs. Parallelism

- **Concurrency** is about **dealing with** a lot of things at once (structure/design)
- **Parallelism** is about **doing** a lot of things at once (execution)

In practical terms:

- **Concurrency** = How we write/organize code to keep our codebase extensible and maintainable
- **Parallelism** = How our code will be executed against hardware/laws of nature

---

## What Should Software Engineers Worry About?

### Should We Worry About Concurrency?

**No.** Technically, we don't have control over it (hello, Stoics!). Programming language designers already designed "concurrency building blocks" and gave them to us. We can only:

- Apply them to program our "reality models" (a.k.a. "software")
- Give feedback to programming language designers about our Developer Experience (DevEx)

### Should We Worry About Parallelism?

**No.** We don't have control over it either. Hardware vendors already built the hardware, and we already bought/rented
it. We can only run our code against the hardware we have access to.

### What SHOULD We Worry About?

As software engineers, our responsibilities are:

1. **Keep our understanding of reality up-to-date**
2. **Focus on the business** and master our ability to find the best existing tool for a specific job
3. **Know what's happening in the "programming languages" space** (direct influence on us)
4. Optionally know what's happening in "hardware" space (less relevant)
5. Optionally know what's happening in "science" space (even less relevant)
6. **Be professionals in our own duties** and let other professionals do their work in their zone of responsibilities

---

## Software + Hardware = Cost of development & operations

**Statement:** There is no need to buy more hardware to handle the same workload if we can achieve the same results
using fewer computational resources by **programming differently** or **using a different programming language**. Such
thinking might give a competitive advantage to our business and helps to win market share (cost-wise).

---

## Java's Long Struggle: A Timeline of Failed Attempts

### The Problem

When the multicore era arrived (~2005), Java was stuck with **heavy Native Threads (1:1 model)**. Each thread consumed ~
2MB of stack space, making it impossible to handle tens of thousands of concurrent connections efficiently. The world
moved to high-concurrency, I/O-bound workloads, but Java's threading model was optimized for CPU-bound parallelism.

### JDK's Attempts (Programming Language Designers' Efforts)

The JDK team made multiple attempts to improve DevEx for async/non-blocking programming:

1. **Java NIO (2002)** - Non-blocking I/O with selectors
2. **ExecutorService (2004)** - Thread pools and task scheduling
3. **Fork/Join Framework (2011)** - For divide-and-conquer algorithms
4. **CompletableFuture (2014)** - Composition of asynchronous operations
5. **Reactive Streams (2015)** - Specification for async stream processing

**The Verdict:** All of these were **still not good enough**. They were complex, hard to reason about, and forced
software engineers to write "callback hell" or complex reactive chains. This can be understood due to:

- Java's long history
- Backward-compatibility-first philosophy
- The fundamental limitation of heavy Native Threads

### The Community's Struggle (Software Engineers' Attempts)

Meanwhile, Java's community of software engineers **felt these inconveniences and inefficiencies**. For many years, they
tried to deliver their own solutions:

#### Community Solutions

- **Scala Actors** - Actor model for concurrency
- **Kotlin Coroutines** - Lightweight threads for Kotlin
- **RxJava** - Reactive extensions for Java
- **Project Reactor** - Reactive streams implementation
- **Reactive Streams Specification** - Standardization effort
- **Netty** - Async event-driven network framework
- **Akka** - Actor toolkit for JVM
- **Vert.x** - Reactive toolkit

**Why They All Failed to Solve the Core Problem:**

These attempts were **far from simple and convenient** to use. Here's why:

1. **Wrong abstraction layer** - Software engineers operate at a different level of abstraction
2. **Not their responsibility** - It was never software engineers' job to fix programming language deficiencies
3. **Complexity remained** - All solutions introduced their own mental models, learning curves, and complexity
4. **Ecosystem fragmentation** - Multiple competing solutions created confusion

**The Fundamental Issue:** Software engineers were trying to solve a problem that **programming language designers**
should have solved. This is a violation of the separation of duties - the community was forced to operate outside their
layer.

---

## Project Loom: The Long-Awaited Solution

### What Changed?

In **September 2023**, with the release of **JDK 21**, Project Loom introduced **Virtual Threads**.

### Why It Finally Works

Project Loom solved the problem by:

1. **Operating at the right abstraction layer** - Programming language designers fixed the language
2. **Returning to the right model** - M:N threading (like Green Threads, but multicore-capable)
3. **Simplicity** - Code looks like blocking/synchronous code
4. **Efficiency** - Code executes efficiently against modern hardware
5. **Backward compatibility** - Existing Java code works with minimal changes

### The Developer Experience We've Been Waiting For

As software engineers, we **finally received an instrument that satisfies our needs**:

- **Write code simply** - Looks like blocking, synchronous code  
- **Execute efficiently** - Runs efficiently against any hardware  
- **Stop trying to improve the programming language** - Language designers did their job  
- **Be calm and satisfied** - The problem is solved at the right abstraction layer

### The Bigger Picture

Project Loom allows us to:

- **Stop worrying about async/await, callbacks, and reactive streams**
- **Write straightforward, easy-to-understand & easy-to-maintain code**
- **Let the JVM handle the complexity** of scheduling millions of virtual threads
- **Focus on business logic** instead of concurrency primitives

---

## The Final Lesson

**Languages influence each other** - One language might introduce innovation, and others might borrow new concepts,
reshape them, or not. Go introduced Goroutines in 2009, and it took Java **14 years** to bring a similar model to the
JVM.

**Responsibility matters** - When software engineers try to solve problems that should be solved by programming language
designers, the solutions are inevitably complex, fragmented, and unsatisfying.

**Patience paid off** - After two decades of struggling, Java finally has a concurrency model that is:

- Simple to use
- Efficient on modern hardware
- Appropriate for the post-multicore era

**We can finally focus on what matters:** Building great software, not wrestling with concurrency primitives.

---

**[Next: Re:Revolution Irony →](../02-re-revolution-irony/README.md)**
