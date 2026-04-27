package engine.process;

import engine.data.*;
import java.util.*;

/**
 * Abstract base class for AI players.
 * Subclasses implement different difficulty strategies.
 */
public abstract class BotPlayer extends Player {

    public BotPlayer(String id) { super(id); }

    public abstract Combination choisirCombinaison(List<Card> hand, Combination last);

    protected List<Combination> getCombinaisonsJouables(List<Card> hand, Combination last) {
        List<Combination> toutes = getToutesCombinaisonsValides(hand);
        if (last == null) return toutes;
        List<Combination> result = new ArrayList<>();
        for (Combination c : toutes) {
            if (c.canBeat(last)) result.add(c);
        }
        return result;
    }

    protected List<Combination> getToutesCombinaisonsValides(List<Card> hand) {
        List<Combination> result = new ArrayList<>();
        // Singles
        for (Card c : hand) {
            result.add(new Combination(Arrays.asList(c), CombinationType.SIMPLE));
        }
        // Pairs
        for (int i = 0; i < hand.size(); i++) {
            for (int j = i + 1; j < hand.size(); j++) {
                List<Card> pair = Arrays.asList(hand.get(i), hand.get(j));
                CombinationType t = Combination.determineType(pair);
                if (t == CombinationType.DOUBLE || t == CombinationType.DOUBLE_JOKER) {
                    result.add(new Combination(pair, t));
                }
            }
        }
        // Bombs (4 identical cards)
        Map<Card.Value, List<Card>> groups = grouperParValeur(hand);
        for (List<Card> cards : groups.values()) {
            if (cards.size() >= 4) {
                result.add(new Combination(cards.subList(0, 4), CombinationType.BOMB));
            }
        }
        // Series
        result.addAll(trouverSeries(hand));
        
        List<Combination> valides = new ArrayList<>();
        for (Combination c : result) {
            if (c.isValid()) valides.add(c);
        }
        return valides;
    }

    protected Map<Card.Value, List<Card>> grouperParValeur(List<Card> hand) {
        Map<Card.Value, List<Card>> map = new LinkedHashMap<>();
        for (Card c : hand) {
            map.computeIfAbsent(c.getValue(), k -> new ArrayList<>()).add(c);
        }
        return map;
    }

    protected void trierParForce(List<Combination> combos) {
        combos.sort((c1, c2) -> {
            int max1 = c1.getCards().stream().mapToInt(c -> c.getValue().ordinal()).max().orElse(0);
            int max2 = c2.getCards().stream().mapToInt(c -> c.getValue().ordinal()).max().orElse(0);
            return Integer.compare(max1, max2);
        });
    }

    private List<Combination> trouverSeries(List<Card> hand) {
        List<Combination> series = new ArrayList<>();
        List<Card> normal = new ArrayList<>();
        for (Card c : hand) {
            if (c.getValue() != Card.Value.JOKER) normal.add(c);
        }
        normal.sort(Comparator.comparingInt(c -> c.getValue().ordinal()));
        
        for (int start = 0; start < normal.size(); start++) {
            List<Card> current = new ArrayList<>();
            current.add(normal.get(start));
            int lastVal = normal.get(start).getValue().ordinal();
            for (int i = start + 1; i < normal.size(); i++) {
                int val = normal.get(i).getValue().ordinal();
                if (val == lastVal + 1) {
                    current.add(normal.get(i));
                    lastVal = val;
                    if (current.size() >= 3) {
                        series.add(new Combination(new ArrayList<>(current), CombinationType.SERIES));
                    }
                } else if (val != lastVal) break;
            }
        }
        return series;
    }
}
