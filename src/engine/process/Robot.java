package engine.process;

import engine.data.Card;
import java.util.List;

/**
 * Interface Robot — implémentée par les trois niveaux de difficulté.
 * Chaque robot étend Player et implémente cette interface.
 */
public interface Robot {

    /**
     * Choisit une combinaison à jouer.
     *
     * @param main      la main actuelle du robot
     * @param cCourante la combinaison sur la table (null si le robot ouvre le tour)
     * @return la Combination choisie, ou null pour passer
     */
    Combination choisirCombinaison(List<Card> main, Combination cCourante);
}