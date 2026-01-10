import utils.Log;
import utils.Workloads;

/**
 * Scenario: Actual Interruption Possible If Was Implemented
 * Description: This scenario demonstrates that a thread can be interrupted while performing CPU-intensive work.
 * Outcome: The main thread waits for an interrupt signal while performing CPU-intensive work, and upon receiving the signal, it acknowledges the interruption.
 */
void main() {

    Thread mainThread = Thread.currentThread();

    Thread.ofPlatform().daemon(false).name("defered-main-thread-interruptor").start(() -> {
        Workloads.doCpuIntensiveWork(300_000L);
        Log.print("Going to send a polite interruption signal to the main thread...");
        mainThread.interrupt();
        Log.print("Polite interruption signal has been sent to the main thread.");
    });

    Log.print("My thread reference was shared with my child thread, that means, my child thread may ask me for interruption at any time, I will be watching closely.");

    // do CPU-intensive work in a small chunks and periodically check if main thread was not interrupted by the child thread
    // (or by anyone else who has a reference to it)
    while (!mainThread.isInterrupted()) {
        Workloads.doCpuIntensiveSilently(10_000L);
    }

    Log.print("I've detected that I was asked to be interrupted by my child thread. I've stopped doing my work as soon as possible and I'm going to exit now.");
}