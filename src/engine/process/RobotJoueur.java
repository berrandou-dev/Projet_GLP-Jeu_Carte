package engine.process;

import engine.data.Card;
import engine.data.CombinationType;
import engine.data.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe abstraite commune aux trois robots.
 * Étend Player et implémente Robot.
 * Fournit les utilitaires pour générer toutes les combinaisons jouables.
 */
public abstract class RobotJoueur extends Player implements Robot {

    public RobotJoueur(String id) {
        super(id);
    }

    // ─── Utilitaires partagés ─────────────────────────────────────────────────

    /**
     * Retourne toutes les combinaisons valides de la main qui battent cCourante.
     * Si cCourante est null, retourne toutes les combinaisons valides.
     */
    protected List<Combination> getCombinaisonsJouables(List<Card> main, Combination cCourante) {
        List<Combination> toutes = getToutesCombinaisonsValides(main);
        if (cCourante == null) return toutes;
        return toutes.stream()
                .filter(c -> c.canBeat(cCourante))
                .collect(Collectors.toList());
    }

    /**
     * Génère toutes les combinaisons valides depuis une main.
     */
    protected List<Combination> getToutesCombinaisonsValides(List<Card> main) {
        List<Combination> result = new ArrayList<>();

        // --- Simples (1 carte) ---
        for (Card c : main) {
            Combination combo = new Combination(List.of(c), CombinationType.SIMPLE);
            if (combo.isValid()) result.add(combo);
        }

        // --- Doubles et Double Joker (2 cartes) ---
        for (int i = 0; i < main.size(); i++) {
            for (int j = i + 1; j < main.size(); j++) {
                List<Card> paire = List.of(main.get(i), main.get(j));
                CombinationType t = Combination.determineType(paire);
                if (t == CombinationType.DOUBLE || t == CombinationType.DOUBLE_JOKER) {
                    result.add(new Combination(paire, t));
                }
            }
        }

        // --- Bombes (4 cartes de même valeur) ---
        grouperParValeur(main).forEach((valeur, cartes) -> {
            if (cartes.size() >= 4) {
                result.add(new Combination(cartes.subList(0, 4), CombinationType.BOMB));
            }
        });

        // --- Séries (3 cartes consécutives ou plus) ---
        result.addAll(trouverSeries(main));

        return result.stream().filter(Combination::isValid).collect(Collectors.toList());
    }

    /**
     * Trouve toutes les séries possibles dans la main.
     */
    private List<Combination> trouverSeries(List<Card> main) {
        List<Combination> series = new ArrayList<>();

        // Trier les cartes non-Joker par valeur ordinale
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
                    break; // rupture de séquence
                }
            }
        }
        return series;
    }

    /**
     * Groupe les cartes par valeur.
     */
    protected Map<Card.Value, List<Card>> grouperParValeur(List<Card> main) {
        Map<Card.Value, List<Card>> map = new LinkedHashMap<>();
        for (Card c : main) {
            map.computeIfAbsent(c.getValue(), k -> new ArrayList<>()).add(c);
        }
        return map;
    }

    /**
     * Trie une liste de combinaisons du plus faible au plus fort.
     */
    protected List<Combination> trierParForce(List<Combination> combos) {
        combos.sort(Comparator.comparingInt(this::ordinalMax));
        return combos;
    }

    /**
     * Retourne l'ordinal maximal d'une combinaison (sa "force").
     */
    protected int ordinalMax(Combination c) {
        return c.getCards().stream()
                .mapToInt(card -> card.getValue().ordinal())
                .max().orElse(0);
    }
}