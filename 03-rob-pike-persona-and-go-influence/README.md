# Rob Pike Persona & Go's Influence

- What are **"concurrency"** and **"parallelism"**?
    * ["Concurrency is not Parallelism" - an insightful 30-minute talk by "Rob Pike" at "Waza 2012".](https://youtu.be/oV9rvDllKEg)

- Why is this talk worth watching?
    * **Fact A:** Go was designed at Google in 2007 by Robert Griesemer, Rob Pike, and Ken Thompson, and was publicly
      announced in November 2009. Rob Pike, one
      of Go's main inventors, is also the speaker of this recommended talk.
    * **Fact B:** Go is a relatively young programming language. It not only had the privilege to learn from its
      predecessors’ mistakes, but also the
      opportunity to design a language from scratch just after multicore CPUs became mainstream. (Spoiler: Java never
      had this advantage.)
    * **Fact C:** Go quickly earned a reputation as a language for concurrent programming due to its unique
      concurrency-oriented features offered by Go's
      programming language designers.
    * **Fact D:** Go provides first-class support for concurrency through **goroutines** and **channels**. Goroutines
      are lightweight threads managed by the Go
      runtime, allowing millions of concurrent tasks with minimal overhead. (Spoiler: Isn't **Project Loom** bell
      ringing?)

---

**[← Previous: Re:Revolution Irony](../02-re-revolution-irony/README.md)** | **[Next: Async/Await Alternative →](../04-async-await-alternative/README.md)**
