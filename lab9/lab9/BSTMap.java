package lab9;

import java.util.*;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }

        private Node() {
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (p.key.compareTo(key) == 0) {
            return p.value;
        }
        if (p.key.compareTo(key) > 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            return new Node(key, value);
        }
        if (p.key.compareTo(key) == 0) {
            p.value = value;
            return p;
        }
        if (p.key.compareTo(key) > 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (root == null) {
            root = new Node(key, value);
        } else {
            putHelper(key, value, root);
        }
        size += 1;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> res = new HashSet<>();
        keySetHelper(res, root);
        return res;
    }

    private void keySetHelper(Set<K> set, Node node) {
        if (node != null) {
            set.add(node.key);
            keySetHelper(set, node.left);
            keySetHelper(set, node.right);
        }
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        Node sentinel = new Node();
        sentinel.left = root;
        V res = removeHelper(sentinel, root, key, true);
        if (res != null) {
            size -= 1;
        }
        if (sentinel.left == null) {
            root = null;
        }
        return res;
    }

    public V removeHelper(Node parent, Node child, K key, boolean leftChild) {
        if (child == null) {
            return null;
        }
        if (child.key.compareTo(key) == 0) {
            removeNode(parent, child, leftChild);
            return child.value;
        } else if (child.key.compareTo(key) > 0) {
            return removeHelper(child, child.left, key, true);
        } else {
            return removeHelper(child, child.right, key, false);
        }
    }

    private void removeNode(Node parent, Node child, boolean leftChild) {
        if (child.left == null && child.right == null) {
            if (leftChild) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        } else if (child.right != null && child.left == null) {
            if (leftChild) {
                parent.left = child.right;
            } else {
                parent.right = child.right;
            }
        } else if (child.left != null && child.right == null) {
            if (leftChild) {
                parent.left = child.left;
            } else {
                parent.right = child.left;
            }
        } else {
            child.value = removeBiggestNode(child, child.left);
        }
    }

    private V removeBiggestNode(Node parent, Node child) {
        if (child == null) {
            return parent.value;
        } else {
            if (child.left == null) {
                parent.left = null;
                return child.value;
            } else {
                return removeBiggestNode(child, child.left);
            }
        }
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        Node sentinel = new Node();
        sentinel.left = root;
        V res = removeHelper(sentinel, root, key, value, true);
        if (res != null) {
            size -= 1;
        }
        if (sentinel.left == null) {
            root = null;
        }
        return res;
    }

    public V removeHelper(Node parent, Node child, K key, V value, boolean leftChild) {
        if (child == null) {
            return null;
        }
        if (child.key.compareTo(key) == 0) {
            if (child.value.equals(value)) {
                removeNode(parent, child, leftChild);
                return child.value;
            }
            return null;
        } else if (child.key.compareTo(key) > 0) {
            return removeHelper(child, child.left, key, value, true);
        } else {
            return removeHelper(child, child.right, key, value, false);
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
