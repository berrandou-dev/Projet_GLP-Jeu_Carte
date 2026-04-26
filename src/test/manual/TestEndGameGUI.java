package test.manual;

import engine.data.*;
import engine.process.Combination;
import gui.screens.EndGameGUI;
import java.util.Arrays;

public class TestEndGameGUI {

    public static void main(String[] args) {
        new EndGameGUI(createMockStats(), 3,
            () -> System.out.println("Rejouer"),
            () -> System.out.println("Menu")
        );
    }
    
    private static GameStats createMockStats() {
        GameStats stats = new GameStats();
        
        stats.registerPlayer("Vous");
        stats.registerPlayer("Robot 1");
        stats.registerPlayer("Robot 2");
        stats.setWinner("Vous");
        stats.setTotalRounds(8);
        
        // Vous : 5 simples + 1 bombe + 1 double joker
        for (int i = 0; i < 5; i++) {
            stats.recordCombination("Vous", simple(Card.Value.THREE, Card.Suit.HEARTS));
        }
        stats.recordCombination("Vous", bomb(Card.Value.KING));
        stats.recordCombination("Vous", doubleJoker());
        
        // Robot 1 : 15 simples
        for (int i = 0; i < 15; i++) {
            stats.recordCombination("Robot 1", simple(Card.Value.FIVE, Card.Suit.SPADES));
        }
        
        // Robot 2 : 10 simples
        for (int i = 0; i < 10; i++) {
            stats.recordCombination("Robot 2", simple(Card.Value.SEVEN, Card.Suit.CLUBS));
        }
        
        return stats;
    }
    
    // ─── Helpers ───
    private static Combination simple(Card.Value value, Card.Suit suit) {
        return new Combination(Arrays.asList(new Card(value, suit)), CombinationType.SIMPLE);
    }
    
    private static Combination bomb(Card.Value value) {
        return new Combination(Arrays.asList(
            new Card(value, Card.Suit.HEARTS),
            new Card(value, Card.Suit.SPADES),
            new Card(value, Card.Suit.CLUBS)), CombinationType.BOMB);
    }
    
    private static Combination doubleJoker() {
        return new Combination(Arrays.asList(
            new Card(Card.Value.JOKER, Card.Suit.JOKER),
            new Card(Card.Value.JOKER, Card.Suit.JOKER)), CombinationType.DOUBLE_JOKER);
    }
}
