package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CardPanel extends JPanel {

    private String value;
    private String suit;
    private boolean lifted = false;
    private CardPaintStrategy painter = new CardPaintStrategy();
    private JPanel centerPanel;

    public CardPanel(String value, String suit, JPanel centerPanel) {
        this.value = value;
        this.suit = suit;
        this.centerPanel = centerPanel;

        setPreferredSize(new Dimension(100, 170));
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { lifted = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { lifted = false; repaint(); }
            @Override
            public void mouseClicked(MouseEvent e) { moveToCenter(); }
        });
    }

    private void moveToCenter() {
        Container parent = getParent();
        if (parent != null) {
            parent.remove(this);
            centerPanel.removeAll();

            Dimension size = getPreferredSize();
            int centerX = (centerPanel.getWidth() - size.width) / 2;
            int centerY = (centerPanel.getHeight() - size.height) / 2;
            setBounds(centerX, centerY, size.width, size.height);

            centerPanel.add(this);
            centerPanel.revalidate();
            centerPanel.repaint();
            parent.revalidate();
            parent.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        painter.paint(g, value, suit, lifted);
    }

    public void setValue(String value) { this.value = value; repaint(); }
    public void setSuit(String suit) { this.suit = suit; repaint(); }
}

