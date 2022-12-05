import java.awt.*;

public class Up implements Direction {

    final Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg");

    @Override
    public void drawImage(Graphics g, Player player) {
        g.drawImage(pacmanUpImage, player.x, player.y, Color.BLACK, null);
    }

    @Override
    public boolean isOpposite(Direction direction) {
        return direction instanceof Down;
    }

    @Override
    public void movePlayer(Player player, int gridSize) {
        if (isValidDest(player.x, player.y - player.increment, player))
            player.y -= player.increment;
    }

    @Override
    public Direction getOpposite() {
        return new Down();
    }

    @Override
    public void moveIfValid(Mover mover) {
        if (mover.direction.isValidDest(mover.x, mover.y - mover.increment, mover))
            mover.y -= mover.increment;
    }
}
