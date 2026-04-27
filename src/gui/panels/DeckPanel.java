package gui.panels;

import java.awt.*;
import javax.swing.*;
import engine.data.*;
import config.GameConfig;

/**
 * Visual representation of the remaining deck with card count.
 */
public class DeckPanel extends JPanel {

    private Deck deck;
    public DeckPanel(Deck deck, JLayeredPane centerPanel, JPanel bottomPanel) {
        this.deck = deck;
        setPreferredSize(new Dimension(GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT));
        setBackground(GameConfig.DECK_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void refreshCount() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        // Fond bleu
        g.setColor(new Color(0, 80, 150));
        g.fillRect(0, 0, w, h);

        // Bordure intérieure
        g.setColor(Color.WHITE);
        g.drawRect(1, 1, w - 3, h - 3);

        // Motif diagonale
        g.setColor(new Color(255, 255, 255, 60));
        for (int i = -h; i < w + h; i += 20) {
            g.drawLine(i, 0, i - h, h);
            g.drawLine(i, 0, i + h, h);
        }

        // Nombre de cartes restantes
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        String nb = deck.isEmpty() ? "Vide" : String.valueOf(deck.size());
        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(nb)) / 2;
        int y = h / 2 + fm.getAscent() / 2;
        g.drawString(nb, x, y);

        // Indication visuelle si vide
        if (deck.isEmpty()) {
            g.setColor(new Color(255, 80, 80, 180));
            g.fillRect(0, 0, w, h);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            String txt = "VIDE";
            fm = g.getFontMetrics();
            g.drawString(txt, (w - fm.stringWidth(txt)) / 2, h / 2 + fm.getAscent() / 2);
        }
    }
}
