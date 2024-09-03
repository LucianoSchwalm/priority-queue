public class LinearCongruentialGenerator {
    private static final long a = 1664525;
    private static final long c = 1013904223;
    private static final long M = 4294967296L; // 2^32
    private long previous = 1; // Semente inicial

    public double NextRandom() {
        previous = (a * previous + c) % M;
        return (double) previous / M;
    }
}