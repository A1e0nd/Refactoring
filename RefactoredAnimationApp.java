import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class RefactoredAnimationApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RefactoredAnimationApp().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Animated Shapes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        AnimationSettings settings = new AnimationSettings();
        AnimationPanel animationPanel = new AnimationPanel(settings);
        ControlPanel controlPanel = new ControlPanel(settings, animationPanel);

        frame.setLayout(new BorderLayout());
        frame.add(animationPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    static class AnimationSettings {
        int speed = 2;
        Direction direction = Direction.RIGHT;
        ShapeType shapeType = ShapeType.CIRCLE;
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT, RANDOM
    }

    enum ShapeType {
        CIRCLE, SQUARE, TRIANGLE
    }

    static class ControlPanel extends JPanel {
        public ControlPanel(AnimationSettings settings, AnimationPanel panel) {
            setLayout(new FlowLayout());

            add(new JLabel("Speed:"));
            JComboBox<Integer> speedBox = new JComboBox<>(new Integer[]{1, 2, 3, 5, 10});
            speedBox.setSelectedItem(settings.speed);
            speedBox.addActionListener(e -> settings.speed = (Integer) speedBox.getSelectedItem());
            add(speedBox);

            add(new JLabel("Direction:"));
            JComboBox<Direction> directionBox = new JComboBox<>(Direction.values());
            directionBox.setSelectedItem(settings.direction);
            directionBox.addActionListener(e -> settings.direction = (Direction) directionBox.getSelectedItem());
            add(directionBox);

            add(new JLabel("Shape:"));
            JComboBox<ShapeType> shapeBox = new JComboBox<>(ShapeType.values());
            shapeBox.setSelectedItem(settings.shapeType);
            shapeBox.addActionListener(e -> settings.shapeType = (ShapeType) shapeBox.getSelectedItem());
            add(shapeBox);

            JButton startButton = new JButton("Start");
            startButton.addActionListener(e -> panel.startAnimation());
            add(startButton);

            JButton stopButton = new JButton("Stop");
            stopButton.addActionListener(e -> panel.stopAnimation());
            add(stopButton);

            JButton toggleButton = new JButton("Show/Hide");
            toggleButton.addActionListener(e -> panel.toggleVisibility());
            add(toggleButton);
        }
    }

    static class AnimationPanel extends JPanel {
        private static final int SHAPE_SIZE = 50;
        private final AnimationSettings settings;
        private final Timer timer;
        private final Random random = new Random();

        private int x = 100;
        private int y = 100;
        private int dx = 2;
        private int dy = 2;
        private boolean visible = true;
        private Color currentColor = Color.BLUE;

        public AnimationPanel(AnimationSettings settings) {
            this.settings = settings;
            setBackground(Color.WHITE);

            timer = new Timer(20, e -> {
                move();
                repaint();
            });
        }

        public void startAnimation() {
            if (settings.direction == Direction.RANDOM) {
                dx = random.nextInt(7) - 3;
                dy = random.nextInt(7) - 3;
                if (dx == 0 && dy == 0) dx = 2;
            }
            if (!timer.isRunning()) {
                timer.start();
            }
        }

        public void stopAnimation() {
            if (timer.isRunning()) {
                timer.stop();
            }
        }

        public void toggleVisibility() {
            visible = !visible;
            repaint();
        }

        private void move() {
            if (settings.direction == Direction.RANDOM) {
                x += dx;
                y += dy;

                boolean bounced = false;

                if (x < 0 || x > getWidth() - SHAPE_SIZE) {
                    dx = -dx;
                    dy = random.nextInt(7) - 3;
                    bounced = true;
                }

                if (y < 0 || y > getHeight() - SHAPE_SIZE) {
                    dy = -dy;
                    dx = random.nextInt(7) - 3;
                    bounced = true;
                }

                if (bounced) {
                    currentColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                }

            } else {
                int speed = settings.speed;
                switch (settings.direction) {
                    case UP -> y -= speed;
                    case DOWN -> y += speed;
                    case LEFT -> x -= speed;
                    case RIGHT -> x += speed;
                }

                if (x < 0 || x > getWidth() - SHAPE_SIZE) {
                    settings.direction = (settings.direction == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
                }

                if (y < 0 || y > getHeight() - SHAPE_SIZE) {
                    settings.direction = (settings.direction == Direction.UP) ? Direction.DOWN : Direction.UP;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!visible) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(currentColor);

            switch (settings.shapeType) {
                case CIRCLE -> g2.fillOval(x, y, SHAPE_SIZE, SHAPE_SIZE);
                case SQUARE -> g2.fillRect(x, y, SHAPE_SIZE, SHAPE_SIZE);
                case TRIANGLE -> drawTriangle(g2);
            }
        }

        private void drawTriangle(Graphics2D g2) {
            int[] xPoints = {x + SHAPE_SIZE / 2, x, x + SHAPE_SIZE};
            int[] yPoints = {y, y + SHAPE_SIZE, y + SHAPE_SIZE};
            g2.fillPolygon(xPoints, yPoints, 3);
        }
    }
}
