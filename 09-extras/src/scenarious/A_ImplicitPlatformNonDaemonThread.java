import utils.Log;
import utils.Workloads;

/**
 * Scenario: Implicit Platform Non-Daemon Thread
 * Description: Demonstrates that newly spawned child platform thread is a NON-DAEMON thread by default, and it can outlive the parent thread (e.g., main thread).
 * Outcome: The JVM continues running until AT LEAST ONE non-daemon thread still has a non-{@link java.lang.Thread.State#TERMINATED} state.
 * It doesn't prevent parent's thread (e.g., main thread) from termination and from being marked as {@link java.lang.Thread.State#TERMINATED}.
 * It also demonstrates that parent thread termination does NOT affect the child thread's execution.
 */
void main() {
    Log.print("Parent thread is running, starting a new child platform non-daemon thread...");
    Thread.ofPlatform().name("child-platform-non-daemon-thread").start(() -> Workloads.doCpuIntensiveWork(200_000L));
    Log.print("Parent thread has been completed.");
}