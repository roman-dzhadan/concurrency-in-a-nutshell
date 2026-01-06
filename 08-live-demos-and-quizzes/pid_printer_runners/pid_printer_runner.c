#include "pid_printer_runner.h"
#include <stdio.h>
#include <unistd.h>

extern char **environ;

void run_java_pid_printer(void) {
    char *command[] = {"java", "/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.java", NULL};
    char *bin_file = "/usr/bin/java";
    if (execve(bin_file, command, environ) == -1) {
        fprintf(stderr, "Can't execute %s\n", bin_file);
    }
}

void run_python_pid_printer(void) {
    char *command[] = {"python3", "/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.py", NULL};
    char *bin_file ="/usr/bin/python3";
    if (execve(bin_file, command, environ) == -1) {
        fprintf(stderr, "Can't execute %s\n", bin_file);
    }
}
