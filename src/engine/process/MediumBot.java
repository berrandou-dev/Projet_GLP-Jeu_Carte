package engine.process;

import engine.data.*;
import java.util.*;

public class MediumBot extends BotPlayer {
    
    public MediumBot(String id) {
        super(id);
    }
    
    @Override
    public Combination choisirCombinaison(List<Card> hand, Combination last) {
        List<Combination> jouables = getCombinaisonsJouables(hand, last);
        if (jouables.isEmpty()) return null;
        
        trierParForce(jouables);
        
        // Jouer la plus faible qui n'est pas une bombe
        for (Combination c : jouables) {
            if (c.getType() != CombinationType.BOMB && 
                c.getType() != CombinationType.DOUBLE_JOKER) {
                return c;
            }
        }
        
        return jouables.get(0);
    }
}
