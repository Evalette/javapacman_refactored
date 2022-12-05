import java.awt.*;

public class Right implements Direction {

    final Image pacmanRightImage = Toolkit.getDefaultToolkit().getImage("img/pacmanright.jpg");

    @Override
    public void drawImage(Graphics g, Player player) {
        g.drawImage(pacmanRightImage, player.x, player.y, Color.BLACK, null);
    }

    @Override
    public boolean isOpposite(Direction direction) {
        return direction instanceof Left;
    }

    @Override
    public void movePlayer(Player player, int gridSize) {
        if (isValidDest(player.x + gridSize, player.y, player)) {
            player.x += player.increment;
        } else if (player.y == 9 * gridSize && player.x > player.max - gridSize * 2) {
            player.x = gridSize;
            player.teleport = true;
        }
    }

    @Override
    public Direction getOpposite() {
        return new Left();
    }

    @Override
    public void moveIfValid(Mover mover) {
        if (mover.direction.isValidDest(mover.x + mover.gridSize, mover.y, mover))
            mover.x += mover.increment;
    }


}
