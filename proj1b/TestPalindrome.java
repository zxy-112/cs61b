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
        assertTrue(Palindrome.isPalindrome("aoa"));
        assertTrue(Palindrome.isPalindrome("o"));
        assertTrue(Palindrome.isPalindrome(""));

        assertFalse(Palindrome.isPalindrome("zxy"));
        assertFalse(Palindrome.isPalindrome("zxyxxz"));
        assertFalse(Palindrome.isPalindrome("Aoa"));

        assertTrue(Palindrome.isPalindromeRecursive("aoa"));
        assertTrue(Palindrome.isPalindromeRecursive("o"));
        assertTrue(Palindrome.isPalindromeRecursive(""));

        assertFalse(Palindrome.isPalindromeRecursive("zxy"));
        assertFalse(Palindrome.isPalindromeRecursive("Aoa"));
        assertFalse(Palindrome.isPalindromeRecursive("zxyxxz"));

        CharacterComparator offByOne = new OffByOne();
        assertTrue(Palindrome.isPalindrome("o", offByOne));
        assertTrue(Palindrome.isPalindrome("", offByOne));
        assertTrue(Palindrome.isPalindrome("zxy", offByOne));
        assertTrue(Palindrome.isPalindrome("%&", offByOne));

        assertFalse(Palindrome.isPalindrome("aoa", offByOne));
        assertFalse(Palindrome.isPalindrome("Aoa", offByOne));
        assertFalse(Palindrome.isPalindrome("zxyxxz", offByOne));
    }
}
