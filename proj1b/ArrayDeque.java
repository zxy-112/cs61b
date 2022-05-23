public class ArrayDeque<T> implements Deque<T> {

    /* core data structure is array;
    size is the number of items;
    end is the index where to addLast();
    getIndex(end - 1) is the index of the last item if exists;
    getIndex(front-1) is the index where to addFirst();
    front is the index of the first item if exists;
    capacity is the length of the based core data structure which is an array;
     */

    private T[] items;
    private int size;
    private int end;
    private int front;
    private int capacity;

    ArrayDeque() {
        capacity = 4;
        items = (T[]) new Object[capacity];
        size = 0;
        front = 0;
        end = 0;
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isFull() {
        return size == capacity;
    }

    private boolean isWaste() {
        return size <= capacity / 4 && (!(capacity == 1));
    }

    private void reSize() {
        if (isFull()) {
            int newCapacity = 2 * capacity;
            T[] temp = (T[]) new Object[newCapacity];
            System.arraycopy(items, front, temp, 0, capacity - front);
            System.arraycopy(items, 0, temp, capacity - front, end);
            front = 0;
            end = size;
            capacity = newCapacity;
            items = temp;
        } else if (isWaste()) {
            int newCapacity = capacity / 2;
            T[] temp = (T[]) new Object[newCapacity];
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

    @Override
    public void addLast(T item) {
        reSize();
        items[end] = item;
        end = getIndex(end + 1);
        size = size + 1;
    }

    @Override
    public void addFirst(T item) {
        reSize();
        items[getIndex(front - 1)] = item;
        front = getIndex(front - 1);
        size = size + 1;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(items[getIndex(front + i)] + " ");
        }
        System.out.print("\n");
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T res = items[front];
        items[front] = null;
        front = getIndex(front + 1);
        size = size - 1;
        reSize();
        return res;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T res = items[getIndex(end - 1)];
        items[getIndex(end - 1)] = null;
        end = getIndex(end - 1);
        size = size - 1;
        reSize();
        return res;
    }

    @Override
    public T get(int index) {
        if (index >= 0 && index < size) {
            return items[getIndex(front + index)];
        }
        return null;
    }
}
