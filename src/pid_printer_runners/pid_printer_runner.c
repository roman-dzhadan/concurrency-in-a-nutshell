#include "pid_printer_runner.h"
#include <stdio.h>
#include <unistd.h>

void run_java_pid_printer(void) {
    char *command[] = {"java", "../../pid_printers/pid_printer.java", nullptr};
    char *bin_file = command[0];
    if (execvp(bin_file, command) == -1) {
        fprintf(stderr, "Can't execute %s\n", bin_file);
    }
}

void run_python_pid_printer(void) {
    char *command[] = {"python", "../../pid_printers/pid_printer.py", nullptr};
    char *bin_file = command[0];
    if (execvp(bin_file, command) == -1) {
        fprintf(stderr, "Can't execute %s\n", bin_file);
    }
}
