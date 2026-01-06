#include <stdio.h>
#include <unistd.h>

int main() {
    printf("I'm the C program with PID %d and PPID %d\n", getpid(), getppid());
    printf("C program execution completed\n");
}
