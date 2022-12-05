import java.awt.*;
import java.util.LinkedList;

/* This is the pacman object */
class Player extends Mover {
    /* Direction is used in demoMode, currDirection and desiredDirection are used in non demoMode*/
    Direction currDirection;
    Direction desiredDirection;

    /* Keeps track of pellets eaten to determine end of game */
    int pelletsEaten;

    /* teleport is true when travelling through the teleport tunnels*/
    boolean teleport;

    /* Stopped is set when the pacman is not moving or has been killed */
    boolean stopped = false;

    /* Constructor places pacman in initial location and orientation */

    final Image pacmanImage;
    public Player(int x, int y, String pacManImage) {
        super(x, y);

        teleport = false;
        pelletsEaten = 0;
        currDirection = new Left();
        desiredDirection = new Left();
        pacmanImage = Toolkit.getDefaultToolkit().getImage("img/pacman.jpg");
    }

    /* This function is used for demoMode.  It is copied from the Ghost class.  See that for comments */
    public void demoMove() {
        lastX = x;
        lastY = y;
        if (isChoiceDest()) {
            direction = newDirection();
        }
        direction.movePlayer(this, gridSize);
        currDirection = direction;
        frameCount++;
    }

    /* The movePlayer function moves the pacman for one frame in non demo mode */
    public void move() {
        int gridSize = 20;
        lastX = x;
        lastY = y;

        /* Try to turn in the direction input by the user */
        /*Can only turn if we're in center of a grid*/
        if (x % 20 == 0 && y % 20 == 0 ||
                /* Or if we're reversing*/
                (desiredDirection.isOpposite(currDirection))
        ) {
            desiredDirection.moveIfValid(this);
        }
        /* If we haven't moved, then movePlayer in the direction the pacman was headed anyway */
        if (lastX == x && lastY == y) {
            currDirection.movePlayer(this, gridSize);
        }

        /* If we did change direction, update currDirection to reflect that */
        else {
            currDirection = desiredDirection;
        }

        /* If we didn't movePlayer at all, set the stopped flag */
        if (lastX == x && lastY == y)
            stopped = true;

            /* Otherwise, clear the stopped flag and increment the frameCount for animation purposes*/
        else {
            stopped = false;
            frameCount++;
        }
    }

    /* Update what pellet the pacman is on top of */
    public void updatePellet() {
        if (super.isChoiceDest()) {
            pelletX = x / gridSize - 1;
            pelletY = y / gridSize - 1;
        }
    }

    public void drawPacMan(Graphics g){
        g.drawImage(pacmanImage, x, y, Color.BLACK, null);
    }

    public void killPacMan(Graphics g, int width, int height) {
        g.fillRect(x, y, width, height);
    }

    public boolean collidesWith(LinkedList<Ghost> ghosts) {
        for (Ghost ghost: ghosts) {
            if (x == ghost.x && Math.abs(y - ghost.y) < 10)
                return true;
            else if (y == ghost.y && Math.abs(x - ghost.x) < 10)
                return true;
        }
        return false;
    }

    public void eatPellet() {
        pelletsEaten++;
    }
}
