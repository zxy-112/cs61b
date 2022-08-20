package byog.Core;
import java.io.Serializable;

/**
 * @author 77584
 */
public class Position implements Serializable {
    /** the x position.*/
    int x;
    /** the y position.*/
    int y;
    private static final long serialVersionUID = 1L;

    /**
     * create a position instance.
     * @param xPos the x position of the new position
     * @param yPos the y position of the new position
     */
    public Position(int xPos, int yPos) {
        this.x = xPos;
        this.y = yPos;
    }
}
