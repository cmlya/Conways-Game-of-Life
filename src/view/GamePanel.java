package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 20;
    static final int HEIGHT_UNITS = SCREEN_HEIGHT/UNIT_SIZE;
    static final int WIDTH_UNITS = SCREEN_WIDTH/UNIT_SIZE;
    static final int DELAY = 75;
    int[][] cells = new int[HEIGHT_UNITS][WIDTH_UNITS];
    boolean running = false;
    Timer timer;
    Color backgroundColor = new Color(143, 122, 184);
    Color gridColor = new Color(111, 111, 111);
    Color livingCellColor = new Color(113, 17, 72);
    JButton startButton = new JButton("Start");
    Color startButtonColor = new Color(50, 225, 226);

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(backgroundColor);
        this.setFocusable(true);
        startButton.setBackground(startButtonColor);
        startButton.setForeground(Color.white);
        startButton.addActionListener(e -> startGame());
        this.add(startButton);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Graphics g = getGraphics();
                g.setColor(livingCellColor);
                if (!running) {
                    System.out.println(translateToCell(e.getY()) + ", " + translateToCell(e.getX()));
                    if (cells[translateToCell(e.getY())][translateToCell(e.getX())] == 1) {
                        cells[translateToCell(e.getY())][translateToCell(e.getX())] = 0;
                        g.setColor(backgroundColor);
                    }
                    else cells[translateToCell(e.getY())][translateToCell(e.getX())] = 1;
                    g.fillRect(getCellX(translateToCell(e.getX())), getCellY(translateToCell(e.getY())), UNIT_SIZE, UNIT_SIZE);
                }
                g.dispose();
            }
        });
    }

    public void startGame() {
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        boolean hasLive = false;
        // draw grid:
        g.setColor(gridColor);
        for (int i = 0; i < WIDTH_UNITS; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
        if (running) {
            // drawing living cells
            for (int i = 0; i < HEIGHT_UNITS; i++)
                for (int j = 0; j < WIDTH_UNITS; j++)
                    if (cells[i][j] == 1) {
                        hasLive = true;
                        g.setColor(livingCellColor);
                        g.fillRect(getCellX(j), getCellY(i), UNIT_SIZE, UNIT_SIZE);
                    }
        }
        if (!hasLive && running) gameOver(g);
    }

    public void nextGeneration() {
        int neighborsLiving;
        int[][] newCells = new int[HEIGHT_UNITS][WIDTH_UNITS];
        for (int row = 0; row < HEIGHT_UNITS; row++)
            for (int column = 0; column < WIDTH_UNITS; column++) {
                neighborsLiving = 0;
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        if (row + i >= 0 && row + i < HEIGHT_UNITS && column + j >= 0 && column + j < WIDTH_UNITS)
                            neighborsLiving += cells[row + i][column + j];

                neighborsLiving -= cells[row][column];

                if (cells[row][column] == 1 && (neighborsLiving == 2||neighborsLiving == 3))
                    newCells[row][column] = 1;
                else if (neighborsLiving == 3) newCells[row][column] = 1;
                else newCells[row][column] = 0;
            }
        cells = newCells;
    }

    public void gameOver (Graphics g) {
        g.setColor(new Color(136, 26, 201));
        g.setFont(new Font("Avenir", Font.PLAIN, 40));
        g.setColor(Color.magenta);
        g.setFont(new Font("Avenir", Font.PLAIN, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
    }

    public int getCellX(int i) { return i * UNIT_SIZE; }
    public int getCellY(int j) { return j * UNIT_SIZE; }
    public int translateToCell(int n) { return n/UNIT_SIZE; }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) nextGeneration();
        repaint();
    }
}
