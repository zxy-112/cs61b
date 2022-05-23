public interface Deque<T> {

    int size();
    boolean isEmpty();
    void addLast(T item);
    void addFirst(T item);
    T removeFirst();
    T removeLast();
    T get(int index);
    void printDeque();

}
