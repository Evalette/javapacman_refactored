import java.awt.*;

public class Left implements Direction {

    private final Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg");

    @Override
    public void drawImage(Graphics g, Player player) {
        g.drawImage(pacmanLeftImage, player.getX(), player.getY(), Color.BLACK, null);
    }

    @Override
    public boolean isOpposite(Direction direction) {
        return direction instanceof Right;
    }

    @Override
    public void movePlayer(Player player, int gridSize) {
        if (isValidDest(player.getX() - player.getIncrement(), player.getY(), player)) {
            player.setX(player.getX() - player.getIncrement());
        } else if (player.getY() == 9 * gridSize && player.getX() < 2 * gridSize) {
            player.setX(player.getMax() - gridSize);
            player.setTeleport(true);
        }
    }

    @Override
    public Direction getOpposite() {
        return new Right();
    }

    @Override
    public void moveIfValid(Mover mover) {
        if (mover.getDirection().isValidDest(mover.getX() - mover.getIncrement(), mover.getY(), mover))
            mover.setX(mover.getX() - mover.getIncrement());
    }


}
