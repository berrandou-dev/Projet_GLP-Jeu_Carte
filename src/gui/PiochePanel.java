package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PiochePanel extends JPanel {

	private Pioche pioche;
	private JLayeredPane centerPanel;
	private JPanel bottomPanel;

	public PiochePanel(Pioche pioche, JLayeredPane centerPanel, JPanel bottomPanel) {
		this.pioche = pioche;
		this.centerPanel = centerPanel;
		this.bottomPanel = bottomPanel;

		setPreferredSize(new Dimension(100, 150));
		setBackground(new Color(0, 0, 128));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				piocherCarte();
			}
		});
	}

	private void piocherCarte() {
		CardPanel carte = pioche.piocher();
		if (carte != null) {
			Container parent = carte.getParent();
			if (parent != null) {
				parent.remove(carte);
				parent.revalidate();
				parent.repaint();
			}

			bottomPanel.add(carte);
			bottomPanel.revalidate();
			bottomPanel.repaint();
			repaint();
		} else {
			System.out.println("La pioche est vide !");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.WHITE);
		g.drawString("" + pioche.taille(), getWidth() / 2 - 5, getHeight() / 2);
	}
}
