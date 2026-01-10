import utils.Log;
import utils.Workloads;

/**
 * Scenario: Interrupt Actually Does Not Interrupt Except Changing The Flag
 * Description: Demonstrates that interrupting a thread that is not in a blocking state does not interrupt its execution,
 * but only changes its interrupted status flag.
 * Outcome: The main thread is interrupted while performing CPU-intensive work, but it continues its execution,
 * and the interrupted status flag is set to true.
 */
void main() {

    Thread mainThread = Thread.currentThread();
    Log.print("My interrupted status at the beginning is '%b'".formatted(mainThread.isInterrupted()));

    Thread.ofPlatform().daemon(false).name("defered-main-thread-interruptor").start(() -> {
        Workloads.doCpuIntensiveWork(200_000L);
        Log.print("Going to send a polite interruption signal to the main thread...");
        mainThread.interrupt();
        Log.print("Polite interruption signal has been sent to the main thread.");
    });

    Workloads.doCpuIntensiveWork(300_000L);

    // Java's interruption model is COOPERATIVE, not preemptive.
    // Java will NEVER KILL a thread for you.
    // Anyone who has a reference to a thread can call interrupt() on it.
    // But it's up to the target thread to decide how to respond to that interruption signal.
    // Depending on implementation details, target thread may completely ignore that signal or obey it by pausing/stopping its work, etc.
    Log.print("My interrupted status in the end is '%b'".formatted(mainThread.isInterrupted()));
}