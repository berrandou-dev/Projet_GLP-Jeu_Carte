package gui;

import java.awt.*;
import javax.swing.*;
import engine.data.*;
import config.GameConfig;

public class DeckPanel extends JPanel {

    private Deck deck;
    private JPanel bottomPanel;
    private JLayeredPane centerPanel;

    public DeckPanel(Deck deck, JLayeredPane centerPanel, JPanel bottomPanel) {
        this.deck = deck;
        this.bottomPanel = bottomPanel;
        this.centerPanel = centerPanel;

        setPreferredSize(new Dimension(GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT));
        setBackground(GameConfig.DECK_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void drawCard(Player player) {
        if (!deck.isEmpty()) {
            Card cardDrawn = deck.draw();
            player.getHand().add(cardDrawn);

            CardPanel cardPanel = new CardPanel(cardDrawn, centerPanel);
            bottomPanel.add(cardPanel);
            bottomPanel.revalidate();
            bottomPanel.repaint();
            
            repaint();
        } else {
            System.out.println("Deck is empty!");
        }
    }
    
    public void refreshCount() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        g.setColor(new Color(0, 80, 150));
        g.fillRect(0, 0, w, h);

        g.setColor(Color.WHITE);
        g.drawRect(1, 1, w - 3, h - 3);

        g.setColor(new Color(255, 255, 255, 60));
        for (int i = -h; i < w + h; i += 20) {
            g.drawLine(i, 0, i - h, h);
            g.drawLine(i, 0, i + h, h);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        String nb = String.valueOf(deck.size());

        FontMetrics fm = g.getFontMetrics();
        int x = (w - fm.stringWidth(nb)) / 2;
        int y = h / 2 + fm.getAscent() / 2;

        g.drawString(nb, x, y);
    }
}
