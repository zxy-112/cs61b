package byog.Core;

import java.util.Random;
import java.io.Serializable;

/**
 * the helper class for World class to generate world.
 */
public class Rectangular implements  Serializable{
    int x;
    int y;
    int width;
    int height;
    private static final long serialVersionUID = 1L;

    public Rectangular(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * return a random position of the edge of rectangular except the vertex
     * @param random instance of Random
     * @return random position
     */
    Position pickPosition(Random random) {
        int totalChoice = 2 * (width - 2) + 2 * (height - 2);
        int randInt = random.nextInt(totalChoice);
        if (randInt < 2 * (width - 2)) {
            if (randInt < (width - 2)) {
                return new Position(x + 1 + randInt, y);
            } else {
                randInt = randInt - (width - 2);
                return new Position(x + 1 + randInt, y + height - 1);
            }
        } else {
            randInt = randInt - 2 * (width - 2);
            if(randInt < height - 2) {
                return new Position(x, y + 1 + randInt);
            } else {
                randInt = randInt - (height - 2);
                return new Position(x + width - 1, y + 1 + randInt);
            }
        }
    }

    /**
     * return a random size neighbor rectangular that is next to the
     * given position.
     *
     * @param pos    the position that the neighbor must next to.
     * @param random the instance of Random
     * @return a random size neighbor rectangular
     */
    Rectangular randomNeighbor(Position pos, Random random) {
        int width = RandomUtils.uniform(random, World.MIN_RECT_SIZE, World.MAX_RECT_SIZE);
        int height = RandomUtils.uniform(random, World.MIN_RECT_SIZE, World.MAX_RECT_SIZE);
        if(pos.x == x) {
            int yOffset = -RandomUtils.uniform(random, height - 2);
            return new Rectangular(pos.x - width, pos.y - 1 + yOffset, width, height);
        } else if (pos.x == x + this.width - 1) {
            int yOffset = -RandomUtils.uniform(random, height - 2);
            return new Rectangular(pos.x + 1, pos.y - 1 + yOffset, width, height);
        } else if (pos.y == y) {
            int xOffset = -RandomUtils.uniform(random, width - 2);
            return new Rectangular(pos.x - 1 + xOffset, pos.y - height, width, height);
        } else if (pos.y == y + this.height - 1) {
            int xOffset = -RandomUtils.uniform(random, width - 2);
            return new Rectangular(pos.x - 1 + xOffset, pos.y + 1, width, height);
        } else {
            return null;
        }
    }
}
