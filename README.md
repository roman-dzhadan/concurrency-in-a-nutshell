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

- Statement 1: Java is essentially syntactic sugar over C
- Statement 2: Python is another form of syntactic sugar over C
- Statement 3: Reactive programming in Java is a spectacularly mesmerizing piece of shit

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
