import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        int[] lengths = new int[asciis.length];
        int pos = 0;
        for (String item: asciis) {
            lengths[pos] = item.length();
            pos += 1;
        }

        int maxLength = 0;
        for (int item: lengths) {
            if (item > maxLength) {
                maxLength = item;
            }
        }

        String[] res = Arrays.copyOf(asciis, asciis.length);
        int index = maxLength - 1;
        for (int m = 0; m < maxLength; m += 1) {
            sortHelperLSD(res, index);
            index -= 1;
        }
        return res;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        String[] copied = new String[asciis.length];
        System.arraycopy(asciis, 0, copied, 0, asciis.length);

        // there is at most 256 kinds of symbol;
        int[] counts = new int[256];
        for (String ascii : asciis) {
            counts[getIndex(ascii, index)] += 1;
        }

        int[] starts = new int[counts.length];
        int pos = 0;
        for (int m = 0; m < counts.length; m += 1) {
            starts[m] = pos;
            pos += counts[m];
        }

        for (String string: copied) {
            pos = starts[getIndex(string, index)];
            asciis[pos] = string;
            starts[getIndex(string, index)] += 1;
        }

    }

    private static int getIndex(String str, int index) {
        if (index >= str.length()) {
            return 0;
        } else {
            return str.charAt(index);
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] strings = {"zxy", "lhj", "111", "3456", "1", "100"};
        System.out.println("origin:" + Arrays.toString(strings));
        String[] sortedString = sort(strings);
        System.out.println("origin after sort:" + Arrays.toString(strings));
        System.out.println("sorted strings:" + Arrays.toString(sortedString));
    }
}
