package engine.process;

import java.util.List;
import engine.mobile.Card;

public class Combinaison {
    private List<Card> cartes;
    private TypeCombinaison type;

    public Combinaison(List<Card> cartes, TypeCombinaison type) {
        this.cartes = cartes;
        this.type = type;
    }

    public List<Card> getCartes() { return cartes; }
    public TypeCombinaison getType() { return type; }

    public boolean estValide() { return type != null && !type.getNom().equals("Invalide"); }

    // Comparer avec la combinaison précédente
    public boolean peutEcraser(Combinaison precedente) {
    if(precedente == null) return true; // première combinaison du tour

    // Double Joker écrase tout
    if(this.type.getNom().equals("Double_Joker")) return true;

    // Bombe écrase tout sauf Double Joker
    if(this.type.getNom().equals("Bombe") && !precedente.getType().getNom().equals("Bombe"))
        return true;

    // Vérifier même type et même nombre de cartes
    if(this.type.getNom().equals(precedente.getType().getNom())
       && this.cartes.size() == precedente.getCartes().size()) {

        // Chaque carte doit être exactement +1 par rapport à l'adversaire
        for(int i = 0; i < this.cartes.size(); i++) {
            int valeurThis = this.cartes.get(i).getValue().ordinal();
            int valeurPrec = precedente.getCartes().get(i).getValue().ordinal();

            if(valeurThis != valeurPrec + 1) {
                return false; // ne suit pas la règle +1
            }
        }
        return true; // toutes les cartes respectent +1
    }

    return false; // type différent, nombre différent ou règle +1 non respectée
}

    // Détermine le type de la combinaison
    public static TypeCombinaison determinerType(List<Card> cartes) {
        if(cartes.isEmpty()) return new TypeCombinaison("Invalide");
        if(cartes.size() == 1) return new TypeCombinaison("Simple");
        if(cartes.size() == 2) {
            if(cartes.get(0).getValue() == cartes.get(1).getValue()) return new TypeCombinaison("Double");
            if(cartes.get(0).getValue() == Card.Value.JOKER && cartes.get(1).getValue() == Card.Value.JOKER) return new TypeCombinaison("Double_Joker");
        }
        if(cartes.size() == 3) return new TypeCombinaison("Bombe"); // triple = bombe
        if(cartes.size() >= 3) {
            // série check
            boolean serie = true;
            for(int i=1;i<cartes.size();i++) {
                if(cartes.get(i).getValue().ordinal() != cartes.get(i-1).getValue().ordinal()+1) serie = false;
            }
            if(serie) return new TypeCombinaison("Serie");
        }
        return new TypeCombinaison("Invalide");
    }
}