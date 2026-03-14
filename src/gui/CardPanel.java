package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import data.*;
import java.util.ArrayList;
import java.util.List;

public class CardPanel extends JPanel {
    private Card card;
    private boolean lifted = false;
    private boolean selected = false;
    private static List<CardPanel> selectedPanels = new ArrayList<>();


    public CardPanel(Card card, JLayeredPane centerPanel) {
        this.card = card;

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
                // Inverser la sélection
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
        
        // Fond blanc pour la carte
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Si survolée
        if (lifted) {
            g.setColor(new Color(255, 255, 255, 200));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        
        // Si sélectionnée
        if (selected) {
            g.setColor(new Color(255, 255, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.YELLOW);
            g.drawRect(2, 2, getWidth()-5, getHeight()-5);
            g.drawRect(3, 3, getWidth()-7, getHeight()-7);
        }
        
        // Dessiner la carte
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Valeur et couleur
        String value = card.getValue().getSymbol();
        String suit = card.getSuit().getSymbol();
        
        // Changer la couleur selon la couleur
        if (suit.equals("♥") || suit.equals("♦")) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
        
        // Dessiner en haut à gauche
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(value, 10, 25);
        g.drawString(suit, 10, 45);
        
        // Dessiner en bas à droite (inversé)
        g.drawString(value, getWidth() - 25, getHeight() - 15);
        g.drawString(suit, getWidth() - 25, getHeight() - 35);
        
        // Bordure
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);
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
