package engine.strategy;
 
import engine.data.Card;
import engine.data.CombinationType;
import engine.process.Combination;
 
import java.util.List;
 
/**
 * Stratégie MOYENNE : économique.
 * Joue la combinaison valide la plus faible qui bat la table.
 * Conserve Bombes et Double Joker pour les situations critiques.
 * Ne passe jamais si une combinaison normale est jouable.
 */
public class MediumBotStrategy implements BotStrategy {
 
    @Override
    public Combination chooseCombination(List<Card> hand, Combination lastCombination) {
        if (shouldPass(hand, lastCombination)) return null;
 
        List<Combination> jouables = CombinationHelper.getCombinaisonsJouables(hand, lastCombination);
        if (jouables.isEmpty()) return null;
 
        CombinationHelper.trierParForce(jouables);
 
        // Préférer une combinaison normale (pas Bombe ni Double Joker)
        for (Combination c : jouables) {
            if (c.getType() != CombinationType.BOMB && c.getType() != CombinationType.DOUBLE_JOKER) {
                return c;
            }
        }
 
        // Seules des cartes fortes disponibles → les jouer quand même
        return jouables.get(0);
    }
 
    @Override
    public boolean shouldPass(List<Card> hand, Combination lastCombination) {
        // Passe uniquement si aucune combinaison jouable
        return CombinationHelper.getCombinaisonsJouables(hand, lastCombination).isEmpty();
    }
}