package test.unit;

import static org.junit.Assert.*;
import org.junit.Test;
import engine.process.Combination;
import engine.data.*;
import java.util.*;

public class TestCombination {

    // ========== DETECTION DES TYPES ==========

    @Test
    public void testDetectSimple() {
        List<Card> cards = Arrays.asList(new Card(Card.Value.FIVE, Card.Suit.HEARTS));
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.SIMPLE, type);
    }

    @Test
    public void testDetectDouble() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.KING, Card.Suit.HEARTS),
            new Card(Card.Value.KING, Card.Suit.SPADES)
        );
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.DOUBLE, type);
    }

    @Test
    public void testDetectDoubleJoker() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.JOKER, Card.Suit.JOKER),
            new Card(Card.Value.JOKER, Card.Suit.JOKER)
        );
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.DOUBLE_JOKER, type);
    }

    @Test
    public void testDetectBomb3Cards() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.SEVEN, Card.Suit.HEARTS),
            new Card(Card.Value.SEVEN, Card.Suit.SPADES),
            new Card(Card.Value.SEVEN, Card.Suit.CLUBS)
        );
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.BOMB, type);
    }

    @Test
    public void testDetectBomb4Cards() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.QUEEN, Card.Suit.HEARTS),
            new Card(Card.Value.QUEEN, Card.Suit.SPADES),
            new Card(Card.Value.QUEEN, Card.Suit.CLUBS),
            new Card(Card.Value.QUEEN, Card.Suit.DIAMONDS)
        );
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.BOMB, type);
    }

    @Test
    public void testDetectSeries() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.FOUR, Card.Suit.HEARTS),
            new Card(Card.Value.FIVE, Card.Suit.SPADES),
            new Card(Card.Value.SIX, Card.Suit.CLUBS)
        );
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.SERIES, type);
    }

    @Test
    public void testDetectSeriesWithJoker() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.FOUR, Card.Suit.HEARTS),
            new Card(Card.Value.JOKER, Card.Suit.JOKER),
            new Card(Card.Value.SIX, Card.Suit.SPADES)
        );
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.SERIES, type);
    }

    @Test
    public void testJokerAloneIsInvalid() {
        List<Card> cards = Arrays.asList(new Card(Card.Value.JOKER, Card.Suit.JOKER));
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.INVALID, type);
    }

    @Test
    public void testInvalidCombination() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.TWO, Card.Suit.HEARTS),
            new Card(Card.Value.FIVE, Card.Suit.SPADES)
        );
        CombinationType type = Combination.determineType(cards);
        assertEquals(CombinationType.INVALID, type);
    }

    // ========== COMPARAISON (CAN BEAT) ==========

    @Test
    public void testSimpleBeatsLowerSimple() {
        Combination lower = createSimple(Card.Value.FIVE);
        Combination higher = createSimple(Card.Value.SIX);
        
        assertTrue(higher.canBeat(lower));
        assertFalse(lower.canBeat(higher));
    }

    @Test
    public void testTwoBeatsNormal() {
        Combination normal = createSimple(Card.Value.SEVEN);
        Combination two = createSimple(Card.Value.TWO);
        
        assertTrue(two.canBeat(normal));
    }

    @Test
    public void testBombBeatsTwo() {
        Combination two = createSimple(Card.Value.TWO);
        Combination bomb = createBomb(Card.Value.THREE);
        
        assertTrue(bomb.canBeat(two));
        assertFalse(two.canBeat(bomb));
    }

    @Test
    public void testDoubleJokerBeatsBomb() {
        Combination bomb = createBomb(Card.Value.ACE);
        Combination doubleJoker = createDoubleJoker();
        
        assertTrue(doubleJoker.canBeat(bomb));
    }

    @Test
    public void testSeriesBeatsLowerSeries() {
        Combination lower = createSeries(Card.Value.FOUR, Card.Value.FIVE, Card.Value.SIX);
        Combination higher = createSeries(Card.Value.FIVE, Card.Value.SIX, Card.Value.SEVEN);
        
        assertTrue(higher.canBeat(lower));
        assertFalse(lower.canBeat(higher));
    }

    @Test
    public void testCannotBeatDifferentTypeSameSize() {
        // Simple ne peut pas battre Double (même taille 1 vs 2? Non, tailles différentes)
        // Ce test vérifie que des types différents ne peuvent pas se battre
        Combination simple = createSimple(Card.Value.KING);
        Combination doubleCombo = createDouble(Card.Value.TWO);
        
        assertFalse(doubleCombo.canBeat(simple));
        assertFalse(simple.canBeat(doubleCombo));
    }

    @Test
    public void testTwoCannotBeatBomb() {
        Combination two = createSimple(Card.Value.TWO);
        Combination bomb = createBomb(Card.Value.THREE);
        
        assertFalse(two.canBeat(bomb));
    }

    @Test
    public void testNullPreviousIsAlwaysBeatable() {
        Combination combo = createSimple(Card.Value.FIVE);
        assertTrue(combo.canBeat(null));
    }

    @Test
    public void testBombBeatsNormalCard() {
        Combination normal = createSimple(Card.Value.KING);
        Combination bomb = createBomb(Card.Value.THREE);
        
        assertTrue(bomb.canBeat(normal));
    }

    // ========== VALIDITE DES COMBINAISONS ==========

    @Test
    public void testValidCombinationIsValid() {
        Combination combo = createSimple(Card.Value.FIVE);
        assertTrue(combo.isValid());
    }

    @Test
    public void testInvalidCombinationIsNotValid() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.TWO, Card.Suit.HEARTS),
            new Card(Card.Value.FIVE, Card.Suit.SPADES)
        );
        Combination combo = new Combination(cards, CombinationType.INVALID);
        assertFalse(combo.isValid());
    }

    @Test
    public void testToString() {
        Combination combo = createSimple(Card.Value.FIVE);
        String str = combo.toString();
        assertTrue(str.contains("Simple"));
        assertTrue(str.contains("5"));
    }

    // ========== METHODES AIDE ==========

    private Combination createSimple(Card.Value value) {
        List<Card> cards = Arrays.asList(new Card(value, Card.Suit.HEARTS));
        return new Combination(cards, CombinationType.SIMPLE);
    }

    private Combination createDouble(Card.Value value) {
        List<Card> cards = Arrays.asList(
            new Card(value, Card.Suit.HEARTS),
            new Card(value, Card.Suit.SPADES)
        );
        return new Combination(cards, CombinationType.DOUBLE);
    }

    private Combination createBomb(Card.Value value) {
        List<Card> cards = Arrays.asList(
            new Card(value, Card.Suit.HEARTS),
            new Card(value, Card.Suit.SPADES),
            new Card(value, Card.Suit.CLUBS)
        );
        return new Combination(cards, CombinationType.BOMB);
    }

    private Combination createDoubleJoker() {
        List<Card> cards = Arrays.asList(
            new Card(Card.Value.JOKER, Card.Suit.JOKER),
            new Card(Card.Value.JOKER, Card.Suit.JOKER)
        );
        return new Combination(cards, CombinationType.DOUBLE_JOKER);
    }

    private Combination createSeries(Card.Value v1, Card.Value v2, Card.Value v3) {
        List<Card> cards = Arrays.asList(
            new Card(v1, Card.Suit.HEARTS),
            new Card(v2, Card.Suit.SPADES),
            new Card(v3, Card.Suit.CLUBS)
        );
        return new Combination(cards, CombinationType.SERIES);
    }
}
