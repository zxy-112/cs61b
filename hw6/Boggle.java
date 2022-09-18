import edu.princeton.cs.introcs.In;

import java.io.File;
import java.util.*;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        File newFile = new File(boardFilePath);
        if (!newFile.exists()) {
            throw new IllegalArgumentException();
        }
        if (k <= 0) {
            throw new IllegalArgumentException();
        }

        Trie trie = new Trie();
        In in = new In(dictPath);
        if (!in.exists()) {
            throw new IllegalArgumentException();
        }
        while (in.hasNextLine()) {
            trie.addSequence(in.readLine().toCharArray());
        }

        CharMap g = new CharMap(boardFilePath);

        Set<String> allPossible = new HashSet<>();
        for (int m = 0; m < g.size(); m++) {
            Traveler traveler = new Traveler(g, m, trie);
            allPossible.addAll(traveler.solution());
        }
        List<String> allString = new LinkedList<>(allPossible);
        allString.sort(new stringComparator());

        if (allString.size() < k) {
            return allString;
        }
        return allString.subList(0, k);
    }

    static class stringComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            if (o1.length() > o2.length()) {
                return -1;
            } else if (o1.length() == o2.length()) {
                return o1.compareTo(o2);
            } else {
                return 1;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(solve(7, "smallBoard.txt"));
    }
}
