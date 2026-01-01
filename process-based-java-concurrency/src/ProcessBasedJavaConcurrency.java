import java.io.IOException;

public class ProcessBasedJavaConcurrency {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("[STARTED] Wrapper Process PID: " + ProcessHandle.current().pid());

        var cProcess = new ProcessBuilder("/tmp/pid_printer.bin").inheritIO().start();
        var javaProcess = new ProcessBuilder("java", "/home/roman/projects/concurrency-in-a-nutshell/src/pid_printers/pid_printer.java").inheritIO().start();
        var pythonProcess = new ProcessBuilder("python", "/home/roman/projects/concurrency-in-a-nutshell/src/pid_printers/pid_printer.py").inheritIO().start();

        cProcess.waitFor();
        javaProcess.waitFor();
        pythonProcess.waitFor();

        System.out.println("[COMPLETED] Wrapper Process PID: " + ProcessHandle.current().pid());
    }
}
