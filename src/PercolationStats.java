import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONTIDANCE_CONST = 1.96;
    private final double[] thresholds;
    private final int size;

    private final double mean;
    private final double stddev;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException("invalid params");
        }
        size = n;
        thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            thresholds[i] = evaluateThreshold();
        }
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
    }

    private double evaluateThreshold() {
        Percolation percolation = new Percolation(size);
        boolean[][] sites = new boolean[size][size];
        while (!percolation.percolates()) {
            int rowToOpen = 1 + StdRandom.uniform(size);
            int colToOpen = 1 + StdRandom.uniform(size);
            while (sites[rowToOpen - 1][colToOpen - 1]) {
                rowToOpen = 1 + StdRandom.uniform(size);
                colToOpen = 1 + StdRandom.uniform(size);
            }
            percolation.open(rowToOpen, colToOpen);
            sites[rowToOpen - 1][colToOpen - 1] = true;
        }
        return ((double) percolation.numberOfOpenSites()) / (size * size);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLo() {
        return mean() - CONTIDANCE_CONST * stddev() / Math.sqrt(thresholds.length);
    }

    public double confidenceHi() {
        return mean() + CONTIDANCE_CONST * stddev() / Math.sqrt(thresholds.length);
    }

    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.printf("%-40s = %f\n", "mean", percolationStats.mean());
        StdOut.printf("%-40s = %f\n", "stddev", percolationStats.stddev());
        StdOut.printf("%-40s = [%f, %f]", "95% confidence interval", percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }
}
