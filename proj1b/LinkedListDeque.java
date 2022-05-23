public class LinkedListDeque<Lockless> implements Deque<Lockless> {

    /*
    the number of items in the list should be size;
    the first item is sentinel.rest if exists;
    the last item is sentinel.prev if exists;
    get() returns null if deque is empty;
     */

    private class LockNode {
        private Lockless first;
        private LockNode rest;
        private LockNode prev;

        LockNode(Lockless first, LockNode rest, LockNode prev) {
            this.first = first;
            this.rest = rest;
            this.prev = prev;
        }

        /* used to created sentinel node */
        LockNode() {
            this.rest = this;
            this.prev = this;
        }
    }

    private final LockNode sentinel;
    private int size;

    public LinkedListDeque() {
        this.sentinel = new LockNode();
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public void printDeque() {
        LockNode point = this.sentinel.rest;
        while (point != this.sentinel) {
            System.out.print(point.first + " ");
            point = point.rest;
        }
        System.out.print("\n");
    }
    public void addFirst(Lockless item) {
        LockNode created = new LockNode(item, this.sentinel.rest, this.sentinel);
        this.sentinel.rest = created;
        created.rest.prev = created;
        this.size = this.size + 1;
    }

    public void addLast(Lockless item) {
        LockNode created = new LockNode(item, this.sentinel, this.sentinel.prev);
        this.sentinel.prev = created;
        created.prev.rest = created;
        this.size = this.size + 1;
    }

    public Lockless removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        /* point to the Node that need to be deleted */
        LockNode point = this.sentinel.rest;
        point.prev.rest = point.rest;
        point.rest.prev = point.prev;
        this.size = this.size - 1;
        return point.first;
    }

    /* remove the last node,
    size should be 1 less
     */
    public Lockless removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        LockNode point = this.sentinel.prev;
        point.prev.rest = point.rest;
        point.rest.prev = point.prev;
        this.size = this.size - 1;
        return point.first;
    }

    public Lockless get(int index) {
        if (index < this.size && index >= 0) {
            LockNode point = this.sentinel;
            for (int i = 0; i <= index; i++) {
                point = point.rest;
            }
            return point.first;
        }
        return null;
    }

    public Lockless getRecursive(int index) {
        if (index >= 0 && index < size) {
            return this.getRecursive(this.sentinel.rest, index);
        }
        return null;
    }

    private Lockless getRecursive(LockNode point, int index) {
        if (index == 0) {
            return point.first;
        }
        return this.getRecursive(point.rest, index - 1);
    }
}
