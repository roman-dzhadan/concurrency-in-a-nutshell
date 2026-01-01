# Disclaimer:

- Everything I say and show today reflects my personal opinions.
- I do not claim to hold the absolute truth.
- These views are based on my personal experience and observations.
- I would like to share my mental model of the subject with you.

# Entrypoint:

```
rm -rf /tmp/concurrency-in-a-nutshell && cd ~ && pwd
git clone git@github.com:roman-dzhadan/concurrency-in-a-nutshell.git /tmp/concurrency-in-a-nutshell/
```

# Bold Statements:

- **Statement 1:** Java is essentially nothing more than syntactic sugar over C.
- **Statement 2:** Python is just another form of syntactic sugar over C.
- **Statement 3:** Reactive programming in Java is a spectacularly mesmerizing piece of shit.
- **P.S.:** Feel free to quote me on these three statements.

# Strict Boundaries:

- What is the **"concurrency"** and what is the **"parallelism"**?
    * ["Concurrency is not Parallelism" - an insightful 30-mins long talk by "Rob Pike" at "Waza 2012".](https://youtu.be/oV9rvDllKEg)

- Why does this talk worth to be watched?
    * **Fact A:** Go was designed at Google in 2007 by Robert Griesemer, Rob Pike, and Ken Thompson, and was publicly announced in November 2009. Rob Pike was
      one of the main inventors of Go, and he is the speaker of the talk I'm recommending you to watch.
    * **Fact B:** Go is a relatively young programming language. Go not only had a privilege to learn lessons and do not repeat most of its predecessors design
      mistakes, but also had a chance to design a language from a scratch right after multicore CPU(s) became mainstreamed. **Spoiler alert:** Java never had
      such opportunity.
    * **Fact C:** Go rapidly gained the glory of a language for concurrent programming due to its unique concurrency-related features offered by Go's
      programming language designers.
    * **Fact D:** Go has a first-class support for concurrency through **goroutines** and **channels**. Goroutines are lightweight threads managed by the Go
      runtime, allowing millions of concurrent tasks with minimal overhead.

# Introduction:

```bash
nvim /tmp/concurrency-in-a-nutshell/pid_printers/pid_printer.c

cc -o /tmp/concurrency-in-a-nutshell/pid_printers/pid_printer.bin \
      /tmp/concurrency-in-a-nutshell/pid_printers/pid_printer.c

/tmp/concurrency-in-a-nutshell/pid_printers/pid_printer.bin

nvim /tmp/concurrency-in-a-nutshell/pid_printers/pid_printer.java
java /tmp/concurrency-in-a-nutshell/pid_printers/pid_printer.java

nvim /tmp/concurrency-in-a-nutshell/pid_printers/pid_printer.py
python /tmp/concurrency-in-a-nutshell/pid_printers/pid_printer.py

ls -l /proc/XXXX/exe
```

# Exec Quiz:

```bash
nvim /tmp/concurrency-in-a-nutshell/quizes/quiz-exec/exec_quiz.c
nvim /tmp/concurrency-in-a-nutshell/pid_printer_runners/pid_printer_runner.h
nvim /tmp/concurrency-in-a-nutshell/pid_printer_runners/pid_printer_runner.c

cc -o /tmp/concurrency-in-a-nutshell/quizes/quiz-exec/exec_quiz.bin \
   /tmp/concurrency-in-a-nutshell/pid_printer_runners/pid_printer_runner.c \
   /tmp/concurrency-in-a-nutshell/quizes/quiz-exec/exec_quiz.c
   
/tmp/concurrency-in-a-nutshell/quizes/quiz-exec/exec_quiz.bin

nvim /tmp/concurrency-in-a-nutshell/quizes/quiz-exec/exec_explanation.c

cc -o /tmp/concurrency-in-a-nutshell/quizes/quiz-exec/exec_explanation.bin \
   /tmp/concurrency-in-a-nutshell/pid_printer_runners/pid_printer_runner.c \
   /tmp/concurrency-in-a-nutshell/quizes/quiz-exec/exec_explanation.c

/tmp/concurrency-in-a-nutshell/quizes/quiz-exec/exec_explanation.bin
```

# Fork Quiz:

```bash
nvim /tmp/concurrency-in-a-nutshell/quizes/quiz-fork/fork_quiz.c

cc -o /tmp/concurrency-in-a-nutshell/quizes/quiz-fork/fork_quiz.bin \
   /tmp/concurrency-in-a-nutshell/quizes/quiz-fork/fork_quiz.c
   
/tmp/concurrency-in-a-nutshell/quizes/quiz-fork/fork_quiz.bin

nvim /tmp/concurrency-in-a-nutshell/quizes/quiz-fork/fork_explanation.c

cc -o /tmp/concurrency-in-a-nutshell/quizes/quiz-fork/fork_explanation.bin \
   /tmp/concurrency-in-a-nutshell/quizes/quiz-fork/fork_explanation.c
   
/tmp/concurrency-in-a-nutshell/quizes/quiz-fork/fork_explanation.bin
```

# Fork & Exec Quiz:

```bash
nvim /tmp/concurrency-in-a-nutshell/quizes/quiz-fork-n-exec/fork_n_exec_quiz.c

cc -o /tmp/concurrency-in-a-nutshell/quizes/quiz-fork-n-exec/fork_n_exec_quiz.bin \
   /tmp/concurrency-in-a-nutshell/pid_printer_runners/pid_printer_runner.c \
   /tmp/concurrency-in-a-nutshell/quizes/quiz-fork-n-exec/fork_n_exec_quiz.c

/tmp/concurrency-in-a-nutshell/quizes/quiz-fork-n-exec/fork_n_exec_quiz.bin

nvim /tmp/concurrency-in-a-nutshell/quizes/quiz-fork-n-exec/fork_n_exec_explanation.md
```
