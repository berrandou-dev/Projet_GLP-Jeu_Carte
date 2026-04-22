package test.unit;

import static org.junit.Assert.*;
import org.junit.Test;
import engine.data.Deck;
import engine.data.Card;

public class TestDeck {

    @Test
    public void testDeckHas54Cards() {
        Deck deck = new Deck();
        assertEquals(54, deck.size());
    }

    @Test
    public void testDrawReducesDeck() {
        Deck deck = new Deck();
        int initialSize = deck.size();
        Card card = deck.draw();
        
        assertNotNull(card);
        assertEquals(initialSize - 1, deck.size());
    }

    @Test
    public void testDeckBecomesEmpty() {
        Deck deck = new Deck();
        for (int i = 0; i < 54; i++) {
            deck.draw();
        }
        assertTrue(deck.isEmpty());
        assertNull(deck.draw());
    }

    @Test
    public void testShuffleChangesOrder() {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();
        
        // Vérifier que les deux decks sont différents (hautement probable)
        boolean different = false;
        for (int i = 0; i < 54; i++) {
            if (!deck1.getCards().get(i).equals(deck2.getCards().get(i))) {
                different = true;
                break;
            }
        }
        assertTrue(different);
    }
}
