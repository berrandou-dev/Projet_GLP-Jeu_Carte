package test.unit;

import static org.junit.Assert.*;
import org.junit.Test;
import engine.data.Card;

public class TestCard {

    @Test
    public void testCardCreation() {
        Card card = new Card(Card.Value.ACE, Card.Suit.HEARTS);
        assertEquals(Card.Value.ACE, card.getValue());
        assertEquals(Card.Suit.HEARTS, card.getSuit());
    }

    @Test
    public void testCardSymbols() {
        Card heart = new Card(Card.Value.TWO, Card.Suit.HEARTS);
        Card joker = new Card(Card.Value.JOKER, Card.Suit.JOKER);
        
        assertEquals("2", heart.getValue().getSymbol());
        assertEquals("\u2665", heart.getSuit().getSymbol());
        assertEquals("JOKER", joker.getValue().getSymbol());
        assertEquals("\u2605", joker.getSuit().getSymbol());
    }

    @Test
    public void testCardEquality() {
        Card card1 = new Card(Card.Value.KING, Card.Suit.CLUBS);
        Card card2 = new Card(Card.Value.KING, Card.Suit.CLUBS);
        Card card3 = new Card(Card.Value.KING, Card.Suit.HEARTS);
        
        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }

    @Test
    public void testJokerCard() {
        Card joker = new Card(Card.Value.JOKER, Card.Suit.JOKER);
        assertEquals(Card.Value.JOKER, joker.getValue());
        assertEquals(Card.Suit.JOKER, joker.getSuit());
    }
}
