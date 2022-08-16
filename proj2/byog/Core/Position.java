package byog.Core;
import java.io.Serial;
import java.io.Serializable;

public class Position implements Serializable{
    int x;
    int y;
    @Serial
    private static final long serialVersionUID = 1L;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
