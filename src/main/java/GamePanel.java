import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 80;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 1;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = false;
    boolean paused = false;
    Timer timer;
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if (running) {
            graphics.setColor(Color.RED);
            graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.GREEN);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    graphics.setColor(new Color(45, 180, 0));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            String scoreString = "Score: " + foodEaten;
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString(
                    scoreString,
                    0,
                    graphics.getFont().getSize()
            );

            if (paused) {
                String pausedString = "Paused";
                graphics.setColor(Color.YELLOW);
                graphics.setFont(new Font("Comic Sans MS", Font.PLAIN, 75));
                FontMetrics pausedMetrics = getFontMetrics(graphics.getFont());
                graphics.drawString(
                        pausedString,
                        (SCREEN_WIDTH - pausedMetrics.stringWidth(pausedString)) / 2,
                        SCREEN_HEIGHT / 2
                );
            }
        } else {
            gameOver(graphics);
        }
    }

    public void newFood() {
        foodX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    public void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollisions() {
        // Checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        // Checks if head collides with borders
        if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
        String scoreString = "Score: " + foodEaten;
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        graphics.drawString(
                scoreString,
                0,
                graphics.getFont().getSize()
        );

        String gameOverMessage = "Game over :(";
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Comic Sans MS", Font.PLAIN, 75));
        FontMetrics messageMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString(
                gameOverMessage,
                (SCREEN_WIDTH - messageMetrics.stringWidth(gameOverMessage)) / 2,
                SCREEN_HEIGHT / 2
        );

        String restartMessage = "Press Enter to Restart";
        graphics.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
        FontMetrics restartMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString(
                restartMessage,
                (SCREEN_WIDTH - restartMetrics.stringWidth(restartMessage)) / 2,
                SCREEN_HEIGHT / 2 + 50
        );
    }

    public void resetGame() {
        Arrays.fill(x, 0); // Reset snake positions
        Arrays.fill(y, 0);
        bodyParts = 1;
        foodEaten = 0;
        direction = 'R';
        running = false;
        paused = false;
        timer.stop();
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !paused) {
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> {
                if (direction != 'R') {
                    direction = 'L';
                }
            }

            case KeyEvent.VK_RIGHT -> {
                if (direction != 'L') {
                    direction = 'R';
                }
            }

            case KeyEvent.VK_UP -> {
                if (direction != 'D') {
                    direction = 'U';
                }
            }

            case KeyEvent.VK_DOWN -> {
                if (direction != 'U') {
                    direction = 'D';
                }
            }

            case KeyEvent.VK_SPACE -> {
                paused = !paused;
                if (paused) {
                    timer.stop();
                } else {
                    timer.start();
                }
                repaint();
            }

            case KeyEvent.VK_ENTER -> {
                if (!running) {
                    resetGame();
                }
            }
        }
    }

    // Redundant method
    @Override
    public void keyReleased(KeyEvent e) {}

    // Redundant method
    @Override
    public void keyTyped(KeyEvent e) {}
}