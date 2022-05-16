public class ArrayDeque<LockLess> {

    /* core data structure is array;
    size is the number of items;
    end is the index where to addLast();
    getIndex(end - 1) is the index of the last item if exists;
    getIndex(front-1) is the index where to addFirst();
    front is the index of the first item if exists;
    capacity is the length of the based core data structure which is an array;
     */

    private LockLess[] items;
    private int size;
    private int end;
    private int front;
    private int capacity;

    ArrayDeque() {
        capacity = 4;
        items = (LockLess[]) new Object[capacity];
        size = 0;
        front = 0;
        end = 0;
    }

    public int size() {
       return size;
    }

    public int capacity() {
        return capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isFull() {
        return size == capacity;
    }

    private boolean isWaste() {
        return size <= capacity / 4 && (!(capacity==1));
    }

    private void reSize() {
        if (isFull()) {
            int newCapacity = 2 * capacity;
            LockLess[] temp = (LockLess[]) new Object[newCapacity];
            System.arraycopy(items, front, temp, 0, capacity -front);
            System.arraycopy(items, 0, temp, capacity - front, end);
            front = 0;
            end = size;
            capacity = newCapacity;
            items = temp;
        } else if (isWaste()) {
            int newCapacity = capacity / 2;
            LockLess[] temp = (LockLess[]) new Object[newCapacity];
            if (end >= front) {
                System.arraycopy(items, front, temp, 0, size);
            } else {
                System.arraycopy(items, front, temp, 0, capacity - front);
                System.arraycopy(items, 0, temp, capacity - front, end);
            }
            front = 0;
            end = size;
            capacity = newCapacity;
            items = temp;
        }
    }

    private int getIndex(int index) {
        return (index + capacity) % capacity;
    }

    public void addLast(LockLess item) {
        reSize();
        items[end] = item;
        end = getIndex(end + 1);
        size = size + 1;
    }

    public void addFirst(LockLess item) {
        reSize();
        items[getIndex(front-1)] = item;
        front = getIndex(front - 1);
        size = size + 1;
    }

    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[getIndex(front+i)] + " ");
        }
        System.out.print("\n");
    }

    public LockLess removeFirst() {
        if (isEmpty()) {
            return null;
        }
        LockLess res = items[front];
        items[front] = null;
        front = getIndex(front + 1);
        size = size - 1;
        reSize();
        return res;
    }

    public LockLess removeLast() {
        if (isEmpty()) {
            return null;
        }
        LockLess res = items[getIndex(end-1)];
        items[getIndex(end-1)] = null;
        end = getIndex(end - 1);
        size = size - 1;
        reSize();
        return res;
    }

    public LockLess get(int index) {
        if (index >= 0 && index < size) {
            return items[getIndex(front+index)];
        }
        return null;
    }
}
