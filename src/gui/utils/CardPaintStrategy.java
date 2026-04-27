package gui.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import config.GameConfig;

/**
 * Paints a single card: background, border, value, suit.
 */
public class CardPaintStrategy {

    private static final int WIDTH = GameConfig.CARD_WIDTH;
    private static final int HEIGHT = GameConfig.CARD_HEIGHT;

    public void paint(Graphics g, String value, String suit, boolean lifted) {
        int offset = lifted ? 0 : 20;

        // Fond blanc
        g.setColor(GameConfig.CARD_BACKGROUND);
        g.fillRect(0, offset, WIDTH, HEIGHT);

        // Bordure
        g.setColor(Color.BLACK);
        g.drawRect(0, offset, WIDTH - 1, HEIGHT - 1);

        // Couleur selon la carte
        if (suit.equals("\u2665") || suit.equals("\u2666")) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }

        // Police
        g.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, GameConfig.FONT_LARGE));
        
        // Valeur en haut à gauche
        g.drawString(value, 10, 25 + offset);
        g.drawString(suit, 10, 45 + offset);
        
        // Valeur en bas à droite (inversée)
        g.drawString(value, WIDTH - 25, HEIGHT - 10 + offset);
        g.drawString(suit, WIDTH - 25, HEIGHT - 30 + offset);
    }
}
