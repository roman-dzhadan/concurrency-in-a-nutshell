import utils.Log;
import utils.Workloads;

/**
 * Scenario: Join Platform Non-Daemon Thread
 * Description: This scenario demonstrates the behavior of joining a platform non-daemon thread from the parent thread.
 * Outcome: The parent thread starts a platform non-daemon thread, waits for its completion using join(), and then completes its own execution.
 */
void main() throws InterruptedException {
    Log.print("Parent thread is running, starting a new child platform non-daemon thread...");
    Thread childThread = Thread.ofPlatform().daemon(false).name("child-platform-non-daemon-thread").start(() -> Workloads.doCpuIntensiveWork(200_000L));
    childThread.join();
    Log.print("Parent thread has been completed.");
}