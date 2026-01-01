#include <stdio.h>
#include <unistd.h>
#include "../../pid_printer_runners/pid_printer_runner.h"

int main() {
    printf("I'm the C program with PID %d and PPID %d\n", getpid(), getppid());

    run_java_pid_printer();

    printf("C program execution completed\n");
}
