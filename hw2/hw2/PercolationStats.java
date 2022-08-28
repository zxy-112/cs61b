package hw2;

import java.util.ArrayList;

import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {

    private double mean;
    private double stddev;
    private double confidenceLow;
    private double confidenceHigh;

    public PercolationStats(int n, int t, PercolationFactory pf) {
        int sitesNumber = n * n;
        double[] percentages = new double[t];
        for (int m = 0; m < t; m = m + 1) {
            Percolation percolation = pf.make(n);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(0, n), StdRandom.uniform(0, n));
            }
            percentages[m] = percolation.numberOfOpenSites() / (double) sitesNumber;
        }
        mean = StdStats.mean(percentages);
        stddev = StdStats.stddev(percentages);
        confidenceLow = mean - 1.96 * stddev /  Math.sqrt(t);
        confidenceHigh = mean + 1.96 * stddev / Math.sqrt(t);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return confidenceLow;
    }

    public double confidenceHigh() {
        return confidenceHigh;
    }
}
