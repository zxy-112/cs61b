import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("aoa"));
        assertTrue(palindrome.isPalindrome("o"));
        assertTrue(palindrome.isPalindrome(""));

        assertFalse(palindrome.isPalindrome("zxy"));
        assertFalse(palindrome.isPalindrome("zxyxxz"));
        assertFalse(palindrome.isPalindrome("Aoa"));

        assertTrue(palindrome.isPalindromeRecursive("aoa"));
        assertTrue(palindrome.isPalindromeRecursive("o"));
        assertTrue(palindrome.isPalindromeRecursive(""));

        assertFalse(palindrome.isPalindromeRecursive("zxy"));
        assertFalse(palindrome.isPalindromeRecursive("Aoa"));
        assertFalse(palindrome.isPalindromeRecursive("zxyxxz"));

        CharacterComparator offByOne = new OffByOne();
        assertTrue(palindrome.isPalindrome("o", offByOne));
        assertTrue(palindrome.isPalindrome("", offByOne));
        assertTrue(palindrome.isPalindrome("zxy", offByOne));
        assertTrue(palindrome.isPalindrome("%&", offByOne));

        assertFalse(palindrome.isPalindrome("aoa", offByOne));
        assertFalse(palindrome.isPalindrome("Aoa", offByOne));
        assertFalse(palindrome.isPalindrome("zxyxxz", offByOne));
    }
}
