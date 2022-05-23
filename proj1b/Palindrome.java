public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i = i + 1) {
            wordDeque.addLast(word.charAt(i));
        }
        return wordDeque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> wordDeque = wordToDeque(word);
        char first;
        char last;
        while (!wordDeque.isEmpty()) {
            if (wordDeque.size() == 1) {
                wordDeque.removeFirst();
            } else {
                first = wordDeque.removeFirst();
                last = wordDeque.removeLast();
                if (first != last) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPalindromeRecursive(Deque<Character> wordDeque) {
        if (wordDeque.size() == 1 || wordDeque.isEmpty()) {
            return true;
        } else {
            char first;
            char last;
            first = wordDeque.removeFirst();
            last = wordDeque.removeLast();
            if (first != last) {
                return false;
            } else {
                return isPalindromeRecursive(wordDeque);
            }
        }
    }

    public boolean isPalindromeRecursive(String word) {
        Deque<Character> wordDeque = wordToDeque(word);
        return isPalindromeRecursive(wordDeque);
    }

    public boolean isPalindrome(String word, CharacterComparator comparator) {
        Deque<Character> wordDeque = wordToDeque(word);
        char first;
        char last;
        while (!wordDeque.isEmpty()) {
            if (wordDeque.size() == 1) {
                wordDeque.removeFirst();
            } else {
                first = wordDeque.removeFirst();
                last = wordDeque.removeLast();
                if (!comparator.equalChars(first, last)) {
                    return false;
                }
            }
        }
        return true;
    }
}
