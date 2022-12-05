import java.awt.*;

/* Ghost class controls the ghost. */
class Ghost extends Mover {

    /* The pellet the ghost was last on top of */
    int lastPelletX, lastPelletY;

    final Image looksLeft;
    final Image looksRight;

    /*Constructor places ghost and updates states*/
    public Ghost(int x, int y, String leftImagePath, String rightImagePath) {
        super(x, y);
        lastPelletX = pelletX;
        lastPelletY = pelletY;
        looksLeft = Toolkit.getDefaultToolkit().getImage(leftImagePath);
        looksRight = Toolkit.getDefaultToolkit().getImage(rightImagePath);
    }

    /* update pellet status */
    public void updatePellet() {
        int tempX, tempY;
        tempX = x / gridSize - 1;
        tempY = y / gridSize - 1;
        if (tempX != pelletX || tempY != pelletY) {
            lastPelletX = pelletX;
            lastPelletY = pelletY;
            pelletX = tempX;
            pelletY = tempY;
        }

    }

    /* Random movePlayer function for ghost */
    public void move() {
        lastX = x;
        lastY = y;

        /* If we can make a decision, pick a new direction randomly */
        if (isChoiceDest()) {
            direction = newDirection();
        }

        /* If that direction is valid, movePlayer that way */
        direction.moveIfValid(this);
    }

    public void drawLooksToTheRight(Graphics g) {
        g.drawImage(looksRight, x, y, Color.BLACK, null);
    }

    public void drawLooksToTheLeft(Graphics g) {
        g.drawImage(looksLeft, x, y, Color.BLACK, null);
    }

    /* Draws one individual pellet.  Used to redraw pellets that ghosts have run over */
    public void fillPellet(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(lastPelletX * 20 + 28, lastPelletY * 20 + 28, 4, 4);
    }
}
