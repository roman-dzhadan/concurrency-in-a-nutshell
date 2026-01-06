import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

void main() throws InterruptedException {
    System.out.println("[STARTED] Virtual Thread Demo - PID = " + ProcessHandle.current().pid());
    System.out.println("Attach strace now: strace -f -p " + ProcessHandle.current().pid() + " -e trace=clone,clone3");
    System.out.println("Waiting 5 seconds for you to attach strace...");
    Thread.sleep(5000);

    int processors = Runtime.getRuntime().availableProcessors();
    System.out.println("\nAvailable processors: " + processors);
    System.out.println("Carrier pool size: ~" + processors + " threads");

    System.out.println("\n>>> Watch strace: Carrier pool being created (expect ~" + processors + " clone() calls) <<<\n");

    // Create a large number of virtual threads to demonstrate scalability
    int threadCount = 1000;
    System.out.println("Creating " + threadCount + " virtual threads...");
    System.out.println("\n>>> Watch strace: NO new clone() calls despite creating " + threadCount + " threads! <<<\n");

    List<Thread> threads = new ArrayList<>();
    AtomicInteger completedCount = new AtomicInteger(0);

    // Create virtual threads using Thread.startVirtualThread()
    // IMPORTANT: Each virtual thread creation does NOT trigger clone()!
    for (int i = 1; i <= threadCount; i++) {
        final int threadNum = i;
        Thread vThread = Thread.startVirtualThread(() -> doWork(threadNum, completedCount));
        threads.add(vThread);

        // Print progress every 100 threads
        if (i % 100 == 0) {
            System.out.println("Created " + i + " virtual threads... (still no new clone() calls in strace)");
        }
    }

    System.out.println("\nAll " + threadCount + " virtual threads created successfully");
    System.out.println("Total OS threads (clone() calls): ~" + processors + " (carrier pool)");
    System.out.println("Waiting for threads to complete...\n");

    // Wait for all virtual threads to complete
    for (Thread thread : threads) {
        thread.join();
    }

    System.out.println("\nAll virtual threads completed");
    System.out.println("Total virtual threads created: " + threadCount);
    System.out.println("Total completed: " + completedCount.get());
    System.out.println("[COMPLETED] Virtual Thread Demo - PID = " + ProcessHandle.current().pid());
}

void doWork(int threadNum, AtomicInteger completedCount) {
    Thread currentThread = Thread.currentThread();

    // Check if this is a virtual thread
    boolean isVirtual = currentThread.isVirtual();

    // Print info for first few threads and last few
    if (threadNum <= 5 || threadNum % 250 == 0 || threadNum > 995) {
        System.out.println("[VirtualThread-" + threadNum + "] Started - Is Virtual: " + isVirtual +
                         ", Thread ID: " + currentThread.threadId());
    }

    try {
        // Simulate some I/O work
        // When virtual thread blocks here, it UNMOUNTS from carrier thread
        // The carrier thread is freed to run other virtual threads
        // NO clone() system call is made!
        Thread.sleep(10);

        if (threadNum <= 5 || threadNum % 250 == 0 || threadNum > 995) {
            System.out.println("[VirtualThread-" + threadNum + "] Completed");
        }

        completedCount.incrementAndGet();
    } catch (InterruptedException e) {
        System.err.println("[VirtualThread-" + threadNum + "] Interrupted");
    }
}

