package engine.data;

import java.util.Objects;

/**
 * Represents a playing card with a value and a suit.
 */
public class Card {

    public enum Value {
        TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"),
        EIGHT("8"), NINE("9"), TEN("10"), JACK("J"), QUEEN("Q"), KING("K"), ACE("A"), JOKER("JOKER");
        private String symbol;
        Value(String symbol) { this.symbol = symbol; }
        public String getSymbol() { return symbol; }
    }

    public enum Suit {
        HEARTS("\u2665"), SPADES("\u2660"), DIAMONDS("\u2666"), CLUBS("\u2663"), JOKER("\u2605");
        private String symbol;
        Suit(String symbol) { this.symbol = symbol; }
        public String getSymbol() { return symbol; }
    }

    private Value value;
    private Suit suit;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public Value getValue() { return value; }
    public Suit getSuit() { return suit; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return value == card.value && suit == card.suit;
    }

    @Override
    public int hashCode() { return Objects.hash(value, suit); }
}
