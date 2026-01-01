#include <stdio.h>

// usage: cc -o /tmp/read_below_stack.bin read_below_stack.c && /tmp/read_below_stack.bin alfa beta gamma
// outcomes: reads memory below the current stack frame, potentially exposing sensitive data, until a segmentation fault occurs (downward memory access violation)
void main() {
    char starting_point = '.';
    char *ptr = &starting_point;
    while (1) {
        putchar(*ptr == '\0' ? '\n' : *ptr);
        fflush(stdout);
        ptr--;
    }
}