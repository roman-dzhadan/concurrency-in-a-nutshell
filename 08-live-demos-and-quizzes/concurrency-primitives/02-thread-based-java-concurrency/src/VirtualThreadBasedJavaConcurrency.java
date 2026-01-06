import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CountDownLatch;

public class VirtualThreadBasedJavaConcurrency {

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 1_000_000; // default
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
        System.out.println("[STARTED] Virtual Thread Demo - PID = " + ProcessHandle.current().pid());
        System.out.println("Attach strace now: strace -f -p " + ProcessHandle.current().pid() + " -e trace=clone,clone3");
        System.out.println("Waiting 5 seconds for you to attach strace...");
        Thread.sleep(5000);

        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("\nAvailable processors: " + processors);
        System.out.println("Carrier pool size: ~" + processors + " threads");

        System.out.println("\n>>> Watch strace: Carrier pool being created (expect ~" + processors + " clone() calls) <<<\n");

        System.out.println("Creating " + threadCount + " virtual threads...");
        System.out.println("\n>>> Watch strace: NO new clone() calls despite creating " + threadCount + " threads! <<<\n");

        AtomicInteger completedCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 1; i <= threadCount; i++) {
            final int threadNum = i;
            Thread.startVirtualThread(() -> doWork(threadNum, completedCount, latch, threadCount));

            // Print progress less frequently to avoid log spam
            if (threadCount <= 10_000) {
                if (i % 100 == 0) System.out.println("Created " + i + " virtual threads...");
            } else if (threadCount <= 1_000_000) {
                if (i % 10_000 == 0) System.out.println("Created " + i + " virtual threads...");
            } else {
                if (i % 100_000 == 0) System.out.println("Created " + i + " virtual threads...");
            }
        }

        System.out.println("\nAll " + threadCount + " virtual threads created successfully");
        System.out.println("Total OS threads (clone() calls): ~" + processors + " (carrier pool)");
        System.out.println("Waiting for threads to complete...\n");

        latch.await();

        System.out.println("\nAll virtual threads completed");
        System.out.println("Total virtual threads created: " + threadCount);
        System.out.println("Total completed: " + completedCount.get());
        System.out.println("[COMPLETED] Virtual Thread Demo - PID = " + ProcessHandle.current().pid());
    }

    private static void doWork(int threadNum, AtomicInteger completedCount, CountDownLatch latch, int threadCount) {
        Thread currentThread = Thread.currentThread();

        boolean isVirtual = currentThread.isVirtual();

        if (threadNum <= 5 || threadNum % Math.max(1, threadCount / 100) == 0 || threadNum > threadCount - 5) {
            System.out.println("[VirtualThread-" + threadNum + "] Started - Is Virtual: " + isVirtual + ", Thread ID: " + currentThread.threadId());
        }

        try {
            Thread.sleep(10);

            if (threadNum <= 5 || threadNum % Math.max(1, threadCount / 100) == 0 || threadNum > threadCount - 5) {
                System.out.println("[VirtualThread-" + threadNum + "] Completed");
            }

            completedCount.incrementAndGet();
        } catch (InterruptedException e) {
            System.err.println("[VirtualThread-" + threadNum + "] Interrupted");
        } finally {
            latch.countDown();
        }
    }
}
