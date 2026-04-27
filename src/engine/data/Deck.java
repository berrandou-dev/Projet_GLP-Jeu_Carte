package engine.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck of 54 cards (52 standard + 2 Jokers).
 */
public class Deck {

    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (Card.Suit suit : new Card.Suit[]{Card.Suit.HEARTS, Card.Suit.SPADES, Card.Suit.DIAMONDS, Card.Suit.CLUBS}) {
            for (Card.Value value : Card.Value.values()) {
                if (value != Card.Value.JOKER) {
                    cards.add(new Card(value, suit));
                }
            }
        }
        cards.add(new Card(Card.Value.JOKER, Card.Suit.JOKER));
        cards.add(new Card(Card.Value.JOKER, Card.Suit.JOKER));
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        return cards.isEmpty() ? null : cards.remove(cards.size() - 1);
    }

    public int size() { return cards.size(); }
    public boolean isEmpty() { return cards.isEmpty(); }
    public List<Card> getCards() { return cards; }
}
