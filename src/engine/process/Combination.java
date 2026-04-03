package engine.process;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import engine.data.*;

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
        
        // Carte 2 bat tout sauf les bombes
    	if (this.type == CombinationType.SIMPLE && this.cards.get(0).getValue() == Card.Value.TWO) {
        	return previous.getType() != CombinationType.BOMB && previous.getType() != CombinationType.DOUBLE_JOKER;
    	}
    
    	// Si la précédente est un 2, seule une bombe peut battre
    	if (previous.type == CombinationType.SIMPLE && previous.cards.get(0).getValue() == Card.Value.TWO) {
        	return this.type == CombinationType.BOMB || this.type == CombinationType.DOUBLE_JOKER;
    	}
        
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
    
    // Séparer jokers
    List<Card> normal = new ArrayList<>();
    int jokers = 0;
    
    for (Card c : cards) {
        if (c.getValue() == Card.Value.JOKER) {
            jokers++;
        } else {
            normal.add(c);
        }
    }
    
    if (normal.isEmpty()) {
        if (jokers == 2) return CombinationType.DOUBLE_JOKER;
        return CombinationType.INVALID; // 1 joker seul
    }
    
    // Trier les cartes normales
    Collections.sort(normal, (a, b) -> a.getValue().ordinal() - b.getValue().ordinal());
    
    int taille = normal.size() + jokers;
    
    // --- SIMPLE ---
    if (taille == 1) return CombinationType.SIMPLE;
    
    // --- DOUBLE (2 cartes identiques) ---
    if (taille == 2) {
        // Deux cartes identiques
        if (normal.size() == 2 && normal.get(0).getValue() == normal.get(1).getValue()) {
            return CombinationType.DOUBLE;
        }
        // 1 carte + 1 joker
        if (normal.size() == 1 && jokers == 1) {
            return CombinationType.DOUBLE;
        }
    }
    
    // --- BOMBE (3+ cartes identiques) ---
    if (taille >= 3 && normal.size() >= 1) {
        boolean identiques = true;
        for (int i = 1; i < normal.size(); i++) {
            if (normal.get(i).getValue() != normal.get(0).getValue()) {
                identiques = false;
                break;
            }
        }
        if (identiques) {
            return CombinationType.BOMB;
        }
    }
    
    // --- SÉRIE (3+ cartes consécutives) ---
    if (taille >= 3 && normal.size() >= 2) {
        if (estSerie(normal, jokers)) {
            return CombinationType.SERIES;
        }
    }
    
    return CombinationType.INVALID;
}

/**
 * Vérifie si les cartes forment une série avec le joker
 */
private static boolean estSerie(List<Card> normal, int jokers) {
    // Prendre les valeurs
    List<Integer> vals = new ArrayList<>();
    for (Card c : normal) {
        vals.add(c.getValue().ordinal());
    }
    Collections.sort(vals);
    
    // Vérifier les écarts entre valeurs consécutives
    int jokersNecessaires = 0;
    for (int i = 1; i < vals.size(); i++) {
        int ecart = vals.get(i) - vals.get(i-1) - 1;
        if (ecart < 0) return false;
        jokersNecessaires += ecart;
    }
    
    return jokersNecessaires <= jokers;
}
    
    @Override
	public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(type).append(" [");
    for (int i = 0; i < cards.size(); i++) {
        Card c = cards.get(i);
        sb.append(c.getValue().getSymbol()).append(c.getSuit().getSymbol());
        if (i < cards.size() - 1) {
            sb.append(" ");
        }
    }
    sb.append("]");
    return sb.toString();
}
}
