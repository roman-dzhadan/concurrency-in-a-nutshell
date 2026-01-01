#include <stdio.h>

// usage: cc -o /tmp/read_above_stack.bin read_above_stack.c && /tmp/read_above_stack.bin alfa beta gamma
// outcomes: reads memory above the current stack frame, potentially exposing sensitive data, until a segmentation fault occurs (upward memory access violation)
void main() {
    char starting_point = '.';
    char *ptr = &starting_point;
    while (1) {
        putchar(*ptr == '\0' ? '\n' : *ptr);
        fflush(stdout);
        ptr++;
    }
}