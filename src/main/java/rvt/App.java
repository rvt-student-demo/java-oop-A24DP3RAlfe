package rvt;

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        JFrame logs = new JFrame("Grafika");
        logs.setSize(1024, 768);
        logs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        logs.setLocationRelativeTo(null); // Centrēts
        logs.setVisible(true);

        // logs.add();
    }
}
