package engine.process;

import engine.data.*;
import log.LoggerUtility;
import org.apache.log4j.Logger;
import java.util.*;

/**
 * Easy difficulty bot: plays randomly, sometimes passes voluntarily.
 */
public class EasyBot extends BotPlayer {
    private static final Logger logger = LoggerUtility.getLogger(EasyBot.class, "html");
    private Random random = new Random();

    public EasyBot(String id) { super(id); }

    @Override
    public Combination choisirCombinaison(List<Card> hand, Combination last) {
        List<Combination> jouables = getCombinaisonsJouables(hand, last);
        if (jouables.isEmpty()) {
            logger.info(getId() + " - Aucune combinaison jouable, pioche.");
            return null;
        }
        if (last != null && random.nextDouble() < 0.30) {
            logger.info(getId() + " choisit de passer.");
            return null;
        }
        Combination choix = jouables.get(random.nextInt(jouables.size()));
        logger.info(getId() + " joue (aleatoire) : " + choix.toString());
        return choix;
    }
}
