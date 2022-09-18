import edu.princeton.cs.introcs.In;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CharMap {
    private final char[][] chars;
    private final int height;
    private final int width;

    public CharMap(String boardFilePath) {
        chars = readChars(boardFilePath);
        height = chars.length;
        width = chars[0].length;
    }

    public CharMap(char[][] chars) {
        this.chars = chars;
        height = chars.length;
        width = chars[0].length;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int size() {
        return width * height;
    }

    public char get(int row, int col) {
        return chars[row][col];
    }

    public char get(int index) {
        return chars[row(index)][col(index)];
    }

    public int d2Tod1(int row, int col) {
        return row * width + col;
    }

    public int row(int index) {
        return index / width;
    }

    public int col(int index) {
        return index % width;
    }

    public Iterable<Integer> neighbors(int row, int col) {
        List<Integer> res = new LinkedList<>();
        if (inBound(row - 1, col)) {
            res.add(d2Tod1(row - 1, col));
        }
        if (inBound(row + 1, col)) {
            res.add(d2Tod1(row + 1, col));
        }
        if (inBound(row, col - 1)) {
            res.add(d2Tod1(row, col - 1));
        }
        if (inBound(row, col + 1)) {
            res.add(d2Tod1(row, col + 1));
        }
        if (inBound(row - 1, col - 1)) {
            res.add(d2Tod1(row - 1, col - 1));
        }
        if (inBound(row - 1, col + 1)) {
            res.add(d2Tod1(row - 1, col + 1));
        }
        if (inBound(row + 1, col - 1)) {
            res.add(d2Tod1(row + 1, col - 1));
        }
        if (inBound(row + 1, col +1)) {
            res.add(d2Tod1(row + 1, col + 1));
        }
        return res;
    }

    public Iterable<Integer> neighbors(int index) {
        return neighbors(row(index), col(index));
    }

    boolean inBound(int row, int col) {
        return (0 <= row && row < height && col >= 0 && col < width);
    }

    public static char[][] readChars(String boardFilePath) {
        In in = new In(boardFilePath);
        List<char[]> listChars = new ArrayList<>();
        while (in.hasNextLine()) {
            listChars.add(in.readLine().toCharArray());
        }
        for (int m = 0; m < listChars.size() - 1; m++) {
            if (listChars.get(m).length != listChars.get(m+1).length) {
                throw new IllegalArgumentException();
            }
        }
        char[][] res = new char[listChars.size()][listChars.get(0).length];
        for (int m = 0; m < listChars.size(); m++) {
            res[m] = listChars.get(m);
        }
        return res;
    }

}
