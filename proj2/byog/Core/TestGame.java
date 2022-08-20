package byog.Core;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class TestGame {
    @Test
    public void testCanPlace() {
        World world = new World(new Random());
        assertTrue(world.canPlace(new Rectangular(0, 0, World.WIDTH - 1, World.HEIGHT - 1)));
        assertFalse(world.canPlace(new Rectangular(-1, 0, 3, 3)));
        assertTrue(world.canPlace(new Rectangular(0, 0, 3, 3)));
    }

    @Test
    public void testPickPosition() {
        Rectangular rectangular = new Rectangular(2, 2, 4, 5);
        Random random = new Random();
        for (int m = 0; m < 10000; m = m + 1) {
            Position newPos = rectangular.pickPosition(random);
            boolean atEdge = newPos.x == rectangular.x
                    || newPos.y == rectangular.y
                    || newPos.x == rectangular.x + rectangular.width - 1
                    || newPos.y == rectangular.y + rectangular.height - 1;
            boolean atVertex = (
                    newPos.x == rectangular.x && newPos.y == rectangular.y
                    || newPos.x == rectangular.x && newPos.y == rectangular.y + rectangular.height - 1
                    || newPos.x == rectangular.x + rectangular.width - 1 && newPos.y == rectangular.y
                    || newPos.x == rectangular.x + rectangular.width - 1
                    && newPos.y == rectangular.y + rectangular.height - 1
                    );
            assertTrue(atEdge && !atVertex);
        }
    }

    @Test
    public void parseInputString() {
        Game game = new Game();
        int actual = game.randomStrToRandomInt("53495755565648565251535457485149545251115");
        System.out.println(actual);
    }
}
