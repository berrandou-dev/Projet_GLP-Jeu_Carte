package engine.strategy;

import engine.data.Card;
import engine.data.CombinationType;
import engine.process.Combination;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe utilitaire statique.
 * Génère toutes les combinaisons valides / jouables depuis une main.
 * Utilisée par les trois stratégies et par RobotJoueur.
 */
public class CombinationHelper {

    private CombinationHelper() {}

    /** Toutes les combinaisons valides qui battent cCourante (ou toutes si null). */
    public static List<Combination> getCombinaisonsJouables(List<Card> main, Combination cCourante) {
        List<Combination> toutes = getToutesCombinaisonsValides(main);
        if (cCourante == null) return toutes;
        return toutes.stream().filter(c -> c.canBeat(cCourante)).collect(Collectors.toList());
    }

    /** Toutes les combinaisons valides d'une main. */
    public static List<Combination> getToutesCombinaisonsValides(List<Card> main) {
        List<Combination> result = new ArrayList<>();

        // Simples
        for (Card c : main) {
            Combination combo = new Combination(List.of(c), CombinationType.SIMPLE);
            if (combo.isValid()) result.add(combo);
        }

        // Doubles et Double Joker
        for (int i = 0; i < main.size(); i++) {
            for (int j = i + 1; j < main.size(); j++) {
                List<Card> paire = List.of(main.get(i), main.get(j));
                CombinationType t = Combination.determineType(paire);
                if (t == CombinationType.DOUBLE || t == CombinationType.DOUBLE_JOKER) {
                    result.add(new Combination(paire, t));
                }
            }
        }

        // Bombes (4 de même valeur)
        grouperParValeur(main).forEach((valeur, cartes) -> {
            if (cartes.size() >= 4) {
                result.add(new Combination(cartes.subList(0, 4), CombinationType.BOMB));
            }
        });

        // Séries (3+ consécutives)
        result.addAll(trouverSeries(main));

        return result.stream().filter(Combination::isValid).collect(Collectors.toList());
    }

    /** Trie du plus faible au plus fort. */
    public static List<Combination> trierParForce(List<Combination> combos) {
        combos.sort(Comparator.comparingInt(CombinationHelper::ordinalMax));
        return combos;
    }

    /** Ordinal max d'une combinaison = sa force. */
    public static int ordinalMax(Combination c) {
        return c.getCards().stream()
                .mapToInt(card -> card.getValue().ordinal())
                .max().orElse(0);
    }

    /** Groupe les cartes par valeur. */
    public static Map<Card.Value, List<Card>> grouperParValeur(List<Card> main) {
        Map<Card.Value, List<Card>> map = new LinkedHashMap<>();
        for (Card c : main) {
            map.computeIfAbsent(c.getValue(), k -> new ArrayList<>()).add(c);
        }
        return map;
    }

    private static List<Combination> trouverSeries(List<Card> main) {
        List<Combination> series = new ArrayList<>();
        List<Card> triees = main.stream()
                .filter(c -> c.getValue() != Card.Value.JOKER)
                .sorted(Comparator.comparingInt(c -> c.getValue().ordinal()))
                .collect(Collectors.toList());

        for (int debut = 0; debut < triees.size(); debut++) {
            List<Card> courante = new ArrayList<>();
            courante.add(triees.get(debut));
            int dernierOrdinal = triees.get(debut).getValue().ordinal();

            for (int fin = debut + 1; fin < triees.size(); fin++) {
                int ordinal = triees.get(fin).getValue().ordinal();
                if (ordinal == dernierOrdinal + 1) {
                    courante.add(triees.get(fin));
                    dernierOrdinal = ordinal;
                    if (courante.size() >= 3) {
                        series.add(new Combination(new ArrayList<>(courante), CombinationType.SERIES));
                    }
                } else if (ordinal != dernierOrdinal) {
                    break;
                }
            }
        }
        return series;
    }
}