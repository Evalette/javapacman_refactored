import java.awt.*;

public class Down implements Direction {

    private final Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg");

    @Override
    public void drawImage(Graphics g, Player player) {
        g.drawImage(pacmanDownImage, player.getX(), player.getY(), Color.BLACK, null);
    }

    @Override
    public boolean isOpposite(Direction direction) {
        return direction instanceof Up;
    }

    @Override
    public void movePlayer(Player player, int gridSize) {
        if (isValidDest(player.getX(), player.getY() + gridSize, player))
            player.setY(player.getY() + player.getIncrement());
    }

    @Override
    public Direction getOpposite() {
        return new Left();
    }

    @Override
    public void moveIfValid(Mover mover) {
        if (isValidDest(mover.getX(), mover.getY() + mover.getGridSize(), mover))
            mover.setY(mover.getY() + mover.getIncrement());
    }
}
