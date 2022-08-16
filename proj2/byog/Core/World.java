package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serial;
import java.io.Serializable;

import java.util.Random;

public class World implements Serializable{

    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int MAX_RECT_SIZE = 8;
    public static final int MIN_RECT_SIZE = 3;
    public static final int DENSITY = 7;
    TETile[][] tiles = new TETile[WIDTH][HEIGHT];
    Position playerPos;
    Random random;
    TERenderer ter = new TERenderer();
    @Serial
    private static final long serialVersionUID = 1L;

    public World(Random random) {
        ter.initialize(WIDTH + 10, HEIGHT, 10, 0);
        this.random = random;
        fillWithNothing();
    }

    void canvasInit() {
        ter.initialize(WIDTH + 10, HEIGHT, 10, 0);
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
        int width = RandomUtils.uniform(random, MIN_RECT_SIZE, MAX_RECT_SIZE);
        int height = RandomUtils.uniform(random, MIN_RECT_SIZE, MAX_RECT_SIZE);
        int maxX = WIDTH - width;
        int maxY = HEIGHT - height;
        int x = RandomUtils.uniform(random, maxX);
        int y = RandomUtils.uniform(random, maxY);
        Rectangular res = new Rectangular(x, y, width, height);
        addRectangular(res);
        int playerX = RandomUtils.uniform(random, width-2) + x + 1;
        int playerY = RandomUtils.uniform(random, height - 2) + y + 1;
        playerPos = new Position(playerX, playerY);
        changePos(playerPos);
        return res;
    }

    void changePos(Position newPos) {
        if (tiles[newPos.x][newPos.y] != Tileset.FLOOR) {
            return;
        }
        tiles[playerPos.x][playerPos.y] = Tileset.FLOOR;
        tiles[newPos.x][newPos.y] = Tileset.PLAYER;
        playerPos = newPos;
    }

    void move(int diffX, int diffY) {
        Position newPos = new Position(playerPos.x + diffX, playerPos.y + diffY);
        changePos(newPos);
    }

    /**
     * expand the rect with other random rectangular to get
     * a big world.
     * @param rect the rectangular to be expanded
     */
    void expandRect(Rectangular rect) {
        for (int m = 0; m < DENSITY; m = m + 1) {
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

    void printTiles() {
        for (TETile[] tile : tiles) {
            for (int j = 0; j < tiles[0].length; j = j + 1) {
                System.out.print(tile[j].character());
            }
            System.out.println();
        }
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
