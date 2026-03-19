package engine.data;


import java.util.ArrayList;
import java.util.List;
import engine.process.*;

public class Player {

    // Attributes
    private String id;
    private List<Card> hand;
    
    // Constructor
    public Player(String id){
        this.id = id;
        hand = new ArrayList<>();
    }
    
    // Méthodes
    public boolean playCombination(Combination c){
        if (c == null) {
            System.out.println(id + " ne peut pas jouer une combinaison null !");
            return false;
        }
        
        // Vérifier que la combinaison est valide
        if (!c.isValid()) {
            System.out.println(id + " ne peut pas jouer une combinaison invalide !");
            return false;
        }
        
        List<Card> cardsToPlay = c.getCards();
        
        // Vérifier que le joueur a toutes ces cartes en main
        if (!handContainsAll(cardsToPlay)) {
            System.out.println(id + " n'a pas toutes les cartes de cette combinaison en main !");
            return false;
        }
        
        // Retirer les cartes de la main
        for (Card card : cardsToPlay) {
            hand.remove(card);
        }
        
        System.out.println(id + " a joué : " + c);
        return true;
    }
    
    private boolean handContainsAll(List<Card> cards) {
        // Créer une copie de la main pour ne pas modifier l'originale
        List<Card> handCopy = new ArrayList<>(hand);
        
        for (Card card : cards) {
            if (!handCopy.remove(card)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean canPlay(Combination c) {
        if (c == null || !c.isValid()) return false;
        return handContainsAll(c.getCards());
    }
    
    public void drawCard(Deck deck){
        if (!deck.isEmpty()) {
            Card card = deck.draw();
            hand.add(card);
        }
        else {
            System.out.println(id + " ne peut pas piocher, le deck est vide !");
        }
    }

    public boolean hasCard(){
        return !hand.isEmpty();
    }
    
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }
    
    public String getId() {
        return id;
    }
    
    public int getHandSize() {
        return hand.size();
    }
}
