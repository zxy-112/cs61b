package creatures;

import org.junit.Assert;
import org.junit.Test;

public class TestReplicate {

    @Test
    public void testReplicate() {
        Plip p = new Plip();
        Plip newP = p.replicate();
        Assert.assertNotSame(p, newP);
    }
}
