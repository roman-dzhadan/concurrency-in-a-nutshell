package utils;

import java.math.BigInteger;

public class Workloads {
    public static void doCpuIntensiveWork(long complexity) {
        Log.print("Starting a CPU-intensive computational work...");
        BigInteger result = doCpuIntensiveSilently(complexity);
        Log.print("CPU-intensive computation result consists of %d digits".formatted(result.toString().length()));
    }

    public static BigInteger doCpuIntensiveSilently(long complexity) {
        BigInteger result = BigInteger.ONE;
        for (long i = 1L; i <= complexity; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
