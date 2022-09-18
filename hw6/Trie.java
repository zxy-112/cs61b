import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private final Node root = new Node();
    private Node record;
    private int depth;

    public Trie() {
        record = root;
        depth = 0;
    }

    public void addSequence(char[] sequence) {
        Node current = root;
        for (char item: sequence) {
            if (!current.hasChild(item)) {
                current.putChild(item);
            }
            current = current.getChild(item);
        }
        current.colored = true;
        if (sequence.length > depth) {
            depth = sequence.length;
        }
    }

    public void resetTracer() {
        record = root;
    }

    boolean trace(char c) {
        if (record == null) {
            return false;
        }
        if (record.hasChild(c)) {
            record = record.getChild(c);
            return true;
        }
        record = null;
        return false;
    }

    boolean validPos() {
        if (record == null) {
            return false;
        }
        return record.colored;
    }

    public boolean hasPrefix(List<Character> chars) {
        for (char item: chars) {
            if (!trace(item)) {
                resetTracer();
                return false;
            }
        }
        resetTracer();
        return true;
    }

    public boolean hasPrefix(char[] chars) {
        for (char item: chars) {
            if (!trace(item)) {
                resetTracer();
                return false;
            }
        }
        resetTracer();
        return true;
    }

    public boolean hasWord(List<Character> chars) {
        for (char item: chars) {
            if (!trace(item)) {
                resetTracer();
                return false;
            }
        }
        boolean res = validPos();
        resetTracer();
        return res;
    }

    public boolean hasWord(char[] chars) {
        for (char item: chars) {
            if (!trace(item)) {
                resetTracer();
                return false;
            }
        }
        boolean res = validPos();
        resetTracer();
        return res;
    }

    static class Node {
        char value;
        Map<Character, Node> children;
        boolean colored;

        Node(char value) {
            this.value = value;
            children = new HashMap<>();
            colored = false;
        }

        Node() {
            children = new HashMap<>();
            colored = false;
        }

        boolean hasChild(char t) {
            return children.containsKey(t);
        }

        Node getChild(char t) {
            return children.get(t);
        }

        void putChild(char t) {
            Node node = new Node(t);
            children.put(t, node);
        }
    }
}
