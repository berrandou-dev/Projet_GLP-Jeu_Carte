package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

public class Pioche {

	private List<CardPanel> cartes;
	private JLayeredPane centerPanel;

	public Pioche(JLayeredPane centerPanel) {
		this.centerPanel = centerPanel;
		cartes = new ArrayList<>();

		String[] suits = { "♠", "♥", "♦", "♣" };
		String[] values = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

		for (String suit : suits) {
			for (String value : values) {
				cartes.add(new CardPanel(value, suit, centerPanel));
			}
		}

		cartes.add(new CardPanel("JOKER", "★", centerPanel));
		cartes.add(new CardPanel("JOKER", "☆", centerPanel));

		melanger();
	}

	public void melanger() {
		Collections.shuffle(cartes);
	}

	public CardPanel piocher() {
		if (!cartes.isEmpty()) {
			return cartes.remove(cartes.size() - 1);
		}
		return null;
	}

	public int taille() {
		return cartes.size();
	}

	public boolean estVide() {
		return cartes.isEmpty();
	}

	public List<CardPanel> getCartes() {
		return cartes;
	}
}
