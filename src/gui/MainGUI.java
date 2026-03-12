package gui;

import java.awt.*;
import javax.swing.*;

import data.*;

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

        //Création de la pioche
        Deck tabledeck = new Deck();
        
        // Création d’un joueur
        Player player = new Player("Laiza");
	
	//DeckPanel
        DeckPanel deckPanel = new DeckPanel(tabledeck, centerPanel, bottomPanel);
        deckPanel.setBounds(20, 20, 100, 150);
        centerPanel.add(deckPanel, JLayeredPane.DEFAULT_LAYER);
        
        // Ajout d’un MouseListener pour piocher pour ce joueur
        deckPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                deckPanel.drawCard(player); // la carte va dans la main et s'affiche
            }
        });

        add(mainPanel);
        setVisible(true);
    }

}
