#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include "../../pid_printer_runners/pid_printer_runner.h"

int main() {
    printf("[START] I'm the C program with PID %d and PPID %d\n", getpid(), getppid());
    const int pid_for_java = fork();
    if (pid_for_java == 0) {
        run_java_pid_printer();
    } else if (pid_for_java > 0) {
        const int pid_for_python = fork();
        if (pid_for_python == 0) {
            run_python_pid_printer();
        } else if (pid_for_python > 0) {
            printf(
                "[INFO] I'm the C program with PID %d and PPID %d and I have 2 children (Java with PID %d and Python with PID %d)\n",
                getpid(), getppid(), pid_for_java, pid_for_python
            );
            // defer parent process termination until all child processes won't be terminated
            int java_status, python_status;
            waitpid(pid_for_java, &java_status, 0); // (exit-code eq 0 exacted for java child process)
            waitpid(pid_for_python, &python_status, 0); // (exit-code eq 0 exacted for python child process)
            if (java_status == 0 && python_status == 0) {
                printf(
                    "[FINISH] I'm the C program with PID %d and PPID %d and I had 2 children (Java with PID %d and Python with PID %d)\n",
                    getpid(), getppid(), pid_for_java, pid_for_python
                );
            }
        } else {
            fprintf(stderr, "Failed to fork for Python process\n");
        }
    } else {
        fprintf(stderr, "Failed to fork for Java process\n");
    }
}
