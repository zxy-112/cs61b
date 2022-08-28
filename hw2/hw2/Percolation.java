package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;

public class Percolation {

    /** the sites that represents the real sites.*/
    private final boolean[][] sites;
    private int numberOfOpenSites;
    private final WeightedQuickUnionUF unions;
    private final ArrayList<Integer> openedUpper;
    private final ArrayList<Integer> openedBottom;

    /**
     * maker a percolation with size of sites being n.
     * @param n the size of sites.
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        sites = new boolean[n][n];
        for (int i = 0; i < n; i = i + 1) {
            for (int j = 0; j < n; j = j + 1) {
                sites[i][j] = false;
            }
        }
        numberOfOpenSites = 0;
        unions = new WeightedQuickUnionUF(n * n);
        openedUpper = new ArrayList<>(n);
        openedBottom = new ArrayList<>(n);
    }

    /**
     * open the corresponding site.
     * @param row the row index.
     * @param col the col index.
     */
    public void open(int row, int col) {
        if (checkIndex(row) && checkIndex(col)) {
            if (!sites[row][col]) {
                numberOfOpenSites = numberOfOpenSites + 1;

                int currentId = id(row, col);
                if (checkIndex(row-1)) {
                    if (isOpen(row-1, col)) {
                        unions.union(id(row-1, col), currentId);
                    }
                }
                if (checkIndex(row+1)) {
                    if (isOpen(row+1, col)) {
                        unions.union(id(row+1, col), currentId);
                    }
                }
                if (checkIndex(col-1)) {
                    if (isOpen(row, col-1)) {
                        unions.union(id(row, col-1), currentId);
                    }
                }
                if (checkIndex(col+1)) {
                    if (isOpen(row, col+1)) {
                        unions.union(id(row, col+1), currentId);
                    }
                }
                if (row == 0) {
                    openedUpper.add(currentId);
                }
                if (row == sites.length - 1) {
                    openedBottom.add(currentId);
                }

            }
            sites[row][col] = true;
        } else {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    /**
     * weather the sites[row][col] is open.
     * @param row the row num.
     * @param col the col index.
     * @return true or false.
     */
    public boolean isOpen(int row, int col) {
        if (checkIndex(row) && checkIndex(col)) {
            return sites[row][col];
        } else {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    public boolean isFull(int row, int col) {
        if (checkIndex(row) && checkIndex(col)) {
            for (int m: openedUpper) {
                if (unions.connected(id(0, m), id(row, col))) {
                    return true;
                }
            }
            return false;
        } else {
            throw new java.lang.IndexOutOfBoundsException();
        }
    }

    int id(int row, int col) {
        return row * sites.length + col;
    }

    /**
     * return if the index is not negative and less than n
     * @param index the index to be checked.
     * @return true or false.
     */
    boolean checkIndex(int index) {
        return index >= 0 && index < sites.length;
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        for (int m: openedBottom) {
            if (isFull(m/sites.length, m%sites.length)) {
                return true;
            }
        }
        return false;
    }
}
