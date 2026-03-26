package engine.process;

import engine.data.*;
import java.util.*;

public abstract class BotPlayer extends Player {
    
    public BotPlayer(String id) {
        super(id);
    }
    
    public abstract Combination choisirCombinaison(List<Card> hand, Combination last);
    
    protected List<Combination> getCombinaisonsJouables(List<Card> hand, Combination last) {
        List<Combination> toutes = getToutesCombinaisonsValides(hand);
        if (last == null) return toutes;
        
        List<Combination> result = new ArrayList<>();
        for (Combination c : toutes) {
            if (c.canBeat(last)) {
                result.add(c);
            }
        }
        return result;
    }
    
    protected List<Combination> getToutesCombinaisonsValides(List<Card> hand) {
        List<Combination> result = new ArrayList<>();
        
        // Simples
        for (Card c : hand) {
            List<Card> single = new ArrayList<>();
            single.add(c);
            Combination combo = new Combination(single, CombinationType.SIMPLE);
            if (combo.isValid()) result.add(combo);
        }
        
        // Doubles
        for (int i = 0; i < hand.size(); i++) {
            for (int j = i + 1; j < hand.size(); j++) {
                List<Card> paire = new ArrayList<>();
                paire.add(hand.get(i));
                paire.add(hand.get(j));
                CombinationType t = Combination.determineType(paire);
                if (t == CombinationType.DOUBLE || t == CombinationType.DOUBLE_JOKER) {
                    result.add(new Combination(paire, t));
                }
            }
        }
        
        // Bombes (4 cartes identiques)
        Map<Card.Value, List<Card>> groupes = grouperParValeur(hand);
        for (List<Card> cartes : groupes.values()) {
            if (cartes.size() >= 4) {
                result.add(new Combination(cartes.subList(0, 4), CombinationType.BOMB));
            }
        }
        
        // Séries
        result.addAll(trouverSeries(hand));
        
        // Filtrer les combinaisons valides
        List<Combination> valides = new ArrayList<>();
        for (Combination c : result) {
            if (c.isValid()) valides.add(c);
        }
        return valides;
    }
    
    protected Map<Card.Value, List<Card>> grouperParValeur(List<Card> hand) {
        Map<Card.Value, List<Card>> map = new LinkedHashMap<>();
        for (Card c : hand) {
            List<Card> list = map.get(c.getValue());
            if (list == null) {
                list = new ArrayList<>();
                map.put(c.getValue(), list);
            }
            list.add(c);
        }
        return map;
    }
    
    protected void trierParForce(List<Combination> combos) {
        combos.sort(new Comparator<Combination>() {
            @Override
            public int compare(Combination c1, Combination c2) {
                int max1 = 0, max2 = 0;
                for (Card card : c1.getCards()) {
                    max1 = Math.max(max1, card.getValue().ordinal());
                }
                for (Card card : c2.getCards()) {
                    max2 = Math.max(max2, card.getValue().ordinal());
                }
                return Integer.compare(max1, max2);
            }
        });
    }
    
    private List<Combination> trouverSeries(List<Card> hand) {
        List<Combination> series = new ArrayList<>();
        
        // Trier les cartes non-Joker par valeur ordinale
        List<Card> triees = new ArrayList<>();
        for (Card c : hand) {
            if (c.getValue() != Card.Value.JOKER) {
                triees.add(c);
            }
        }
        triees.sort(new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return Integer.compare(c1.getValue().ordinal(), c2.getValue().ordinal());
            }
        });
        
        for (int debut = 0; debut < triees.size(); debut++) {
            List<Card> courante = new ArrayList<>();
            courante.add(triees.get(debut));
            int dernier = triees.get(debut).getValue().ordinal();
            
            for (int fin = debut + 1; fin < triees.size(); fin++) {
                int ordinal = triees.get(fin).getValue().ordinal();
                if (ordinal == dernier + 1) {
                    courante.add(triees.get(fin));
                    dernier = ordinal;
                    if (courante.size() >= 3) {
                        series.add(new Combination(new ArrayList<>(courante), CombinationType.SERIES));
                    }
                } else if (ordinal != dernier) {
                    break;
                }
            }
        }
        return series;
    }
}
