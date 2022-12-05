import java.awt.*;

public class Left implements Direction {

    final Image pacmanLeftImage = Toolkit.getDefaultToolkit().getImage("img/pacmanleft.jpg");

    @Override
    public void drawImage(Graphics g, Player player) {
        g.drawImage(pacmanLeftImage, player.x, player.y, Color.BLACK, null);
    }

    @Override
    public boolean isOpposite(Direction direction) {
        return direction instanceof Right;
    }

    @Override
    public void movePlayer(Player player, int gridSize) {
        if (isValidDest(player.x - player.increment, player.y, player)) {
            player.x -= player.increment;
        } else if (player.y == 9 * gridSize && player.x < 2 * gridSize) {
            player.x = player.max - gridSize;
            player.teleport = true;
        }
    }

    @Override
    public Direction getOpposite() {
        return new Right();
    }

    @Override
    public void moveIfValid(Mover mover) {
        if (mover.direction.isValidDest(mover.x - mover.increment, mover.y, mover))
            mover.x -= mover.increment;
    }


}
