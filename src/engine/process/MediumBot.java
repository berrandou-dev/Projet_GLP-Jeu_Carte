package engine.process;

import engine.data.*;
import engine.logger.GameLogger;
import java.util.*;

public class MediumBot extends BotPlayer {
    
    private GameLogger logger = GameLogger.getInstance();
    
    public MediumBot(String id) {
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
        
        for (Combination c : jouables) {
            if (c.getType() != CombinationType.BOMB && 
                c.getType() != CombinationType.DOUBLE_JOKER) {
                return c;
            }
        }
        
        return jouables.get(0);
    }
}
