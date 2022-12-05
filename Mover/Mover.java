import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/* Both Player and Ghost inherit Mover.  Has generic functions relevant to both*/
abstract class Mover {
    /* frameCount is used to count animation frames*/
    int frameCount = 0;

    /* State contains the game map */
    final boolean[][] state;

    /* gridSize is the size of one square in the game.
       max is the height/width of the game.
       increment is the speed at which the object moves,
       1 increment per movePlayer() call */
    final int gridSize;
    final int max;
    final int increment;
    /* Direction ghost is heading */
    Direction direction;
    /* Current ghost location */
    int x;
    int y;
    /* Last ghost location*/
    int lastX;
    int lastY;
    /* The pellet the ghost is on top of */
    int pelletX;
    int pelletY;

    /* Generic constructor */
    public Mover(int x, int y) {
        gridSize = 20;
        increment = 4;
        max = 400;
        state = new boolean[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                state[i][j] = false;
            }
        }
        direction = new Left();
        this.x = x;
        this.y = y;
        this.lastX = x;
        this.lastY = y;
        pelletX = x / gridSize - 1;
        pelletY = x / gridSize - 1;
    }

    /* Updates the state information */
    public void updateState(boolean[][] state) {
        for (int i = 0; i < 20; i++) {
            System.arraycopy(state[i], 0, this.state[i], 0, 20);
        }
    }

    /* Chooses a new direction randomly for the ghost to movePlayer */
    public Direction newDirection() {
        int random;
        int lookX = x, lookY = y;
        Set<Direction> set = new HashSet<>();
        Direction backwards = direction.getOpposite();

        Direction newDirection = backwards;
        /* While we still haven't found a valid direction */
        while (newDirection.equals(backwards) || !direction.isValidDest(lookX, lookY, this)) {
            /* If we've tried every location, turn around and break the loop */
            if (set.size() == 3) {
                newDirection = backwards;
                break;
            }

            lookX = x;
            lookY = y;

            /* Randomly choose a direction */
            random = (int) (Math.random() * 4) + 1;
            if (random == 1) {
                newDirection = new Left();
                lookX -= increment;
            } else if (random == 2) {
                newDirection = new Right();
                lookX += gridSize;
            } else if (random == 3) {
                newDirection = new Up();
                lookY -= increment;
            } else if (random == 4) {
                newDirection = new Down();
                lookY += gridSize;
            }
            if (!newDirection.equals(backwards)) {
                set.add(newDirection);
            }
        }
        return newDirection;
    }

    /* Determines if the location is one where the ghost has to make a decision*/
    public boolean isChoiceDest() {
        return x % gridSize == 0 && y % gridSize == 0;
    }
}
