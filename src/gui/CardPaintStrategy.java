package gui;

import java.awt.Color;
import java.awt.Graphics;

public class CardPaintStrategy {

	private static final int WIDTH = 100;
	private static final int HEIGHT = 150;

	public void paint(Graphics g, String value, String suit, boolean lifted) {
		int offset = lifted ? 0 : 20;

		g.setColor(Color.WHITE);
		g.fillRect(0, offset, WIDTH, HEIGHT);

		g.setColor(Color.BLACK);
		g.drawRect(0, offset, WIDTH - 1, HEIGHT - 1);

		if (suit.equals("\u2665") || suit.equals("\u2666")) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}

		g.drawString(value, 10, 20 + offset);
		g.drawString(suit, 10, 40 + offset);
		g.drawString(value, WIDTH - 20, HEIGHT - 10 + offset);
	}
}
