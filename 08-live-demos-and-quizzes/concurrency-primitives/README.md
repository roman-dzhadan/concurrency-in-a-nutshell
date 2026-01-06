# Concurrency Primitives (short)

This folder contains small, hands-on demos that illustrate different concurrency models and how they map to OS activity.

Demos
- `01-process-based-java-concurrency/` — process-based (fork/exec)
- `02-thread-based-java-concurrency/` — thread-based (platform and virtual threads)
- `99-process-and-main-thread-compatability/` — misc compatibility examples

Quick run (using absolute /tmp paths and the single-file `java` launcher)

```bash
# Run platform threads (default 1000 if omitted)
java /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/concurrency-primitives/02-thread-based-java-concurrency/src/ThreadBasedJavaConcurrency.java platform 1000

# Run virtual threads (requires JDK 21+; default 1000000 if omitted)
java /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/concurrency-primitives/02-thread-based-java-concurrency/src/ThreadBasedJavaConcurrency.java virtual 1000000
```

Notes
- Virtual threads require a Loom-enabled JDK (JDK 21+ recommended).
- Use `strace -f -p <PID> -e trace=clone,clone3,futex` in another terminal to observe OS-level thread creation (platform threads produce many `clone()` calls; virtual threads only create carrier threads).

That's it — the subfolders contain more detailed READMEs and examples if you want to dive deeper.
