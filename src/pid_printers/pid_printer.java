void main() {
    IO.println("I'm the Java program with PID %d and PPID %d".formatted(
            ProcessHandle.current().pid(), ProcessHandle.current().parent().orElseThrow().pid())
    );
    IO.println("Java program execution completed");
}