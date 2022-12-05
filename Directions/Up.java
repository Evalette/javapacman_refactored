import java.awt.*;

public class Up implements Direction {

    private final Image pacmanUpImage = Toolkit.getDefaultToolkit().getImage("img/pacmanup.jpg");

    @Override
    public void drawImage(Graphics g, Player player) {
        g.drawImage(pacmanUpImage, player.getX(), player.getY(), Color.BLACK, null);
    }

    @Override
    public boolean isOpposite(Direction direction) {
        return direction instanceof Down;
    }

    @Override
    public void movePlayer(Player player, int gridSize) {
        if (isValidDest(player.getX(), player.getY() - player.getIncrement(), player))
            player.setY(player.getY() - player.getIncrement());
    }

    @Override
    public Direction getOpposite() {
        return new Down();
    }

    @Override
    public void moveIfValid(Mover mover) {
        if (mover.getDirection().isValidDest(mover.getX(), mover.getY() - mover.getIncrement(), mover))
            mover.setY(mover.getY() - mover.getIncrement());
    }
}
