# Disclaimer:

- Everything I say and show today reflects my personal opinions.
- I do not claim to hold the absolute truth.
- These views are based on my personal experience and observations.
- I would like to share my mental model of the subject with you.

# Non-Goals:

- I'm not going to show/teach you how to use Project Loom (a.k.a.: virtual threads, structured concurrency, scoped
  values).

# Goals:

- I'm going to convince you, that Project Loom is a Re:Revolution in Java and it worth to find time & invest it in
  learning/practicing these new techniques.

# Agenda:

1. [Java's Long Struggle with Concurrency Primitives](01-java-long-struggling-with-concurrency-primitives/README.md)
2. [Re:Revolution Irony](02-re-revolution-irony/README.md)
3. [Rob Pike Persona & Go's Influence](03-rob-pike-persona-and-go-influence/README.md)
4. [Async/Await Alternative](04-async-await-alternative/README.md)
5. [Project Loom State](05-project-loom-state/README.md)
6. [Project Loom Production Readiness](06-project-loom-production-readiness/README.md)
7. [Virtual Threads Bring Extra Pressure on Heap and GC](07-virtual-threads-brings-extra-pressure-on-heap-and-gc/README.md)
8. [Live Demos and Quizzes](08-live-demos-and-quizzes/README.md)

# Bold Statements:

- **Statement 1:** Project Loom will overtake every previously created concurrency techniques that were introduced for
  JVM in recent 20 years.
- **Statement 2:** Java is essentially nothing more than syntactic sugar over C.
- **Statement 3:** Python is just another form of syntactic sugar over C.

# Entrypoint:

```
rm -rf /tmp/concurrency-in-a-nutshell && cd ~ && pwd
git clone git@github.com:roman-dzhadan/concurrency-in-a-nutshell.git /tmp/concurrency-in-a-nutshell/
```