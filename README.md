# Bold Statements:

- Statement 1: Java is essentially syntactic sugar over C
- Statement 2: Python is another form of syntactic sugar over C

# Introduction:

```bash
cd ~/projects/concurrency-in-a-nutshell/pid_printers/
nvim pid_printer.c
cc -o /tmp/pid_printer.bin pid_printer.c && /tmp/pid_printer.bin

nvim pid_printer.java
java pid_printer.java

nvim pid_printer.py
python pid_printer.py

ls -l /proc/XXXX/exe
```

# Exec Quiz:

```bash
cd ~/projects/concurrency-in-a-nutshell/quizes/quiz-exec/

nvim exec_quiz.c
nvim ../../pid_printer_runners/pid_printer_runner.h
nvim ../../pid_printer_runners/pid_printer_runner.c

cc -o /tmp/exec_quiz.bin ../../pid_printer_runners/pid_printer_runner.c exec_quiz.c && /tmp/exec_quiz.bin

nvim exec_explanation.c
cc -o /tmp/exec_explanation.bin ../../pid_printer_runners/pid_printer_runner.c exec_explanation.c && /tmp/exec_explanation.bin
```

# Fork Quiz:

```bash
cd ~/projects/concurrency-in-a-nutshell/src/quizes/quiz-fork/
nvim fork_quiz.c
cc -o /tmp/fork_quiz.bin fork_quiz.c && /tmp/fork_quiz.bin

nvim fork_explanation.c
cc -o /tmp/fork_explanation.bin fork_explanation.c && /tmp/fork_explanation.bin
```

# Fork & Exec Quiz:

```bash
cd ~/projects/concurrency-in-a-nutshell/src/quizes/quiz-fork-n-exec/
nvim fork_n_exec_quiz.c
cc -o /tmp/fork_n_exec_quiz.bin ../../pid_printer_runners/pid_printer_runner.c fork_n_exec_quiz.c && /tmp/fork_n_exec_quiz.bin

nvim fork_n_exec_explanation.md
```