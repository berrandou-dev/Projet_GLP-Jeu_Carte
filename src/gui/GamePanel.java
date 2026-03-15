package gui;

import javax.swing.*;
import java.awt.*;
import process.*;

public class GamePanel extends JPanel {
    private Game game;
    private JLabel roundLabel;
    private JLabel currentPlayerLabel;
    private JLabel lastCombinationLabel;
    private JButton passButton;
    private JButton drawButton;
    private MainGUI mainGUI;
    
    public GamePanel(Game game, MainGUI mainGUI) {
        this.game = game;
        this.mainGUI = mainGUI;
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 100));
        setBackground(new Color(50, 50, 50));
        
        // Panneau d'information
        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.setBackground(new Color(70, 70, 70));
        
        roundLabel = new JLabel("Round: 1");
        roundLabel.setForeground(Color.WHITE);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        currentPlayerLabel = new JLabel("Joueur: " + game.getCurrentPlayer().getId());
        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        lastCombinationLabel = new JLabel("Dernière combinaison: Aucune");
        lastCombinationLabel.setForeground(Color.WHITE);
        lastCombinationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        infoPanel.add(roundLabel);
        infoPanel.add(currentPlayerLabel);
        infoPanel.add(lastCombinationLabel);
        
        // Panneau de contrôle
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(70, 70, 70));
        
        passButton = new JButton("Passer");
        passButton.setFont(new Font("Arial", Font.BOLD, 14));
        passButton.addActionListener(e -> pass());
        
        drawButton = new JButton("Piocher");
        drawButton.setFont(new Font("Arial", Font.BOLD, 14));
        drawButton.addActionListener(e -> drawCard());
        
        controlPanel.add(passButton);
        controlPanel.add(drawButton);
        
        add(infoPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
    }
    
    private void pass() {
        game.pass();
        updateDisplay();
        if (mainGUI != null) {
            mainGUI.refreshDisplay();
            mainGUI.refreshHand();
        }
    }
    
    private void drawCard() {
        if (game.getCurrentPlayer().getId().equals("Vous")) {
            game.drawCard();
            
            if (mainGUI != null) {
                mainGUI.refreshHand();
                mainGUI.refreshHand();
            }
            
        }
        
        updateDisplay();
    }
    
    public void updateDisplay() {
        roundLabel.setText("Round: " + game.getCurrentRound().getRoundNumber());
        currentPlayerLabel.setText("Joueur: " + game.getCurrentPlayer().getId());
        
        Combination lastCombo = game.getLastCombination();
        if (lastCombo != null) {
            lastCombinationLabel.setText("Dernière: " + lastCombo.toString());
        }
        

        /*boolean isHumanTurn = game.getCurrentPlayer().getId().equals("Vous");
        passButton.setEnabled(isHumanTurn);
        drawButton.setEnabled(isHumanTurn);
        */
        
        repaint();
    }
}
