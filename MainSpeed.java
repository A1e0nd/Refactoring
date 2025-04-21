import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainSpeed {


    private static JComboBox combobox;
    private static JRadioButton radiobutton1;
    private static JRadioButton radiobutton2;
    private static JButton button;

    private static class MyPanel extends JPanel {
        private boolean active = false;
        private boolean firstTime = true;

        private int x;
        private int y;
        private int dx;
        private int dy;
        private int speed;

        private final int TIMER_DELAY = 20;
        private final int SHAPE_SIZE = 50;

        private Timer timer;

        public MyPanel() {
            x = 0;
            y = 0;
            dx = 0;
            dy = 0;
            speed = 0;
            timer = new Timer(TIMER_DELAY, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (x < 0 || x > getWidth() - SHAPE_SIZE) {
                        dx = -dx;
                    }
                    if (y < 0 || y > getHeight() - SHAPE_SIZE) {
                        dy = -dy;
                    }

                    x += dx;
                    y += dy;

                    repaint();
                }
            });


        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);


            if (active) {
                g.setColor(Color.BLUE);
                g.drawOval(x, y, SHAPE_SIZE, SHAPE_SIZE);
                g.fillOval(x, y, SHAPE_SIZE, SHAPE_SIZE);
            }


        }

        private void getParameters() {
            speed = Integer.parseInt((String) combobox.getSelectedItem());
            if (firstTime) { //
                x = getWidth() / 2;
                y = getHeight() / 2;
                firstTime = false;
            }

            if (radiobutton1.isSelected()) {
                if (dy > 0) {
                    dy = -speed;
                } else {
                    dy = speed;
                }
            } else {
                if (dy > 0) {
                    dy = speed;
                } else {
                    dy = -speed;
                }
            }
            if (radiobutton2.isSelected()) {
                if (dx > 0) {
                    dx = -speed;
                } else {
                    dx = speed;
                }
            } else {
                if (dx > 0) {
                    dx = speed;
                } else {
                    dx = -speed;
                }
            }
        }

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Moving square");
        frame.setSize(new Dimension(600, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
//        frame.setLayout(new GridBagLayout());

        JFrame frame1 = new JFrame("Choose");
        frame1.setSize(new Dimension(400, 100));
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocationRelativeTo(null);
        frame1.setLayout(new GridBagLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenuBar menuBar1 = new JMenuBar();

        JLabel label = new JLabel("Speed:");
        String str[] = {"1", "2", "3", "4", "5", "10"};
        combobox = new JComboBox(str);
        radiobutton1 = new JRadioButton("Up-Down");
        radiobutton2 = new JRadioButton("Left-Right");
        button = new JButton("Ok");

        frame1.add(label);
        frame1.add(combobox);
        frame1.add(radiobutton1);
        frame1.add(radiobutton2);
        frame1.add(button);


        MyPanel panel = new MyPanel();
        panel.setBackground(Color.white);
        frame.add(panel, BorderLayout.CENTER);


        JMenu menu = new JMenu("Menu");

        JMenuItem showpicture = new JMenuItem("Show picture");
        JMenuItem choose = new JMenuItem("Choose");
        JMenuItem animate = new JMenuItem("Animate");
        JMenuItem stop = new JMenuItem("Stop");
        JMenuItem quit = new JMenuItem("Quit");

        menu.add(showpicture);
        menu.add(choose);
        menu.add(animate);
        menu.add(stop);
        menu.addSeparator();
        menu.add(quit);

        menuBar.add(menu);


        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
//                frame1.setVisible(false);
                panel.getParameters();
                panel.repaint();
            }
        });


        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);

            }
        });


        choose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame1.setJMenuBar(menuBar1);
                frame1.setVisible(true);


            }
        });

        animate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!panel.timer.isRunning()) {
                    panel.timer.start();
                }
                frame.repaint();
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel.timer.isRunning()) {
                    panel.timer.stop();
                }
                frame.repaint();
            }
        });


        showpicture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel.active) {
                    panel.active = false;
                } else {

                    panel.active = true;
                    if (panel.firstTime) {
                        panel.getParameters();
                    }
                }
                frame.repaint();
            }
        });

        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

}