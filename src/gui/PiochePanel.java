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

		int w = getWidth();
		int h = getHeight();

		// Fond bleu foncé
		g.setColor(new Color(0, 80, 150));
		g.fillRect(0, 0, w, h);

		// Bordure blanche
		g.setColor(Color.WHITE);
		g.drawRect(1, 1, w - 3, h - 3);

		// Motif croisé simple (diagonales)
		g.setColor(new Color(255, 255, 255, 60));
		for (int i = -h; i < w + h; i += 20) {
			g.drawLine(i, 0, i - h, h);
			g.drawLine(i, 0, i + h, h);
		}

		// Nombre de cartes restantes
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 18));
		String nb = String.valueOf(pioche.taille());
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(nb)) / 2;
		int y = h / 2 + fm.getAscent() / 2;
		g.drawString(nb, x, y);
	}
}
