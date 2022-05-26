package synthesizer;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer<>(10);
        arb.enqueue(1);
        int expect = 1;
        int actual = arb.fillCount();
        assertEquals(expect, actual);
        try {
            for (int i = 0; i < 10; i = i + 1) {
                arb.enqueue(1);
            }
        } catch (RuntimeException e) {
            System.out.println("good");
        }

        Iterator<Integer> iter = arb.iterator();
        actual = iter.next();
        expect = 1;
        assertEquals(expect, actual);

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
