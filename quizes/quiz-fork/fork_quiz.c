#include <stdio.h>
#include <unistd.h>

int main() {
    printf("start\n");
    const int result = fork();
    if (result == 0) {
        printf("equal-zero\n");
    } else if (result > 0) {
        printf("greater-than-zero\n");
    } else {
        printf("less-than-zero\n");
    }
    printf("finish\n");
}
