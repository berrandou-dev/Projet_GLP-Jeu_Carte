package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();

        // 52 cartes normales
        for (Card.Suit suit : new Card.Suit[]{
                Card.Suit.HEARTS,
                Card.Suit.SPADES,
                Card.Suit.DIAMONDS,
                Card.Suit.CLUBS
        }) {
            for (Card.Value value : new Card.Value[]{
                    Card.Value.ACE, Card.Value.KING, Card.Value.QUEEN,
                    Card.Value.JACK, Card.Value.TEN, Card.Value.NINE,
                    Card.Value.EIGHT, Card.Value.SEVEN, Card.Value.SIX,
                    Card.Value.FIVE, Card.Value.FOUR, Card.Value.THREE,
                    Card.Value.TWO
            }) {
                cards.add(new Card(value, suit));
            }
        }

        // Ajouter 2 jokers séparément
        cards.add(new Card(Card.Value.JOKER, Card.Suit.JOKER));
        cards.add(new Card(Card.Value.JOKER, Card.Suit.JOKER));

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (!cards.isEmpty()) return cards.remove(cards.size() - 1);
        return null;
    }

    public int size() {
		 return cards.size(); 
		
		}

    public boolean isEmpty() { 
		return cards.isEmpty(); 
	}

    public List<Card> getCards() { 
		return cards; 
	}
}
