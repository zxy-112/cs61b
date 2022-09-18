import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TestTrie {

    @Test
    public void testCharMap() {
        char[][] actual = CharMap.readChars("exampleBoard.txt");

        assertEquals(4, actual.length);
        assertEquals(4, actual[0].length);

        System.out.println(Arrays.deepToString(actual));
    }

    @Test
    public void testHasPrefix() {
        Trie trie = new Trie();
        char[] seq1 = {'a', 'b', 'c'};
        trie.addSequence(seq1);

        List<Character> chars = new LinkedList<>();
        chars.add('a');
        assertTrue(trie.hasPrefix(chars));
        chars.add('b');
        assertTrue(trie.hasPrefix(chars));
        assertFalse(trie.hasWord(chars));
        chars.add('c');
        assertTrue(trie.hasWord(chars));
    }

}
