import java.awt.*;

public class Screen {

    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private final Image screenImage;
    public Screen(String fileName, boolean isActive) {
        screenImage = Toolkit.getDefaultToolkit().getImage(fileName);
        this.isActive = isActive;
    }


    public void drawImage(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);
        g.drawImage(screenImage, 0, 0, Color.BLACK, null);
    }
}
