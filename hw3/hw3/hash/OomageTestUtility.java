package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int N = oomages.size();
        double lowerBound = N / 50.;
        double upperBound = N / 2.5;
        int[] counts = new int[M];
        int index;
        for (Oomage oomage: oomages) {
            index = (oomage.hashCode() & 0x7FFFFFFF) % M;
            counts[index] += 1;
        }
        for (int m = 0; m < M; m += 1) {
            if (counts[m] < lowerBound || counts[m] > upperBound){
                return false;
            }
        }
        return true;
    }
}
