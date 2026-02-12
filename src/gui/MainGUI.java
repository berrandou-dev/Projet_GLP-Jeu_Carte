package gui;

import java.awt.*;
import javax.swing.*;

public class MainGUI extends JFrame {

	public MainGUI(String title) {
		super(title);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 400);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());

		JLayeredPane centerPanel = new JLayeredPane();
		centerPanel.setPreferredSize(new Dimension(800, 200));
		centerPanel.setBackground(new Color(0, 128, 0));
		centerPanel.setOpaque(true);
		mainPanel.add(centerPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		bottomPanel.setBackground(new Color(0, 128, 0));
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);

		String[] playerValues = { "A", "K" };
		String[] playerSuits = { "♥", "♠" };
		for (int i = 0; i < playerValues.length; i++) {
			CardPanel card = new CardPanel(playerValues[i], playerSuits[i], centerPanel);
			bottomPanel.add(card);
		}

		Pioche pioche = new Pioche(centerPanel);

		PiochePanel piochePanel = new PiochePanel(pioche, centerPanel, bottomPanel);
		piochePanel.setBounds(20, 20, 100, 150);
		centerPanel.add(piochePanel, JLayeredPane.DEFAULT_LAYER);

		add(mainPanel);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainGUI("Jeu Carte");
	}
}
