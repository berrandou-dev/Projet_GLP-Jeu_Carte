package gui;

import java.awt.*;
import javax.swing.*;

public class MainGUI extends JFrame {

    public MainGUI(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel central pour la carte posée
        JPanel centerPanel = new JPanel(null); // null layout pour placer la carte au centre
        centerPanel.setPreferredSize(new Dimension(800, 200));
        centerPanel.setBackground(new Color(0, 128, 0));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Panel du bas pour les cartes du joueur
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(0, 128, 0));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Création des cartes
        String[] values = {"A","K","Q","J"};
        String[] suits = {"♥","♠","♦","♣"};

        for(int i=0;i<values.length;i++) {
            CardPanel card = new CardPanel(values[i], suits[i], centerPanel);
            bottomPanel.add(card);
        }

        // Ajouter le panel principal à la JFrame
        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainGUI("Jeu Carte");
    }
}

