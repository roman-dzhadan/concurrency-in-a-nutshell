# Live Demos and Quizzes

## Introduction

```bash
nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.c

gcc -o /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.bin \
      /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.c

/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.bin

nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.java
java /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.java

nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.py
python3 /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.py

ls -l /proc/XXXX/exe
```

# Exec Quiz:

```bash
nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-exec/exec_quiz.c
nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printer_runners/pid_printer_runner.h
nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printer_runners/pid_printer_runner.c

gcc -o /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-exec/exec_quiz.bin \
   /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printer_runners/pid_printer_runner.c \
   /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-exec/exec_quiz.c
   
/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-exec/exec_quiz.bin

nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-exec/exec_explanation.c

gcc -o /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-exec/exec_explanation.bin \
   /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printer_runners/pid_printer_runner.c \
   /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-exec/exec_explanation.c

/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-exec/exec_explanation.bin
```

# Fork Quiz:

```bash
nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork/fork_quiz.c

gcc -o /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork/fork_quiz.bin \
   /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork/fork_quiz.c
   
/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork/fork_quiz.bin

nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork/fork_explanation.c

gcc -o /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork/fork_explanation.bin \
   /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork/fork_explanation.c
   
/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork/fork_explanation.bin
```

# Fork & Exec Quiz:

```bash
nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork-n-exec/fork_n_exec_quiz.c

gcc -o /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork-n-exec/fork_n_exec_quiz.bin \
   /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printer_runners/pid_printer_runner.c \
   /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork-n-exec/fork_n_exec_quiz.c

/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork-n-exec/fork_n_exec_quiz.bin

nvim /tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/quizes/quiz-fork-n-exec/fork_n_exec_explanation.md
```

---

**[← Previous: Virtual Threads Bring Extra Pressure on Heap and GC](../07-virtual-threads-brings-extra-pressure-on-heap-and-gc/README.md)**

