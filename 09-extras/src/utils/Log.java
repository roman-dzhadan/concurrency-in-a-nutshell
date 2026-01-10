package utils;

public class Log {
    public static void print(String message) {
        Thread thisThread = Thread.currentThread();
        thisThread.getState();
        IO.println("[%s][v:%s][d:%s]: %s".formatted(thisThread.getName(), thisThread.isVirtual(), thisThread.isDaemon(), message));
    }
}
