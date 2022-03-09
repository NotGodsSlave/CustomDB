import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {
    private Cell c1;
    private Cell c2;

    @Before
    public void setUp() throws Exception {
        c1 = new Cell("Money");
        c2 = new Cell("Character");
    }

    @Test
    public void checkValue() {
        Assert.assertTrue(c1.checkValue("150$"));
        Assert.assertTrue(c1.checkValue("0$"));
        Assert.assertFalse(c1.checkValue("614"));
        Assert.assertFalse(c1.checkValue("$"));
        Assert.assertTrue(c2.checkValue("$"));
        Assert.assertFalse(c2.checkValue("150$"));
        Assert.assertTrue(c2.checkValue("."));

    }
}