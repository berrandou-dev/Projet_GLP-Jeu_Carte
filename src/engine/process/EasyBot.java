package engine.process;

import engine.data.*;
import engine.logger.GameLogger;
import java.util.*;

public class EasyBot extends BotPlayer {
    private Random random = new Random();
    private GameLogger logger = GameLogger.getInstance();
    
    public EasyBot(String id) {
        super(id);
    }
    
    @Override
    public Combination choisirCombinaison(List<Card> hand, Combination last) {
        List<Combination> jouables = getCombinaisonsJouables(hand, last);
        
        if (jouables.isEmpty()) {
            logger.log(getId() + " - Aucune combinaison jouable");
            return null;
        }
        
        if (last != null && random.nextDouble() < 0.30) {
            logger.log(getId() + " passe");
            return null;
        }
        
        return jouables.get(random.nextInt(jouables.size()));
    }
}
