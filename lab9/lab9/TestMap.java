package lab9;

import edu.princeton.cs.algs4.SET;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Optional;
import java.util.Set;

public class TestMap {
    @Test
    public void testBSTMap() {
        BSTMap<String, Integer> bstMap = new BSTMap<>();
        bstMap.put("zxy", 7758);
        bstMap.put("lhj", 7658);
        bstMap.put("lmf", 234);
        bstMap.put("16601530222", 2334);
        Set<String> set = bstMap.keySet();
        for (String str: set) {
            System.out.println(str);
        }
        for (String str: bstMap) {
            System.out.println(str);
        }
        assertEquals(7758, (int) bstMap.get("zxy"));
        System.out.println(bstMap.remove("zxy"));
        System.out.println(bstMap.remove("lhj"));
        System.out.println(bstMap.remove("lmf"));
        System.out.println(bstMap.remove("lmf"));
        assertEquals(1, bstMap.size());
    }
}
