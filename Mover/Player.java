import java.awt.*;
import java.util.LinkedList;

/* This is the pacman object */
class Player extends Mover {
    /* Direction is used in demoMode, currDirection and desiredDirection are used in non demoMode*/
    private Direction currDirection;
    private Direction desiredDirection;

    /* Keeps track of pellets eaten to determine end of game */
    private int pelletsEaten;

    /* teleport is true when travelling through the teleport tunnels*/
    private boolean teleport;

    /* Stopped is set when the pacman is not moving or has been killed */
    private boolean stopped = false;

    /* Constructor places pacman in initial location and orientation */

    private final Image pacmanImage;
    public Player(int x, int y, String pacManImage) {
        super(x, y);

        teleport = false;
        pelletsEaten = 0;
        currDirection = new Left();
        desiredDirection = new Left();
        pacmanImage = Toolkit.getDefaultToolkit().getImage(pacManImage);
    }

    /* This function is used for demoMode.  It is copied from the Ghost class.  See that for comments */
    public void demoMove() {
        this.setLastX(getX());
        this.setLastY(getY());
        if (isChoiceDest()) {
            setDirection(newDirection());
        }
        getDirection().movePlayer(this, getGridSize());
        currDirection = getDirection();
        setFrameCount(getFrameCount() + 1);
    }

    /* The movePlayer function moves the pacman for one frame in non demo mode */
    public void move() {
        int gridSize = 20;
        this.setLastX(getX());
        this.setLastY(getY());

        /* Try to turn in the direction input by the user */
        /*Can only turn if we're in center of a grid*/
        if (getX() % 20 == 0 && getY() % 20 == 0 ||
                /* Or if we're reversing*/
                (desiredDirection.isOpposite(currDirection))
        ) {
            desiredDirection.moveIfValid(this);
        }
        /* If we haven't moved, then movePlayer in the direction the pacman was headed anyway */
        if (getLastX() == getX() && getLastY() == getY()) {
            currDirection.movePlayer(this, gridSize);
        }

        /* If we did change direction, update currDirection to reflect that */
        else {
            currDirection = desiredDirection;
        }

        /* If we didn't movePlayer at all, set the stopped flag */
        if (getLastX() == getX() && getLastY() == getY())
            stopped = true;

            /* Otherwise, clear the stopped flag and increment the frameCount for animation purposes*/
        else {
            stopped = false;
            setFrameCount(getFrameCount() + 1);
        }
    }

    /* Update what pellet the pacman is on top of */
    public void updatePellet() {
        if (super.isChoiceDest()) {
            this.setPelletX(getX() / getGridSize() - 1);
            this.setPelletY(getY() / getGridSize() - 1);
        }
    }

    public void drawPacMan(Graphics g){
        g.drawImage(pacmanImage, getX(), getY(), Color.BLACK, null);
    }

    public void killPacMan(Graphics g, int width, int height) {
        g.fillRect(getX(), getY(), width, height);
    }

    public boolean collidesWith(LinkedList<Ghost> ghosts) {
        for (Ghost ghost: ghosts) {
            if (getX() == ghost.getX() && Math.abs(getY() - ghost.getY()) < 10)
                return true;
            else if (getY() == ghost.getY() && Math.abs(getX() - ghost.getX()) < 10)
                return true;
        }
        return false;
    }

    public void eatPellet() {
        pelletsEaten++;
    }

    public Direction getCurrDirection() {
        return currDirection;
    }

    public void setCurrDirection(Direction currDirection) {
        this.currDirection = currDirection;
    }

    public void setDesiredDirection(Direction desiredDirection) {
        this.desiredDirection = desiredDirection;
    }

    public int getPelletsEaten() {
        return pelletsEaten;
    }

    public boolean isTeleport() {
        return teleport;
    }

    public void setTeleport(boolean teleport) {
        this.teleport = teleport;
    }

    public boolean isStopped() {
        return stopped;
    }
}
