package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class World {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int maxRectSize = 8;
    public static final int minRectSize = 3;
    public static final int density = 7;
    TETile[][] tiles = new TETile[WIDTH][HEIGHT];
    Random random;
    TERenderer ter = new TERenderer();

    public World(Random random) {
        ter.initialize(WIDTH, HEIGHT);
        this.random = random;
        fillWithNothing();
    }

    /**
     * fill all the tiles with nothing.
     */
    public void fillWithNothing() {
        for (int m = 0; m < tiles.length; m = m + 1) {
            for (int n = 0; n < tiles[0].length; n = n + 1) {
                tiles[m][n] = Tileset.NOTHING;
            }
        }
    }

    public void fillWithRandomRect() {
        Rectangular rect = initRect();
        expandRect(rect);
    }

    public void render() {
        ter.renderFrame(tiles);
    }

    /**
     * add rectangular to tiles
     * @param rectangular the rectangular that will be added to tiles
     */
    void addRectangular(Rectangular rectangular) {
        for(int m = 0; m < rectangular.width; m = m + 1) {
            for (int n = 0; n < rectangular.height; n = n + 1) {
                if (m == 0 || m == rectangular.width - 1 || n == 0 || n == rectangular.height - 1) {
                    tiles[rectangular.x + m][rectangular.y + n] = Tileset.WALL;
                } else {
                    tiles[rectangular.x + m][rectangular.y + n] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * generate an initial random rectangular in the world.
     * @return the generated initial rectangular.
     */
    Rectangular initRect() {
        int width = RandomUtils.uniform(random, minRectSize, maxRectSize);
        int height = RandomUtils.uniform(random, minRectSize, maxRectSize);
        int maxX = WIDTH - width;
        int maxY = HEIGHT - height;
        int x = RandomUtils.uniform(random, maxX);
        int y = RandomUtils.uniform(random, maxY);
        Rectangular res = new Rectangular(x, y, width, height);
        addRectangular(res);
        return res;
    }

    /**
     * expand the rect with other random rectangular to get
     * a big world.
     * @param rect the rectangular to be expanded
     */
    void expandRect(Rectangular rect) {
        for (int m = 0; m < density; m = m + 1) {
            Position newPos = rect.pickPosition(random);
            Rectangular newRect = rect.randomNeighbor(newPos, random);
            if (canPlace(newRect)) {
                addRectangular(newRect);
                digPassage(rect, newPos);
                expandRect(newRect);
            }
        }
    }

    /**
     * make passage (FLOOR) of rect at pos.
     * @param rect the Rectangular that to be digged.
     * @param pos where to make passage
     */
    void digPassage(Rectangular rect, Position pos) {
        if (pos.x == rect.x) {
            tiles[pos.x - 1][pos.y] = Tileset.FLOOR;
        } else if (pos.x == rect.x + rect.width - 1) {
            tiles[pos.x + 1][pos.y] = Tileset.FLOOR;
        } else if (pos.y == rect.y) {
            tiles[pos.x][pos.y - 1] = Tileset.FLOOR;
        } else if (pos.y == rect.y + rect.height - 1) {
            tiles[pos.x][pos.y + 1] = Tileset.FLOOR;
        }
        tiles[pos.x][pos.y] = Tileset.FLOOR;
    }

    /**
     * decide weather the rectangular can be placed in the tiles
     * @param rectangular the rectangular to be decided
     * @return true or false
     */
    boolean canPlace(Rectangular rectangular) {
        for (int m = 0; m < rectangular.width; m = m + 1) {
            for (int n = 0; n < rectangular.height; n = n + 1) {
                if (rectangular.x + m < 0
                        || rectangular.y + n < 0
                        || rectangular.x + m >= WIDTH
                        || rectangular.y + n >= HEIGHT) {
                    return false;
                }
                if (!(tiles[rectangular.x + m][rectangular.y + n] == Tileset.NOTHING)) {
                    return false;
                }
            }
        }
        return true;
    }
}
