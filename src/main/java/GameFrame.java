import javax.swing.*;
import java.util.Objects;

public class GameFrame extends JFrame {
    public GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("/snake.png")));
        this.setIconImage(img.getImage());
    }
}