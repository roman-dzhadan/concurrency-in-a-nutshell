// https://man7.org/linux/man-pages/man3/printf.3.html
// https://man7.org/linux/man-pages/man2/getpid.2.html
// https://man7.org/linux/man-pages/man3/getppid.3p.html
#include <stdio.h>
#include <unistd.h>
#include "../../pid_printer_runners/pid_printer_runner.h"

// behavior: The C program prints its PID, then the Java/Python program prints the same PID, and the final C print statement is never reached
//           Metamorphosis occurs here: the process stays the same, but the program being executed changes from the C program to the Java/Python program
//           The virtual memory addresses of the C program were completely wiped-out and re-initialized by the Java/Python program
//  verdict: execvp(...) system call was called ONCE and result was NEVER returned, because the Java/Python program took over the process
int main() {
    printf("I'm the C program under execution with PID %d and PPID %d\n", getpid(), getppid());

    run_java_pid_printer();
    run_python_pid_printer();

    printf("C program execution completed\n");
}
