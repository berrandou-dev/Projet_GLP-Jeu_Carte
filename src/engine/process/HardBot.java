package engine.process;

import engine.data.*;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import java.util.*;

public class HardBot extends BotPlayer {

    private static final Logger logger = LoggerUtility.getLogger(HardBot.class, "html");

    public HardBot(String id) {
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

        if (hand.size() <= 3) {
            Combination choix = jouables.get(jouables.size() - 1);
            logger.info(getId() + " (main courte) joue fort : " + choix.toString());
            return choix;
        }

        if (last == null) {
            for (Combination c : jouables) {
                if (c.getType() != CombinationType.SIMPLE) {
                    logger.info(getId() + " ouvre avec : " + c.toString());
                    return c;
                }
            }
            logger.info(getId() + " ouvre avec : " + jouables.get(0).toString());
            return jouables.get(0);
        }

        for (Combination c : jouables) {
            if (c.getType() == last.getType() &&
                c.getType() != CombinationType.BOMB) {
                logger.info(getId() + " joue meme type : " + c.toString());
                return c;
            }
        }

        for (Combination c : jouables) {
            if (c.getType() == CombinationType.BOMB ||
                c.getType() == CombinationType.DOUBLE_JOKER) {
                logger.info(getId() + " joue bombe : " + c.toString());
                return c;
            }
        }

        logger.info(getId() + " joue : " + jouables.get(0).toString());
        return jouables.get(0);
    }
}
