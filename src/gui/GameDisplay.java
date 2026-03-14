package gui;

import javax.swing.*;
import java.awt.*;
import process.Game;
import data.Player;
import process.Combination;

public class GameDisplay extends JPanel {
    private Game game;
    
    public GameDisplay(Game game) {
        this.game = game;
        setBackground(new Color(0, 100, 0));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Afficher les informations des joueurs
        int y = 20;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            String text = player.getId() + ": " + player.getHandSize() + " cartes";
            if (player == game.getCurrentPlayer()) {
                g.setColor(Color.YELLOW);
                g.drawString("→ " + text, 10, y);
            } else {
                g.setColor(Color.WHITE);
                g.drawString(text, 10, y);
            }
            y += 25;
        }
        
        // Afficher la dernière combinaison
        Combination lastCombo = game.getLastCombination();
        if (lastCombo != null) {
            g.setColor(Color.CYAN);
            g.drawString("Dernière combinaison: " + lastCombo.toString(), 
                        10, y + 20);
        }
    }
}
