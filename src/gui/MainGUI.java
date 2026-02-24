package gui;

import java.awt.*;
import javax.swing.*;
import engine.mobile.Pioche;
public class MainGUI extends JFrame {

    public MainGUI(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

       
        JPanel mainPanel = new JPanel(new BorderLayout());

       
        JLayeredPane centerPanel = new JLayeredPane();
        centerPanel.setPreferredSize(new Dimension(800, 200));
        centerPanel.setBackground(new Color(0, 128, 0));
        centerPanel.setOpaque(true);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // main du joueur
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(0, 128, 0));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        
        Pioche pioche = new Pioche();

        PiochePanel piochePanel = new PiochePanel(pioche, centerPanel, bottomPanel);
        piochePanel.setBounds(20, 20, 100, 150);
        centerPanel.add(piochePanel, JLayeredPane.DEFAULT_LAYER);

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainGUI("Card Game");
    }
}