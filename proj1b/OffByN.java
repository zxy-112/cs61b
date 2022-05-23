public class OffByN implements CharacterComparator {

    private final int n;

    OffByN(int n) {
        this.n = n;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return (y - x == n) || (x - y == n);
    }
}
