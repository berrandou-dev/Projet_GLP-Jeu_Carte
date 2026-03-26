package engine.process;

import engine.data.*;
import java.util.*;

public class EasyBot extends BotPlayer {
    private Random random = new Random();
    
    public EasyBot(String id) {
        super(id);
    }
    
    @Override
    public Combination choisirCombinaison(List<Card> hand, Combination last) {
        List<Combination> jouables = getCombinaisonsJouables(hand, last);
        if (jouables.isEmpty()) return null;
        
        // 30% de chance de passer
        if (last != null && random.nextDouble() < 0.30) return null;
        
        return jouables.get(random.nextInt(jouables.size()));
    }
}
