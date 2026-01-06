import java.io.IOException;

void main() throws InterruptedException {
    System.out.println("[STARTED] Platform Thread Demo - PID = " + ProcessHandle.current().pid());
    System.out.println("Attach strace now: strace -f -p " + ProcessHandle.current().pid() + " -e trace=clone,clone3");
    System.out.println("Waiting 5 seconds for you to attach strace...");
    Thread.sleep(5000);

    System.out.println("\nCreating 3 platform threads...");

    // Create platform threads (traditional Java threads - 1:1 mapping to OS threads)
    Thread thread1 = new Thread(() -> doWork("Platform Thread-1"), "Platform-Thread-1");
    Thread thread2 = new Thread(() -> doWork("Platform Thread-2"), "Platform-Thread-2");
    Thread thread3 = new Thread(() -> doWork("Platform Thread-3"), "Platform-Thread-3");

    // Start threads - each start() will trigger clone() system call
    System.out.println("\n>>> Starting threads (watch for clone() system calls in strace) <<<");
    thread1.start();
    Thread.sleep(100); // Small delay to see individual clone() calls clearly
    thread2.start();
    Thread.sleep(100);
    thread3.start();

    // Wait for completion
    thread1.join();
    thread2.join();
    thread3.join();

    System.out.println("\nAll platform threads completed");
    System.out.println("[COMPLETED] Platform Thread Demo - PID = " + ProcessHandle.current().pid());
}

void doWork(String threadName) {
    // Get native thread ID (Linux TID)
    long tid = Thread.currentThread().threadId();
    System.out.println("[" + threadName + "] Started - Thread ID = " + tid);

    try {
        // Simulate some work
        System.out.println("[" + threadName + "] Doing work...");
        Thread.sleep(1000);

        System.out.println("[" + threadName + "] Completed");
    } catch (InterruptedException e) {
        System.err.println("[" + threadName + "] Interrupted");
    }
}

