import java.awt.*;

public class Down implements Direction{

    final Image pacmanDownImage = Toolkit.getDefaultToolkit().getImage("img/pacmandown.jpg");

    @Override
    public void drawImage(Graphics g, Player player) {
        g.drawImage(pacmanDownImage, player.x, player.y, Color.BLACK, null);
    }

    @Override
    public boolean isOpposite(Direction direction) {
        return direction instanceof Up;
    }

    @Override
    public void movePlayer(Player player, int gridSize) {
        if (isValidDest(player.x, player.y + gridSize, player))
            player.y += player.increment;
    }

    @Override
    public Direction getOpposite() {
        return new Left();
    }

    @Override
    public void moveIfValid(Mover mover) {
        if (isValidDest(mover.x, mover.y + mover.gridSize, mover))
            mover.y += mover.increment;
    }
}
