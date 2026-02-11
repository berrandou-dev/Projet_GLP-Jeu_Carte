package gui;

import java.awt.Dimension;


import javax.swing.*;

public class MainGUI extends JFrame {

    private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(800, 400);

    /**
     * @param title
     */
    public MainGUI(String title) {
        super(title);

        setSize(IDEAL_MAIN_DIMENSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(null);

        CardPanel card1 = new CardPanel("A", "♥");
        CardPanel card2 = new CardPanel("K", "♠");

        card1.setBounds(300,210, 150, 200);
        card2.setBounds(400, 210, 150, 200);
        add(card1);
        add(card2);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainGUI("Jeu Carte");
    }
}


