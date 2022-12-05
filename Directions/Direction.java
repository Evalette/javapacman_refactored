import java.awt.*;

public interface Direction {
    void drawImage(Graphics g, Player player);

    boolean isOpposite(Direction direction);
    
    void movePlayer(Player player, int gridSize);

    Direction getOpposite();

    void moveIfValid(Mover mover);

    /* Determines if a set of coordinates is a valid destination.*/
    default boolean isValidDest(int x, int y, Mover mover) {
    /* The first statements check that the x and y are inbounds.  The last statement checks the map to
       see if it's a valid location */
        return (((x) % 20 == 0) || ((y) % 20) == 0) && 20 <= x && x < 400 && 20 <= y && y < 400 && mover.state[x / 20 - 1][y / 20 - 1];
    }
}
