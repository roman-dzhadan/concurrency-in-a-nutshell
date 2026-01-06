#include <stdio.h>
#include <unistd.h>

//   behavior: This C program demonstrates the use of the fork() system call to create a new process
//             When fork() is called, it creates a child process that is a duplicate of the parent process
//             The return value of fork() helps to distinguish between the parent and child processes:
//               - it returns 0 to the child process
//               - a positive value (the child's PID) to the parent process,
//               - and -1 if the fork fails
//             The program prints messages indicating whether it is running in the parent or child process, along with their respective PIDs and PPIDs
//    verdict: fork() system call was called ONCE, and result was return TWICE (allowing concurrent execution of parent and child processes)
int main() {
    printf("[START] I'm am the process with PID %d and PPID %d\n", getpid(), getppid());
    const int result = fork();
    if (result == 0) {
        printf("[INFO] I'm a new child process with PID %d and PPID %d\n", getpid(), getppid());
    } else if (result > 0) {
        printf(
            "[INFO] I've just became a parent, but I'm still the old process with PID %d and PPID %d\n",
            getpid(), getppid()
        );
    } else {
        printf(
            "[ERROR] Unfortunately, I haven't became a parent due to %d, but I'm still the old process with PID %d and PPID %d\n",
            result, getpid(), getppid()
        );
    }
    printf("[FINISH] I'm am the process with PID %d and PPID %d\n", getpid(), getppid());
}
