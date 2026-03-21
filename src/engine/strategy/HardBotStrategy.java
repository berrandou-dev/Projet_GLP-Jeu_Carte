package engine.strategy;

import engine.data.Card;
import engine.data.CombinationType;
import engine.process.Combination;

import java.util.List;
import java.util.Map;

/**
 * Stratégie DIFFICILE : 5 priorités.
 *
 *  1. Fin de partie (≤3 cartes) → jouer la plus forte pour finir.
 *  2. Éliminer un groupe complet du même type que la table.
 *  3. Même type que la table, combinaison la plus faible possible.
 *  4. Utiliser une Bombe si nécessaire.
 *  5. Passe stratégique (rien d'intéressant à jouer).
 *
 * Ouverture de tour (lastCombination == null) :
 *  → Préférer jouer un groupe complet (paire, série) plutôt qu'une carte isolée.
 */
public class HardBotStrategy implements BotStrategy {

    private static final int SEUIL_FIN_PARTIE = 3;

    @Override
    public Combination chooseCombination(List<Card> hand, Combination lastCombination) {
        List<Combination> jouables = CombinationHelper.getCombinaisonsJouables(hand, lastCombination);

        if (jouables.isEmpty()) return null;

        CombinationHelper.trierParForce(jouables);

        // ── 1. Fin de partie → jouer fort pour finir ──
        if (hand.size() <= SEUIL_FIN_PARTIE) {
            return jouables.get(jouables.size() - 1);
        }

        // ── Ouverture de tour ──
        if (lastCombination == null) {
            return choisirMeilleureOuverture(hand, jouables);
        }

        // ── 2. Éliminer un groupe complet du même type ──
        Combination groupe = choisirGroupeComplet(hand, jouables, lastCombination.getType());
        if (groupe != null) return groupe;

        // ── 3. Même type, la plus faible ──
        Combination memeType = choisirMemeType(jouables, lastCombination);
        if (memeType != null) return memeType;

        // ── 4. Bombe si nécessaire ──
        Combination bombe = choisirBombe(jouables);
        if (bombe != null) return bombe;

        // ── 5. Passe stratégique ──
        return null;
    }

    @Override
    public boolean shouldPass(List<Card> hand, Combination lastCombination) {
        return chooseCombination(hand, lastCombination) == null;
    }

    // ─── Méthodes privées ────────────────────────────────────────────────────

    private Combination choisirMeilleureOuverture(List<Card> hand, List<Combination> jouables) {
        Map<Card.Value, List<Card>> groupes = CombinationHelper.grouperParValeur(hand);

        // Préférer une paire ou série qui vide un groupe entier
        for (Combination c : jouables) {
            if (c.getType() == CombinationType.BOMB || c.getType() == CombinationType.DOUBLE_JOKER) continue;
            Card.Value valeur = c.getCards().get(0).getValue();
            List<Card> groupe = groupes.get(valeur);
            if (groupe != null && groupe.size() == c.getCards().size() && c.getCards().size() >= 2) {
                return c;
            }
        }

        // Sinon jouer la carte simple la plus faible
        for (Combination c : jouables) {
            if (c.getType() == CombinationType.SIMPLE) return c;
        }

        return jouables.get(0);
    }

    private Combination choisirGroupeComplet(List<Card> hand, List<Combination> jouables, CombinationType typeCible) {
        Map<Card.Value, List<Card>> groupes = CombinationHelper.grouperParValeur(hand);

        for (Combination c : jouables) {
            if (c.getType() != typeCible) continue;
            Card.Value valeur = c.getCards().get(0).getValue();
            List<Card> groupe = groupes.get(valeur);
            // Le groupe est entièrement vidé par ce jeu
            if (groupe != null && groupe.size() == c.getCards().size()) return c;
        }
        return null;
    }

    private Combination choisirMemeType(List<Combination> jouables, Combination lastCombination) {
        for (Combination c : jouables) {
            if (c.getType() == lastCombination.getType()
                    && c.getType() != CombinationType.BOMB
                    && c.getType() != CombinationType.DOUBLE_JOKER) {
                return c;
            }
        }
        return null;
    }

    private Combination choisirBombe(List<Combination> jouables) {
        for (Combination c : jouables) {
            if (c.getType() == CombinationType.BOMB || c.getType() == CombinationType.DOUBLE_JOKER) {
                return c;
            }
        }
        return null;
    }
}