package engine.process;

import java.util.*;
import engine.data.*;

/**
 * Represents a combination of cards (Simple, Double, Series, Bomb, Double Joker).
 * Handles type detection and comparison logic.
 */
public class Combination {
    private List<Card> cards;
    private CombinationType type;

    public Combination(List<Card> cards, CombinationType type) {
        this.cards = new ArrayList<>(cards);
        this.cards.sort((c1, c2) -> c1.getValue().ordinal() - c2.getValue().ordinal());
        this.type = type;
    }

    public List<Card> getCards() { return new ArrayList<>(cards); }
    public CombinationType getType() { return type; }

    public boolean isValid() {
        if (type == null || !type.isValid()) return false;
        return determineType(this.cards) == type;
    }

    public boolean canBeat(Combination previous) {
        if (previous == null) return true;
        
        // 2 beats any single card except bombs
        if (this.type == CombinationType.SIMPLE && this.cards.get(0).getValue() == Card.Value.TWO) {
            return previous.getType() != CombinationType.BOMB && previous.getType() != CombinationType.DOUBLE_JOKER;
        }
        // Only a bomb can beat a 2
        if (previous.type == CombinationType.SIMPLE && previous.cards.get(0).getValue() == Card.Value.TWO) {
            return this.type == CombinationType.BOMB || this.type == CombinationType.DOUBLE_JOKER;
        }
        // Double Joker beats everything
        if (this.type == CombinationType.DOUBLE_JOKER) return true;
        // Bomb beats everything except Double Joker
        if (this.type == CombinationType.BOMB && previous.getType() != CombinationType.BOMB) return true;
        // Same type comparison
        if (this.type == previous.getType() && this.cards.size() == previous.getCards().size()) {
            for (int i = 0; i < this.cards.size(); i++) {
                if (this.cards.get(i).getValue().ordinal() != previous.getCards().get(i).getValue().ordinal() + 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static CombinationType determineType(List<Card> cards) {
        if (cards == null || cards.isEmpty()) return CombinationType.INVALID;
        
        List<Card> normal = new ArrayList<>();
        int jokers = 0;
        for (Card c : cards) {
            if (c.getValue() == Card.Value.JOKER) jokers++;
            else normal.add(c);
        }
        if (normal.isEmpty()) {
            return (jokers == 2) ? CombinationType.DOUBLE_JOKER : CombinationType.INVALID;
        }
        normal.sort((a, b) -> a.getValue().ordinal() - b.getValue().ordinal());
        int size = normal.size() + jokers;
        
        if (size == 1) return CombinationType.SIMPLE;
        if (size == 2) {
            if (normal.size() == 2 && normal.get(0).getValue() == normal.get(1).getValue()) return CombinationType.DOUBLE;
            if (normal.size() == 1 && jokers == 1) return CombinationType.DOUBLE;
        }
        if (size >= 3 && normal.size() >= 1) {
            boolean same = true;
            for (int i = 1; i < normal.size(); i++) {
                if (normal.get(i).getValue() != normal.get(0).getValue()) { same = false; break; }
            }
            if (same) return CombinationType.BOMB;
        }
        if (size >= 3 && normal.size() >= 2 && estSerie(normal, jokers)) return CombinationType.SERIES;
        return CombinationType.INVALID;
    }

    private static boolean estSerie(List<Card> normal, int jokers) {
        List<Integer> vals = new ArrayList<>();
        for (Card c : normal) vals.add(c.getValue().ordinal());
        vals.sort(Integer::compareTo);
        int needed = 0;
        for (int i = 1; i < vals.size(); i++) {
            int gap = vals.get(i) - vals.get(i-1) - 1;
            if (gap < 0) return false;
            needed += gap;
        }
        return needed <= jokers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" [");
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            sb.append(c.getValue().getSymbol()).append(c.getSuit().getSymbol());
            if (i < cards.size() - 1) sb.append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
