package byog.Core;

import byog.SaveDemo.World;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Game {
    /** the render engine.*/
    private TERenderer ter = new TERenderer();
    /** the width of the world. */
    public static final int WIDTH = 80;
    /** the height of the world. */
    public static final int HEIGHT = 30;
    /** the random instance of the world.*/
    private Random random;
    /** the seed of the world.*/
    private int seed;
    /** the TETile array that represents the world.*/
    TETile[][] tiles;
    /** save the game or not when exited.*/
    private boolean saveFlag;
    /** the initial rect.*/
    private Rectangular initRect;
    /** the max size of rect.*/
    public static final int MAXSIZE = 8;
    /** the min size of rect.*/
    public static final int MINSIZE = 3;
    /** the density of the generated structure.*/
    public static final int DENSITY = 7;
    /** the position of the player.*/
    private Position playerPos;
    /** exit the game or not.*/
    private boolean exitFlag;
    /** if : has been typed.*/
    private boolean commandActivate;
    /** the x offset of the world in the canvas.*/
    public static final int XOFF = 10;
    /** the last rect when expand rect.*/
    private Rectangular lastOne;

    public Game() {
        exitFlag = false;
        saveFlag = false;
    }
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        parseString(input);
        ter.initialize(WIDTH + XOFF, HEIGHT, XOFF, 0);
        ter.renderFrame(tiles);

        while (!exitFlag) {
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();
            if (StdDraw.hasNextKeyTyped()) {
                char typed = StdDraw.nextKeyTyped();
                processInputChar(typed);
            }
            StdDraw.show();
        }

        TETile[][] finalWorldFrame = tiles;
        return finalWorldFrame;
    }

    /**
     * method to parse the input string.
     * @param str the input string
     */
    public void parseString(String str) {
        if (str.equals("l")) {
            saveFlag = true;
            loadGame();
        } else if (str.charAt(0) == 'n' || str.charAt(1) == 'N') {
            String tailString = str.substring(str.length()-2);
            String seedString;
            if (tailString.equals(":q")) {
                saveFlag = true;
                seedString = str.substring(1, str.length()-2);
            } else {
                saveFlag = false;
                seedString = str.substring(1);
            }
            parseSeedString(seedString);
            generateWorld();
        } else {
            throw new RuntimeException("input string should start with n");
        }
    }

    /**
     * method to load the saved tiles.
     */
    void loadGame() {
        File f = new File("./world.ser");
        File f2 = new File("./player.ser");
        if (f.exists() && f2.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                tiles = (TETile[][]) os.readObject();
                fs = new FileInputStream(f2);
                os = new ObjectInputStream(fs);
                playerPos = (Position) os.readObject();
                os.close();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
    }

    /**
     * save the tiles.
     */
    void saveGame() {
        File f = new File("./world.ser");
        File f2 = new File("./player.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            if (!f2.exists()) {
                f2.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(tiles);
            fs = new FileOutputStream(f2);
            os = new ObjectOutputStream(fs);
            os.writeObject(playerPos);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("io exception");
            System.exit(0);
        }
    }

    void processInputChar(char typed) {
        if (typed == ':') {
            commandActivate = true;
        } else if (typed == 'q') {
            if (commandActivate) {
                exitFlag = true;
                if (saveFlag) {
                    saveGame();
                }
            }
        } else {
            commandActivate = false;
            if (typed == 'w') {
                changePos(new Position(playerPos.x, playerPos.y + 1));
            } else if (typed == 'a') {
                changePos(new Position(playerPos.x - 1, playerPos.y));
            } else if (typed == 's') {
                changePos(new Position(playerPos.x, playerPos.y - 1));
            } else if (typed == 'd') {
                changePos(new Position(playerPos.x + 1, playerPos.y));
            }
            ter.renderFrame(tiles);
        }
    }

    /**
     * algorithm that parse the seedString to int and set the seed
     * of the game.
     * @param seedString the seedString that from the input.
     */
    void parseSeedString(String seedString) {
        seed = strToInt(seedString);
        random = new Random(seed);
    }

    int strToInt(String str) {
        int res = 0;
        for (char m: str.toCharArray()) {
            res = res + (int) m % RandomUtils.uniform(new Random(0), 5, 10);
        }
        return res;
    }

    /**
     * generate the world according the seed.
     */
    void generateWorld() {
        tiles = new TETile[WIDTH][HEIGHT];
        fillWithNothing(tiles);
        initializeRect();
        expandRect(initRect);
        while (true) {
            Position doorPos = lastOne.pickPosition(random);
            if (tiles[doorPos.x][doorPos.y].equals(Tileset.WALL)) {
                tiles[doorPos.x][doorPos.y] = Tileset.LOCKED_DOOR;
                break;
            }
        }
    }

    /**
     * fill the tiles with nothing.
     * @param tiles which tiles.
     */
    public static void fillWithNothing(TETile[][] tiles) {
        for (TETile[] m: tiles) {
            Arrays.fill(m, Tileset.NOTHING);
        }
    }

    /**
     * add an initial rect to the tiles.
     * and add the player.
     */
    void initializeRect() {
        int rectX = RandomUtils.uniform(random, WIDTH - MAXSIZE);
        int rectY = RandomUtils.uniform(random, HEIGHT - MAXSIZE);
        int rectWidth = RandomUtils.uniform(random, MINSIZE, MAXSIZE);
        int rectHeight = RandomUtils.uniform(random, MINSIZE, MAXSIZE);
        initRect = new Rectangular(rectX, rectY, rectWidth, rectHeight);
        addRect(initRect);
        int playerX = RandomUtils.uniform(random, initRect.width - 2) + initRect.x + 1;
        int playerY = RandomUtils.uniform(random, initRect.height - 2) + initRect.y + 1;
        playerPos = new Position(playerX, playerY);
        changePos(playerPos);
    }

    void changePos(Position newPos) {
        if (!tiles[newPos.x][newPos.y].equals(Tileset.FLOOR)) {
            return;
        }
        tiles[playerPos.x][playerPos.y] = Tileset.FLOOR;
        tiles[newPos.x][newPos.y] = Tileset.PLAYER;
        playerPos = newPos;
    }

    /**
     * add rect to tiles.
     * @param rect the rect.
     */
    void addRect(Rectangular rect) {
        int rightMost = rect.width - 1;
        int upMost = rect.height - 1;
        for (int m = 0; m < rect.width; m = m + 1) {
            for (int n = 0; n < rect.height; n = n + 1) {
                if (m == 0 || n == 0 || m == rightMost ||  n== upMost) {
                    tiles[rect.x + m][rect.y + n] = Tileset.WALL;
                } else {
                    tiles[rect.x + m][rect.y + n] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     * expand rect with other rect.
     * @param rect which rect to expand.
     */
    void expandRect(Rectangular rect) {
        for (int m = 0; m < DENSITY; m = m + 1) {
            Position pos = rect.pickPosition(random);
            Rectangular neighbor = rect.randomNeighbor(pos, random);
            if (canPlace(neighbor)) {
                lastOne = neighbor;
                addRect(neighbor);
                digPassage(rect, pos);
                expandRect(neighbor);
            }
        }
    }

    /**
     * the method to dig passage.
     * @param rect the rect to be dug.
     * @param pos where to dig.
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
     * return weather the rect can be placed.
     * @param rectangular the rect.
     * @return true or false.
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

    /**
     * @author 77584
     */
    static class Position implements Serializable {
        int x;
        int y;
        public Position(int xPos, int yPos) {
            x = xPos;
            y = yPos;
        }
    }

    /**
     * @author 77584
     */
    static class Rectangular{
        /** the x pos of the left bottom corner of the rec.*/
        int x;
        /** the y pos of the left bottom corner of the rec.*/
        int y;
        /** the width of the rect.*/
        int width;
        /** the height of the rect.*/
        int height;
        public Rectangular(int xPos, int yPos, int recWidth, int recHeight) {
            x = xPos;
            y = yPos;
            width = recWidth;
            height = recHeight;
        }

        /**
         * pick a random position from the edge of rect.
         * @param random Random instance.
         * @return picked position.
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
         * return a random size rect as neighbor.
         * @param pos where to next to.
         * @param random the Random instance.
         * @return the random size neighbor rect.
         */
        Rectangular randomNeighbor(Position pos, Random random) {
            int width = RandomUtils.uniform(random, Game.MINSIZE, Game.MAXSIZE);
            int height = RandomUtils.uniform(random, Game.MINSIZE, Game.MAXSIZE);
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
}
