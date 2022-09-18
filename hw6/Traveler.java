import java.util.*;

public class Traveler {
    private final CharMap g;
    private final Trie trie;
    Set<Integer> marked = new HashSet<>();
    LinkedList<Character> chars = new LinkedList<>();
    LinkedList<String> validString = new LinkedList<>();
    Traveler(CharMap g, int start, Trie t) {
        this.g = g;
        this.trie = t;
        dfs(start);
    }

    public List<String> solution() {
        return validString;
    }

    void dfs(int index) {
        chars.addLast(g.get(index));
        marked.add(index);
        if (trie.hasPrefix(chars)) {
            for (int neighbor: g.neighbors(index)) {
                if (!marked.contains(neighbor)) {
                    dfs(neighbor);
                }
            }
        }
        if (trie.hasWord(chars)) {
            StringBuilder sb = new StringBuilder();
            for (char item: chars) {
                sb.append(item);
            }
            validString.add(sb.toString());
        }
        chars.removeLast();
        marked.remove(index);
    }

    public static void main(String[] args) {

        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.pop();
        for (int item: stack) {
            System.out.println(item);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(1);
        sb.append(2);
        System.out.println(sb.toString());
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());
    }

}
