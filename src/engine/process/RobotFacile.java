package engine.process;

import engine.data.Card;

import java.util.List;
import java.util.Random;

/**
 * ROBOT FACILE
 * 
 * Stratégie : aucune.
 * - Choisit une combinaison valide au hasard parmi celles qui battent la table.
 * - A 30% de chance de passer même s'il peut jouer.
 * - Ne conserve jamais ses cartes fortes intentionnellement.
 */
public class RobotFacile extends RobotJoueur {

    private static final double CHANCE_PASSER = 0.30;
    private final Random random = new Random();

    public RobotFacile(String id) {
        super(id);
    }

    @Override
    public Combination choisirCombinaison(List<Card> main, Combination cCourante) {
        List<Combination> jouables = getCombinaisonsJouables(main, cCourante);

        // Aucune combinaison jouable → passer obligatoirement
        if (jouables.isEmpty()) {
            System.out.println(getId() + " [Facile] passe — aucune combinaison jouable.");
            return null;
        }

        // Passer aléatoirement même si une combinaison est disponible
        if (cCourante != null && random.nextDouble() < CHANCE_PASSER) {
            System.out.println(getId() + " [Facile] passe aléatoirement.");
            return null;
        }

        // Choisir au hasard
        Combination choisie = jouables.get(random.nextInt(jouables.size()));
        System.out.println(getId() + " [Facile] joue aléatoirement : " + choisie);
        return choisie;
    }
}