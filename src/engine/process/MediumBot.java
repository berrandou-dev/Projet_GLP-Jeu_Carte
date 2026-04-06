package engine.process;

import engine.data.*;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import java.util.*;

public class MediumBot extends BotPlayer {

    private static final Logger logger = LoggerUtility.getLogger(MediumBot.class, "html");

    public MediumBot(String id) {
        super(id);
    }

    @Override
    public Combination choisirCombinaison(List<Card> hand, Combination last) {
        List<Combination> jouables = getCombinaisonsJouables(hand, last);

        if (jouables.isEmpty()) {
            logger.info(getId() + " - Aucune combinaison jouable, pioche.");
            return null;
        }

        trierParForce(jouables);

        for (Combination c : jouables) {
            if (c.getType() != CombinationType.BOMB &&
                c.getType() != CombinationType.DOUBLE_JOKER) {
                logger.info(getId() + " joue (plus faible non-bombe) : " + c.toString());
                return c;
            }
        }

        logger.info(getId() + " joue : " + jouables.get(0).toString());
        return jouables.get(0);
    }
}
