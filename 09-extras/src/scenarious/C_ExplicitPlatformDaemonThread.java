import utils.Log;
import utils.Workloads;

/**
 * Scenario: Explicit Platform Daemon Thread
 * Description: Demonstrates that newly spawned child platform daemon thread didn't have an opportunity to complete it's work.
 * Outcome: The JVM was terminated right after LAST non-daemon thread was terminated and was marked as {@link Thread.State#TERMINATED}.
 * JVM termination was finished with 0 exit code, and daemon threads were abruptly stopped at that moment.
 */
void main() {
    Log.print("Parent thread is running, starting a new child platform daemon thread...");
    Thread.ofPlatform().daemon(true).name("child-platform-daemon-thread").start(() -> Workloads.doCpuIntensiveWork(200_000L));
    Log.print("Parent thread has been completed.");
}