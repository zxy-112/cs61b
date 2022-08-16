package byog.Core;

import java.util.Random;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Game {
    /* Feel free to change the width and height. */
    private boolean saveFlag;
    private boolean exitFlag = false;
    private boolean commandActivate = false;
    private World world;

    public Game() {
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
        if (input.equals("l")) {
            world = loadWorld();
            world.canvasInit();
            saveFlag = true;
            if (world == null) {
                throw new RuntimeException("no archive found");
            }
        } else {
            int seed = parseInputString(input);
            world = new World(new Random(seed));
            world.fillWithRandomRect();
        }
        world.render();
        while (!exitFlag) {
            if (StdDraw.hasNextKeyTyped()) {
                char typedKey = StdDraw.nextKeyTyped();
                processInputChar(typedKey);
            }
        }
        TETile[][] finalWorldFrame = world.tiles;
        return finalWorldFrame;
    }

    World loadWorld() {
        File f = new File("./world.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                World loadWorld = (World) os.readObject();
                os.close();
                return loadWorld;
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
        return null;
    }


    void saveWorld() {
        File f = new File("./world.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(world);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
    void processInputChar(char input) {
        if (commandActivate) {
            if (input == 'q') {
                if (saveFlag) {
                    saveWorld();
                }
                exitFlag = true;
            } else commandActivate = input == ':';
        } else {
            if (input == ':') {
                commandActivate = true;
            } else if (input == 'w') {
                world.move(0, 1);
            } else if (input == 'a') {
                world.move(-1, 0);
            } else if (input == 's') {
                world.move(0, -1);
            } else if (input == 'd') {
                world.move(1, 0);
            }
            drawFrame(String.valueOf(input));
        }
    }

    void drawFrame(String str) {
        world.render();
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, 2, str);
        StdDraw.show();
    }

    /**
     * parse the input string, including check if the input is legal
     * and if the game should be saved or not and return a random integer
     * as the seed for world according to the input string.
     * @param str the input string, used to generate a seed and decide
     *            weather to save the game or not.
     * @return the random seed according to the given string.
     */
    int parseInputString(String str) {
        String seedString;
        if (parseBeginning(str)) {
            if (parseEnding(str)) {
                saveFlag = true;
                seedString = str.substring(1, str.length()-2);
            } else {
                seedString = str.substring(1);
            }
            return randomStrToRandomInt(seedString);
        } else {
            throw new RuntimeException("invalid input string");
        }
    }

    int randomStrToRandomInt(String str) {
        return Integer.parseInt(str);
    }

    /**
     * return weather the str is beginning with letter n.
     * @param str the str to be judged
     * @return true or false
     */
    boolean parseBeginning(String str) {
        char expect = 'n';
        str = str.toLowerCase();
        char actual = str.charAt(0);
        return expect == actual;
    }

    /**
     * return weather to save the game or not
     * @param str the input string
     * @return save or not
     */
    boolean parseEnding(String str) {
        String expect = ":q";
        String actual = str.substring(str.length()-2);
        return actual.equals(expect);
    }


    public static void main(String[] args) {
        Game game = new Game();
        game.playWithInputString("l");
        System.exit(0);
    }
}
