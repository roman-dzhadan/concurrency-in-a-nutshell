public class ConcurrencyDemo {
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.out.println("Usage: ConcurrencyDemo <platform|virtual> [threadCount]");
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
                PlatformThreadBasedJavaConcurrency.run(threadCount);
                break;
            case "virtual":
                if (threadCount <= 0) threadCount = 1_000_000;
                VirtualThreadBasedJavaConcurrency.run(threadCount);
                break;
            default:
                System.err.println("Unknown type: " + type + ". Use 'platform' or 'virtual'.");
        }
    }
}

