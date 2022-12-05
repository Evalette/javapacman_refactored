import java.awt.*;

public class Right implements Direction {

    private final Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg");

    @Override
    public void drawImage(Graphics g, Player player) {
        g.drawImage(pacmanRightImage, player.getX(), player.getY(), Color.BLACK, null);
    }

    @Override
    public boolean isOpposite(Direction direction) {
        return direction instanceof Left;
    }

    @Override
    public void movePlayer(Player player, int gridSize) {
        if (isValidDest(player.getX() + gridSize, player.getY(), player)) {
            player.setX(player.getX() + player.getIncrement());
        } else if (player.getY() == 9 * gridSize && player.getX() > player.getMax() - gridSize * 2) {
            player.setX(gridSize);
            player.setTeleport(true);
        }
    }

    @Override
    public Direction getOpposite() {
        return new Left();
    }

    @Override
    public void moveIfValid(Mover mover) {
        if (mover.getDirection().isValidDest(mover.getX() + mover.getGridSize(), mover.getY(), mover))
            mover.setX(mover.getX() + mover.getIncrement());
    }


}
