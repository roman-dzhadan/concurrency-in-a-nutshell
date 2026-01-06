void main() throws IOException, InterruptedException {
    IO.println("[STARTED] JAVA WRAPPER WITH PID = " + ProcessHandle.current().pid());

    var cProcess = new ProcessBuilder("/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.bin").inheritIO().start();
    var javaProcess = new ProcessBuilder("java", "/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.java").inheritIO().start();
    var pythonProcess = new ProcessBuilder("python3", "/tmp/concurrency-in-a-nutshell/08-live-demos-and-quizzes/pid_printers/pid_printer.py").inheritIO().start();

    cProcess.waitFor();
    javaProcess.waitFor();
    pythonProcess.waitFor();

    IO.println("[COMPLETED] JAVA WRAPPER WITH PID = " + ProcessHandle.current().pid());
}
