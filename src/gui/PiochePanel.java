package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import engine.mobile.Card;
import engine.mobile.Pioche;

public class PiochePanel extends JPanel {

    private Pioche pioche;
    private JPanel bottomPanel;
    private JLayeredPane centerPanel;

    public PiochePanel(
            Pioche pioche,
            JLayeredPane centerPanel,
            JPanel bottomPanel) {

        this.pioche = pioche;
        this.bottomPanel = bottomPanel;
        this.centerPanel = centerPanel;

        setPreferredSize(new Dimension(100, 150));

        setBackground(new Color(0, 0, 128));

        setBorder(
                BorderFactory.createLineBorder(
                        Color.BLACK
                )
        );

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                drawCard();

            }
        });
    }

    private void drawCard() {

        Card card = pioche.draw();

        if (card != null) {

            CardPanel cardPanel =
                    new CardPanel(card, centerPanel);

            bottomPanel.add(cardPanel);

            bottomPanel.revalidate();

            bottomPanel.repaint();

            repaint();

        } else {

            System.out.println("Deck is empty!");

        }
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

        String nb =
                String.valueOf(pioche.size());

        FontMetrics fm =
                g.getFontMetrics();

        int x =
                (w - fm.stringWidth(nb)) / 2;

        int y =
                h / 2 + fm.getAscent() / 2;

        g.drawString(nb, x, y);
    }
}