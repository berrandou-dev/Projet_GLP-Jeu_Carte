package engine.process;

import engine.data.*;
import java.util.*;

public class HardBot extends BotPlayer {
    
    public HardBot(String id) {
        super(id);
    }
    
    @Override
    public Combination choisirCombinaison(List<Card> hand, Combination last) {
        List<Combination> jouables = getCombinaisonsJouables(hand, last);
        if (jouables.isEmpty()) return null;
        
        trierParForce(jouables);
        
        // 1. Fin de partie (≤3 cartes) => jouer le plus fort
        if (hand.size() <= 3) {
            return jouables.get(jouables.size() - 1);
        }
        
        // 2. Ouverture de tour => jouer une paire ou série si possible
        if (last == null) {
            for (Combination c : jouables) {
                if (c.getType() != CombinationType.SIMPLE) return c;
            }
            return jouables.get(0);
        }
        
        // 3. Jouer le même type que la combinaison précédente
        for (Combination c : jouables) {
            if (c.getType() == last.getType() && 
                c.getType() != CombinationType.BOMB) {
                return c;
            }
        }
        
        // 4. Bombe si nécessaire
        for (Combination c : jouables) {
            if (c.getType() == CombinationType.BOMB || 
                c.getType() == CombinationType.DOUBLE_JOKER) {
                return c;
            }
        }
        
        return jouables.get(0);
    }
}
