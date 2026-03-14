package process;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import data.Card;
import data.CombinationType;

public class Combination {

    // Attributes
    private List<Card> cards;
    private CombinationType type;
    
    // Constructor
    public Combination(List<Card> cards, CombinationType type) {
        this.cards = new ArrayList<>(cards);
        
        // Sort cards for consistent comparison
        Collections.sort(this.cards, (c1, c2) -> 
            c1.getValue().ordinal() - c2.getValue().ordinal());
        
        this.type = type;
    }

    // Getters
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    public CombinationType getType() {
        return type;
    }
    
    public boolean isValid() {
        if (type == null || !type.isValid()) {
            return false;
        }
        
        CombinationType detectedType = determineType(this.cards);
        return detectedType == type;
    }


    public boolean canBeat(Combination previous) {
        if (previous == null) return true;
        
        // Double Joker beats everything
        if (this.type == CombinationType.DOUBLE_JOKER) return true;
        
        // Bomb beats everything except Double Joker
        if (this.type == CombinationType.BOMB && 
            previous.getType() != CombinationType.BOMB) return true;
        
        // Same type and same size check
        if (this.type == previous.getType() && 
            this.cards.size() == previous.getCards().size()) {
            
            // Each card must be exactly +1 higher
            for (int i = 0; i < this.cards.size(); i++) {
                int thisValue = this.cards.get(i).getValue().ordinal();
                int prevValue = previous.getCards().get(i).getValue().ordinal();
                
                if (thisValue != prevValue + 1) {
                    return false;
                }
            }
            return true;
        }
        
        return false;
    }


    public static CombinationType determineType(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            return CombinationType.INVALID;
        }
        
        // Create a sorted copy for analysis
        List<Card> sortedCards = new ArrayList<>(cards);
        Collections.sort(sortedCards, (c1, c2) -> 
            c1.getValue().ordinal() - c2.getValue().ordinal());
        
        int size = sortedCards.size();
        
        // Single card
        if (size == 1) {
            return CombinationType.SIMPLE;
        }
        
        // Two cards
        if (size == 2) {
            // Double (pair)
            if (sortedCards.get(0).getValue() == sortedCards.get(1).getValue()) {
                return CombinationType.DOUBLE;
            }
            // Double Joker
            if (sortedCards.get(0).getValue() == Card.Value.JOKER && 
                sortedCards.get(1).getValue() == Card.Value.JOKER) {
                return CombinationType.DOUBLE_JOKER;
            }
        }
        
        // Three cards - possible bomb
        if (size == 3) {
            if (sortedCards.get(0).getValue() == sortedCards.get(1).getValue() &&
                sortedCards.get(1).getValue() == sortedCards.get(2).getValue()) {
                return CombinationType.BOMB;
            }
        }
        
        if (size >= 3) {
            boolean isSeries = true;
            for (int i = 1; i < size; i++) {
                // Check if cards follow each other in value
                if (sortedCards.get(i).getValue().ordinal() != 
                    sortedCards.get(i-1).getValue().ordinal() + 1) {
                    isSeries = false;
                    break;
                }
            }
            if (isSeries) {
                return CombinationType.SERIES;
            }
        }
        
        return CombinationType.INVALID;
    }
    
    @Override
    public String toString() {
        return type + " with " + cards.size() + " cards";
    }
}
