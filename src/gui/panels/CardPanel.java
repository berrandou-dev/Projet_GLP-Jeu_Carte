package gui.panels;

import engine.data.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import gui.utils.CardPaintStrategy;

public class CardPanel extends JPanel {
    private Card card;
    private boolean lifted = false;
    private boolean selected = false;
    private static List<CardPanel> selectedPanels = new ArrayList<>();
    private CardPaintStrategy paintStrategy;

    public CardPanel(Card card, JLayeredPane centerPanel) {
        this.card = card;
        this.paintStrategy = new CardPaintStrategy();

        setPreferredSize(new Dimension(100, 170));
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lifted = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lifted = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                selected = !selected;
                
                if (selected) {
                    selectedPanels.add(CardPanel.this);
                    setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                } else {
                    selectedPanels.remove(CardPanel.this);
                    setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
                
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Sauvegarder le contexte graphique
        Graphics2D g2d = (Graphics2D) g.create();
        
        
        if (lifted) {
            g2d.translate(0, -10);
        }
        
        
        String value = card.getValue().getSymbol();
        String suit = card.getSuit().getSymbol();
        paintStrategy.paint(g2d, value, suit, lifted);
        
        
        if (selected) {
            g2d.setColor(new Color(255, 255, 0, 100));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.YELLOW);
            g2d.drawRect(2, 2, getWidth()-5, getHeight()-5);
            g2d.drawRect(3, 3, getWidth()-7, getHeight()-7);
        }
        
        g2d.dispose();
    }

    public Card getCard() {
        return card;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public static List<CardPanel> getSelectedPanels() {
        return new ArrayList<>(selectedPanels);
    }
    
    public static void clearSelection() {
        for (CardPanel panel : selectedPanels) {
            panel.selected = false;
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            panel.repaint();
        }
        selectedPanels.clear();
    }
}
