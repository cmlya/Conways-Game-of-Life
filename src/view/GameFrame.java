package view;

import javax.swing.*;

public class GameFrame  extends JFrame {
    public GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Conway's Game of Life");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
