package test.unit;

import static org.junit.Assert.*;
import org.junit.Test;
import engine.data.GameStats;
import engine.process.Combination;
import engine.data.*;
import java.util.*;

public class TestGameStats {

    @Test
    public void testRegisterPlayer() {
        GameStats stats = new GameStats();
        stats.registerPlayer("Joueur1");
        stats.registerPlayer("Joueur2");
        
        assertNotNull(stats.getStats("Joueur1"));
        assertNotNull(stats.getStats("Joueur2"));
        assertEquals(2, stats.getAllStats().size());
    }

    @Test
    public void testScoreSimpleCard() {
        GameStats stats = new GameStats();
        stats.registerPlayer("Joueur1");
        
        List<Card> cards = Arrays.asList(new Card(Card.Value.FIVE, Card.Suit.HEARTS));
        Combination combo = new Combination(cards, CombinationType.SIMPLE);
        stats.recordCombination("Joueur1", combo);
        
        GameStats.PlayerStats ps = stats.getStats("Joueur1");
        assertEquals(1, ps.getCardsPlayed());
        assertEquals(1, ps.getScore());
        assertEquals(1, ps.getTurnsPlayed());
        assertEquals(0, ps.getBombsPlayed());
        assertEquals(0, ps.getJokersPlayed());
    }

    @Test
    public void testScoreBomb() {
        GameStats stats = new GameStats();
        stats.registerPlayer("Joueur1");
        
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.SEVEN, Card.Suit.HEARTS),
            new Card(Card.Value.SEVEN, Card.Suit.SPADES),
            new Card(Card.Value.SEVEN, Card.Suit.CLUBS)
        );
        Combination combo = new Combination(cards, CombinationType.BOMB);
        stats.recordCombination("Joueur1", combo);
        
        GameStats.PlayerStats ps = stats.getStats("Joueur1");
        assertEquals(3, ps.getCardsPlayed());
        assertEquals(6, ps.getScore());
        assertEquals(1, ps.getBombsPlayed());
    }

    @Test
    public void testScoreDoubleJoker() {
        GameStats stats = new GameStats();
        stats.registerPlayer("Joueur1");
        
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.JOKER, Card.Suit.JOKER),
            new Card(Card.Value.JOKER, Card.Suit.JOKER)
        );
        Combination combo = new Combination(cards, CombinationType.DOUBLE_JOKER);
        stats.recordCombination("Joueur1", combo);
        
        GameStats.PlayerStats ps = stats.getStats("Joueur1");
        assertEquals(2, ps.getCardsPlayed());
        assertEquals(8, ps.getScore());
        assertEquals(1, ps.getJokersPlayed());
    }

    @Test
    public void testMultipleCombinationsAccumulate() {
        GameStats stats = new GameStats();
        stats.registerPlayer("Joueur1");
        
        List<Card> simple = Arrays.asList(new Card(Card.Value.TWO, Card.Suit.HEARTS));
        stats.recordCombination("Joueur1", new Combination(simple, CombinationType.SIMPLE));
        
        List<Card> bomb = Arrays.asList(
            new Card(Card.Value.THREE, Card.Suit.HEARTS),
            new Card(Card.Value.THREE, Card.Suit.SPADES),
            new Card(Card.Value.THREE, Card.Suit.CLUBS)
        );
        stats.recordCombination("Joueur1", new Combination(bomb, CombinationType.BOMB));
        
        GameStats.PlayerStats ps = stats.getStats("Joueur1");
        assertEquals(4, ps.getCardsPlayed());
        assertEquals(7, ps.getScore());
        assertEquals(2, ps.getTurnsPlayed());
        assertEquals(1, ps.getBombsPlayed());
    }

    @Test
    public void testSetWinner() {
        GameStats stats = new GameStats();
        stats.setWinner("Joueur1");
        assertEquals("Joueur1", stats.getWinnerId());
    }

    @Test
    public void testSetTotalRounds() {
        GameStats stats = new GameStats();
        stats.setTotalRounds(10);
        assertEquals(10, stats.getTotalRounds());
    }

    @Test
    public void testPlayerStatsWithMultiplePlayers() {
        GameStats stats = new GameStats();
        stats.registerPlayer("Alice");
        stats.registerPlayer("Bob");
        
        List<Card> aliceCards = Arrays.asList(new Card(Card.Value.ACE, Card.Suit.HEARTS));
        stats.recordCombination("Alice", new Combination(aliceCards, CombinationType.SIMPLE));
        
        List<Card> bobCards = Arrays.asList(
            new Card(Card.Value.JOKER, Card.Suit.JOKER),
            new Card(Card.Value.JOKER, Card.Suit.JOKER)
        );
        stats.recordCombination("Bob", new Combination(bobCards, CombinationType.DOUBLE_JOKER));
        
        assertEquals(1, stats.getStats("Alice").getScore());
        assertEquals(8, stats.getStats("Bob").getScore());
    }
}
