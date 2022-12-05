import java.awt.*;

/* Ghost class controls the ghost. */
class Ghost extends Mover {

    /* The pellet the ghost was last on top of */
    private int lastPelletX;
    private int lastPelletY;

    private final Image looksLeft;
    private final Image looksRight;

    /*Constructor places ghost and updates states*/
    public Ghost(int x, int y, String leftImagePath, String rightImagePath) {
        super(x, y);
        lastPelletX = getPelletX();
        lastPelletY = getPelletY();
        looksLeft = Toolkit.getDefaultToolkit().getImage(leftImagePath);
        looksRight = Toolkit.getDefaultToolkit().getImage(rightImagePath);
    }

    /* update pellet status */
    public void updatePellet() {
        int tempX, tempY;
        tempX = getX() / getGridSize() - 1;
        tempY = getY() / getGridSize() - 1;
        if (tempX != getPelletX() || tempY != getPelletY()) {
            lastPelletX = getPelletX();
            lastPelletY = getPelletY();
            this.setPelletX(tempX);
            this.setPelletY(tempY);
        }

    }

    /* Random movePlayer function for ghost */
    public void move() {
        this.setLastX(getX());
        this.setLastY(getY());

        /* If we can make a decision, pick a new direction randomly */
        if (isChoiceDest()) {
            setDirection(newDirection());
        }

        /* If that direction is valid, movePlayer that way */
        getDirection().moveIfValid(this);
    }

    public void drawLooksToTheRight(Graphics g) {
        g.drawImage(looksRight, getX(), getY(), Color.BLACK, null);
    }

    public void drawLooksToTheLeft(Graphics g) {
        g.drawImage(looksLeft, getX(), getY(), Color.BLACK, null);
    }

    /* Draws one individual pellet.  Used to redraw pellets that ghosts have run over */
    public void fillPellet(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(lastPelletX * 20 + 28, lastPelletY * 20 + 28, 4, 4);
    }

    public int getLastPelletX() {
        return lastPelletX;
    }

    public int getLastPelletY() {
        return lastPelletY;
    }
}