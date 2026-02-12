package gui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CardPanel extends JPanel {

	private String value;
	private String suit;
	private boolean lifted = false;
	private CardPaintStrategy painter = new CardPaintStrategy();
	
	public CardPanel(String value, String suit, JLayeredPane centerPanel) {
		this.value = value;
		this.suit = suit;
		setPreferredSize(new Dimension(100, 170));
		setOpaque(true);

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
				lifted = false;
				repaint();

				if (centerPanel != null) {
					Container oldParent = CardPanel.this.getParent();
					if (oldParent != null) {
						oldParent.remove(CardPanel.this);
						oldParent.revalidate();
						oldParent.repaint();
					}

					Dimension size = CardPanel.this.getPreferredSize();
					int centerX = (centerPanel.getWidth() - size.width) / 2;
					int centerY = (centerPanel.getHeight() - size.height) / 2;
					CardPanel.this.setBounds(centerX, centerY, size.width, size.height);

					centerPanel.add(CardPanel.this, JLayeredPane.PALETTE_LAYER);
					centerPanel.moveToFront(CardPanel.this);

					centerPanel.revalidate();
					centerPanel.repaint();
				}
			}
		});
	}

	@Override
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
