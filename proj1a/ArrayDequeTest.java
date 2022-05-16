public class ArrayDequeTest {

    public static void printStatus(boolean passed) {
        if (passed) {
            System.out.println("test passed");
        } else {
            System.out.println("test failed");
        }
        System.out.print("\n");
    }

    public static boolean checkSize(int expect, int actual) {
        if (!(expect == actual)) {
            System.out.println("size() gets " + actual + ", but expect:" + expect);
            return false;
        }
        return true;
    }

    public static boolean checkCapacity(int expect, int actual) {
        if (!(expect == actual)) {
            System.out.println("capacity() gets " + actual + ", but expect:" + expect);
            return false;
        }
        return true;
    }

    public static void addLastSizeIsEmptyTest() {
        System.out.println("addLastSizeIsEmptyTest runs");
        boolean passed = true;
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        if (!ad1.isEmpty()) {
            passed = false;
            System.out.println("isEmpty() get false but expect true");
        }

        ad1.addLast("i");
        int actualSize = ad1.size();
        int expectSize = 1;
        passed = passed && checkSize(expectSize, actualSize);

        ad1.addLast("have");
        ad1.addLast("an");
        ad1.addLast("egg");
        actualSize = ad1.size();
        expectSize = 4;
        passed = passed && checkSize(expectSize, actualSize);

        for (int i = 0; i < 4; i++) {
            ad1.removeLast();
        }
        actualSize = ad1.size();
        expectSize = 0;
        passed = passed && checkSize(expectSize, actualSize);

        System.out.println("the queue is now:");
        ad1.printDeque();

        printStatus(passed);
    }

    public static void addRemoveTest() {
        System.out.println("addRemoveTest runs");

        boolean passed = true;

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);
        ad1.addFirst(4);
        ad1.addFirst(5);

        int actualInt = ad1.get(4);
        int expectInt = 1;
        if (!(actualInt == expectInt)) {
            System.out.println("get(4) get " + actualInt + ", but expect:" + expectInt);
            passed = false;
        }

        System.out.println("the queue is now:");
        ad1.printDeque();

        int actualSize = ad1.size();
        int expectSize = 5;
        passed = passed && checkSize(expectSize, actualSize);

        printStatus(passed);
    }

    public static void capacityTest() {
        System.out.println("capacityTest runs");
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addLast(1);

        int actualCapacity = ad1.capacity();
        int expectCapacity = 2;

        boolean passed = checkCapacity(expectCapacity, actualCapacity);

        ad1.addFirst(2);
        ad1.addLast(3);
        System.out.println("the queue is now:");
        ad1.printDeque();
        actualCapacity = ad1.capacity();
        expectCapacity = 4;
        passed = passed && checkCapacity(expectCapacity, actualCapacity);

        ad1.addFirst(10);
        expectCapacity = 4;
        actualCapacity = ad1.capacity();
        passed = passed && checkCapacity(expectCapacity, actualCapacity);

        for (int i = 0; i < 100; i++) {
            ad1.removeFirst();
        }
        expectCapacity = 1;
        actualCapacity = ad1.capacity();
        passed = passed && checkCapacity(expectCapacity, actualCapacity);

        ad1.addFirst(1);
        expectCapacity = 1;
        actualCapacity = ad1.capacity();
        passed = passed && checkCapacity(expectCapacity, actualCapacity);

        printStatus(passed);
    }
    public static void main(String[] args) {
        addLastSizeIsEmptyTest();
        addRemoveTest();
        capacityTest();
    }
}
