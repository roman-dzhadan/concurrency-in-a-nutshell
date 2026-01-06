public class PlatformThreadBasedJavaConcurrency {

    public static void main(String[] args) throws InterruptedException {
        // Allow running this class directly: optional first arg = thread count
        int threadCount = 1_000;
        if (args.length >= 1) {
            try {
                threadCount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid thread count: " + args[0] + ", using default " + threadCount);
            }
        }
        run(threadCount);
    }

    public static void run(int threadCount) throws InterruptedException {
        System.out.println("[STARTED] Platform Thread Demo - PID = " + ProcessHandle.current().pid());
        System.out.println("Attach strace now: strace -f -p " + ProcessHandle.current().pid() + " -e trace=clone,clone3");
        System.out.println("Waiting 5 seconds for you to attach strace...");
        Thread.sleep(5000);

        System.out.println("\nCreating " + threadCount + " platform threads...");

        // Create and start platform threads (1:1 mapping to OS threads)
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            final int num = i + 1;
            String name = "Platform-Thread-" + num;
            threads[i] = new Thread(() -> doWork(name), name);

            // Each start() will trigger clone() system call for platform threads
            threads[i].start();

            // Small delay for visibility when attaching strace, but only for the first few threads
            if (num <= 10) {
                Thread.sleep(100);
            }

            // Reduce log spam: report every N threads depending on scale
            if (threadCount <= 1_000) {
                if (num % 50 == 0) System.out.println("Started " + num + " platform threads...");
            } else if (threadCount <= 100_000) {
                if (num % 1_000 == 0) System.out.println("Started " + num + " platform threads...");
            } else {
                if (num % 10_000 == 0) System.out.println("Started " + num + " platform threads...");
            }
        }

        System.out.println("\n>>> Started all threads - watch for clone() calls in strace <<<");

        // Wait for completion
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\nAll platform threads completed");
        System.out.println("[COMPLETED] Platform Thread Demo - PID = " + ProcessHandle.current().pid());
    }

    private static void doWork(String threadName) {
        // Get native thread ID (Linux TID)
        long tid = Thread.currentThread().threadId();

        // Keep per-thread logging minimal for large counts
        if (threadName.endsWith("-1") || threadName.endsWith("-2") || threadName.endsWith("-3")) {
            System.out.println("[" + threadName + "] Started - Thread ID = " + tid);
            System.out.println("[" + threadName + "] Doing work...");
        }

        try {
            Thread.sleep(1000);

            if (threadName.endsWith("-1") || threadName.endsWith("-2") || threadName.endsWith("-3")) {
                System.out.println("[" + threadName + "] Completed");
            }
        } catch (InterruptedException e) {
            System.err.println("[" + threadName + "] Interrupted");
        }
    }
}
