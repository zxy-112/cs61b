package hw4.puzzle;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board implements WorldState {

    private final int size;
    private int[][] tiles;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        size = tiles.length;
        this.tiles = exchangedTiles(0, 0, 0, 0);
    }

    public int size() {
        return size;
    }

    public int tileAt(int i, int j) {
        if (!checkBound(i, j)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return tiles[i][j];
    }

    public Iterable<WorldState> neighbors() {
        List<WorldState> lis = new LinkedList<>();
        int zeroIndexX = 0, zeroIndexY = 0;
        for (int m = 0; m < size; m += 1) {
            for (int n = 0; n < size; n += 1) {
                if (tiles[m][n] == 0) {
                    zeroIndexX = m;
                    zeroIndexY = n;
                    break;
                }
            }
        }
        if (checkBound(zeroIndexX - 1, zeroIndexY)) {
            lis.add(new Board(exchangedTiles(zeroIndexX - 1, zeroIndexY, zeroIndexX, zeroIndexY)));
        }
        if (checkBound(zeroIndexX + 1, zeroIndexY)) {
            lis.add(new Board(exchangedTiles(zeroIndexX + 1, zeroIndexY, zeroIndexX, zeroIndexY)));
        }
        if (checkBound(zeroIndexX, zeroIndexY - 1)) {
            lis.add(new Board(exchangedTiles(zeroIndexX, zeroIndexY - 1, zeroIndexX, zeroIndexY)));
        }
        if (checkBound(zeroIndexX, zeroIndexY + 1)) {
            lis.add(new Board(exchangedTiles(zeroIndexX, zeroIndexY + 1, zeroIndexX, zeroIndexY)));
        }
        return lis;
    }

    private boolean checkBound(int x, int y) {
        return (0 <= x && x < size && 0 <= y && y < size);
    }

    private int[][] exchangedTiles(int x, int y, int x2, int y2) {
        int [][] newTiles = new int[size][size];
        for (int m = 0; m < size; m += 1) {
            System.arraycopy(tiles[m], 0, newTiles[m], 0, size);
        }
        newTiles[x][y] = tiles[x2][y2];
        newTiles[x2][y2] = tiles[x][y];
        return newTiles;
    }

    public int hamming() {
        int xIndex, yIndex;
        int res = 0;
        for (int m = 1; m < size * size; m += 1) {
            xIndex = (m - 1) / size;
            yIndex = (m - 1) % size;
            res += (tiles[xIndex][yIndex] == m ? 0 : 1);
        }
        return res;
    }

    public int manhattan() {
        int actualX, actualY;
        int expect;
        int res = 0;
        for (int m = 0; m < size; m += 1) {
            for (int n = 0; n < size; n += 1) {
                expect = m * size + n + 1;
                if (tiles[m][n] != expect && tiles[m][n] != 0) {
                    actualX = (tiles[m][n] - 1) / size;
                    actualY = (tiles[m][n] - 1) % size;
                    res += Math.abs(actualX - m) + Math.abs(actualY - n);
                }
            }
        }
        return res;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || o.getClass() != this.getClass()) {
            return false;
        } else {
            Board other = (Board) o;
            return Arrays.deepEquals(this.tiles, other.tiles);
        }
    }

    @Override
    public int hashCode() {
        int res = 0;
        for (int m = 0; m < size; m += 1) {
            for (int n = 0; n < size; n += 1) {
                res += res * 31 + tiles[m][n];
            }
        }
        return res;
    }

    /** Returns the string representation of the board.
     * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
