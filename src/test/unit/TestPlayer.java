package test.unit;

import static org.junit.Assert.*;
import org.junit.Test;
import engine.data.*;
import engine.process.Combination;
import java.util.*;

public class TestPlayer {

    @Test
    public void testPlayerCreation() {
        Player player = new Player("Testeur");
        assertEquals("Testeur", player.getId());
        assertEquals(0, player.getHandSize());
        assertFalse(player.hasCard());
    }

    @Test
    public void testDrawCard() {
        Player player = new Player("Testeur");
        Deck deck = new Deck();
        int initialDeckSize = deck.size();
        
        player.drawCard(deck);
        
        assertEquals(1, player.getHandSize());
        assertEquals(initialDeckSize - 1, deck.size());
    }

    @Test
    public void testPlayValidCombination() {
        Player player = new Player("Testeur");
        
        // Créer un deck personnalisé avec les cartes qu'on veut
        Deck customDeck = new Deck();
        // Vider le deck
        while (!customDeck.isEmpty()) {
            customDeck.draw();
        }
        // Ajouter nos cartes spécifiques
        Card card1 = new Card(Card.Value.KING, Card.Suit.HEARTS);
        Card card2 = new Card(Card.Value.KING, Card.Suit.SPADES);
        customDeck.getCards().add(card1);
        customDeck.getCards().add(card2);
        
        // Piocher les cartes
        player.drawCard(customDeck);
        player.drawCard(customDeck);
        
        assertEquals(2, player.getHandSize());
        
        // Créer la combinaison avec les mêmes cartes
        List<Card> toPlay = new ArrayList<>();
        toPlay.add(card1);
        toPlay.add(card2);
        
        CombinationType type = Combination.determineType(toPlay);
        Combination combo = new Combination(toPlay, type);
        
        assertTrue("canPlay devrait retourner true", player.canPlay(combo));
        assertTrue("playCombination devrait retourner true", player.playCombination(combo));
        assertEquals(0, player.getHandSize());
    }

    @Test
    public void testCannotPlayCardNotInHand() {
        Player player = new Player("Testeur");
        
        // Carte qui n'est PAS dans la main
        Card card = new Card(Card.Value.ACE, Card.Suit.HEARTS);
        
        List<Card> toPlay = Arrays.asList(card);
        CombinationType type = Combination.determineType(toPlay);
        Combination combo = new Combination(toPlay, type);
        
        assertFalse(player.canPlay(combo));
        assertFalse(player.playCombination(combo));
        assertEquals(0, player.getHandSize());
    }

    @Test
    public void testCannotPlayInvalidCombination() {
        Player player = new Player("Testeur");
        
        // Créer un deck avec un joker
        Deck customDeck = new Deck();
        while (!customDeck.isEmpty()) {
            customDeck.draw();
        }
        Card joker = new Card(Card.Value.JOKER, Card.Suit.JOKER);
        customDeck.getCards().add(joker);
        
        player.drawCard(customDeck);
        assertEquals(1, player.getHandSize());
        
        // Joker seul est invalide
        List<Card> toPlay = Arrays.asList(joker);
        CombinationType type = Combination.determineType(toPlay);
        assertEquals(CombinationType.INVALID, type);
        
        Combination combo = new Combination(toPlay, type);
        
        assertFalse(player.canPlay(combo));
        assertFalse(player.playCombination(combo));
        
        // La carte doit toujours être là car la combinaison est invalide
        assertEquals(1, player.getHandSize());
    }

    @Test
    public void testGetHandReturnsCopy() {
        Player player = new Player("Testeur");
        Deck deck = new Deck();
        player.drawCard(deck);
        
        List<Card> hand1 = player.getHand();
        List<Card> hand2 = player.getHand();
        
        assertNotSame(hand1, hand2);
        assertEquals(hand1, hand2);
    }
    
    @Test
    public void testPlaySimpleCombination() {
        Player player = new Player("Testeur");
        
        // Deck personnalisé avec une seule carte
        Deck customDeck = new Deck();
        while (!customDeck.isEmpty()) {
            customDeck.draw();
        }
        Card card = new Card(Card.Value.FIVE, Card.Suit.HEARTS);
        customDeck.getCards().add(card);
        
        player.drawCard(customDeck);
        assertEquals(1, player.getHandSize());
        
        List<Card> toPlay = Arrays.asList(card);
        CombinationType type = Combination.determineType(toPlay);
        assertEquals(CombinationType.SIMPLE, type);
        
        Combination combo = new Combination(toPlay, type);
        
        assertTrue(player.canPlay(combo));
        assertTrue(player.playCombination(combo));
        assertEquals(0, player.getHandSize());
    }
    
    @Test
    public void testDrawWhenDeckEmpty() {
        Player player = new Player("Testeur");
        Deck emptyDeck = new Deck();
        
        // Vider le deck
        for (int i = 0; i < 54; i++) {
            emptyDeck.draw();
        }
        assertTrue(emptyDeck.isEmpty());
        
        int initialSize = player.getHandSize();
        player.drawCard(emptyDeck);
        
        assertEquals(initialSize, player.getHandSize());
    }
    
    @Test
    public void testCannotPlayCombinationWithMissingCard() {
        Player player = new Player("Testeur");
        
        // Ajouter une seule carte à la main via deck custom
        Deck customDeck = new Deck();
        while (!customDeck.isEmpty()) {
            customDeck.draw();
        }
        Card card1 = new Card(Card.Value.KING, Card.Suit.HEARTS);
        customDeck.getCards().add(card1);
        player.drawCard(customDeck);
        
        // Essayer de jouer une combinaison de 2 cartes (il en manque une)
        Card card2 = new Card(Card.Value.KING, Card.Suit.SPADES);
        List<Card> toPlay = Arrays.asList(card1, card2);
        CombinationType type = Combination.determineType(toPlay);
        Combination combo = new Combination(toPlay, type);
        
        assertFalse(player.canPlay(combo));
        assertFalse(player.playCombination(combo));
        assertEquals(1, player.getHandSize());
    }
}
