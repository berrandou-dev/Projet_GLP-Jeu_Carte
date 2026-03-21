package engine.strategy;

import engine.data.Card;
import engine.process.Combination;

import java.util.List;
import java.util.Random;

/**
 * Stratégie FACILE : aucune stratégie.
 * Joue une combinaison au hasard, passe 30% du temps sans raison.
 */
public class EasyBotStrategy implements BotStrategy {

    private static final double CHANCE_PASSER = 0.30;
    private final Random random = new Random();

    @Override
    public Combination chooseCombination(List<Card> hand, Combination lastCombination) {
        if (shouldPass(hand, lastCombination)) return null;

        List<Combination> jouables = CombinationHelper.getCombinaisonsJouables(hand, lastCombination);
        if (jouables.isEmpty()) return null;

        return jouables.get(random.nextInt(jouables.size()));
    }

    @Override
    public boolean shouldPass(List<Card> hand, Combination lastCombination) {
        List<Combination> jouables = CombinationHelper.getCombinaisonsJouables(hand, lastCombination);
        if (jouables.isEmpty()) return true;
        // Passe aléatoirement si quelqu'un a déjà joué
        return lastCombination != null && random.nextDouble() < CHANCE_PASSER;
    }
}