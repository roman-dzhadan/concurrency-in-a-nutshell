#include <stdio.h>

// usage: gcc -o /tmp/parse_program_arguments.bin parse_program_arguments.c && /tmp/parse_program_arguments.bin alfa beta gamma
void main(int argc, char *argv[]) {
    for (int i = 0; i < argc; i++) {
        printf("Argument %d: %s\n", i, argv[i]);
    }
}