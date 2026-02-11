package gui;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class CardPanel extends JPanel {

    private String value;
    private String suit;
    private boolean lifted = false; 
    private CardPaintStrategy painter = new CardPaintStrategy();

    public CardPanel(String value, String suit) {
        this.value = value;
        this.suit = suit;

        setSize(100, 150);
        setOpaque(false);

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
            public void mouseClicked(MouseEvent e) {

    int centerX = (getParent().getWidth() - getWidth()) / 2;
    int centerY = (getParent().getHeight() - getHeight()) / 2;

    setLocation(centerX, centerY);
}
        });
    }



    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        painter.paint(g, value, suit, lifted);
    }

    public void setValue(String value) {
        this.value = value;
        repaint();
    }

    public void setSuit(String suit) {
        this.suit = suit;
        repaint();
    }
}
