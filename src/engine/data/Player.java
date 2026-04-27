package engine.data;

import java.util.ArrayList;
import java.util.List;
import engine.process.Combination;

/**
 * Represents a player with a hand of cards.
 */
public class Player {

    private String id;
    private List<Card> hand;

    public Player(String id) {
        this.id = id;
        hand = new ArrayList<>();
    }

    public boolean playCombination(Combination c) {
        if (c == null || !c.isValid()) return false;
        List<Card> cardsToPlay = c.getCards();
        if (!handContainsAll(cardsToPlay)) return false;
        for (Card card : cardsToPlay) hand.remove(card);
        return true;
    }

    private boolean handContainsAll(List<Card> cards) {
        List<Card> handCopy = new ArrayList<>(hand);
        for (Card card : cards) {
            if (!handCopy.remove(card)) return false;
        }
        return true;
    }

    public boolean canPlay(Combination c) {
        return c != null && c.isValid() && handContainsAll(c.getCards());
    }

    public void drawCard(Deck deck) {
        if (!deck.isEmpty()) hand.add(deck.draw());
    }

    public void addCardToHand(Card card) { hand.add(card); }
    public boolean hasCard() { return !hand.isEmpty(); }
    public List<Card> getHand() { return new ArrayList<>(hand); }
    public String getId() { return id; }
    public int getHandSize() { return hand.size(); }
}
