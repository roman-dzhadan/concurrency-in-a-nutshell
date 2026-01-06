void main(String[] args) throws InterruptedException {
    if (args.length < 1) {
        IO.println("Usage: ConcurrencyDemo <platform|virtual> [threadCount]");
        return;
    }

    String type = args[0].toLowerCase();
    int threadCount = -1;
    if (args.length >= 2) {
        try {
            threadCount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid thread count: " + args[1]);
            return;
        }
    }

    switch (type) {
        case "platform":
            if (threadCount <= 0) threadCount = 1_000;
            runWithBuilder(threadCount, false);
            break;
        case "virtual":
            if (threadCount <= 0) threadCount = 1_000_000;
            runWithBuilder(threadCount, true);
            break;
        default:
            System.err.println("Unknown type: " + type + ". Use 'platform' or 'virtual'.");
    }
}

private static void runWithBuilder(int threadCount, boolean virtual) throws InterruptedException {
    Thread[] threads = new Thread[threadCount];
    Thread.Builder builder = virtual ? Thread.ofVirtual() : Thread.ofPlatform();

    for (int i = 0; i < threadCount; i++) {
        final int id = i; // capture for the lambda
        Thread t = builder.unstarted(() -> {
            // Minimal workload: yield occasionally to simulate work
            if ((id & 1) == 0) Thread.yield();
        });
        t.start();
        threads[i] = t;
    }

    for (Thread t : threads) {
        if (t != null) t.join();
    }

    IO.println("Completed " + threadCount + " threads (" + (virtual ? "virtual" : "platform") + ").");
}
