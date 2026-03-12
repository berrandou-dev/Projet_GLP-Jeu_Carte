package engine.process;
public class TypeCombinaison implements Comparable<TypeCombinaison> {
    private String nom;

    // Constructeur
    public TypeCombinaison(String nom) {
        if (!nom.equals("Simple") && !nom.equals("Double") &&
            !nom.equals("Serie") && !nom.equals("Bombe") &&
            !nom.equals("Double_Joker")) {
            throw new IllegalArgumentException("Type de combinaison invalide: " + nom);
        }
        this.nom = nom;
    }

    // Getter
    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return nom;
    }

    // Définir un ordre pour comparer les types
    private int getValeur() {
        switch(nom) {
            case "Simple": return 1;
            case "Double": return 2;
            case "Serie": return 3;
            case "Bombe": return 4;
            case "Double_Joker": return 5;
            default: return 0; // ne devrait jamais arriver
        }
    }

    @Override
    public int compareTo(TypeCombinaison other) {
        return Integer.compare(this.getValeur(), other.getValeur());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        TypeCombinaison other = (TypeCombinaison) obj;
        return this.nom.equals(other.nom);
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }
}