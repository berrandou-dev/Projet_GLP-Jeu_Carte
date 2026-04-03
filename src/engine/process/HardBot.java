package engine.process;

import engine.data.*;
import engine.logger.GameLogger;
import java.util.*;

public class HardBot extends BotPlayer {
    
    private GameLogger logger = GameLogger.getInstance();
    
    public HardBot(String id) {
        super(id);
    }
    
    @Override
    public Combination choisirCombinaison(List<Card> hand, Combination last) {
        List<Combination> jouables = getCombinaisonsJouables(hand, last);
        
        if (jouables.isEmpty()) {
            logger.log(getId() + " - Aucune combinaison jouable");
            return null;
        }
        
        trierParForce(jouables);
        
        if (hand.size() <= 3) {
            return jouables.get(jouables.size() - 1);
        }
        
        if (last == null) {
            for (Combination c : jouables) {
                if (c.getType() != CombinationType.SIMPLE) return c;
            }
            return jouables.get(0);
        }
        
        for (Combination c : jouables) {
            if (c.getType() == last.getType() && 
                c.getType() != CombinationType.BOMB) {
                return c;
            }
        }
        
        for (Combination c : jouables) {
            if (c.getType() == CombinationType.BOMB || 
                c.getType() == CombinationType.DOUBLE_JOKER) {
                return c;
            }
        }
        
        return jouables.get(0);
    }
}
