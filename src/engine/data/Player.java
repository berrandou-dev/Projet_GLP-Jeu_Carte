package engine.data;

import java.util.ArrayList;
import java.util.List;
import engine.process.*;

public class Player {

    private String id;
    private List<Card> hand;
    
    public Player(String id){
        this.id = id;
        hand = new ArrayList<>();
    }
    
    public boolean playCombination(Combination c){
        if (c == null) {
            return false;
        }
        
        if (!c.isValid()) {
            return false;
        }
        
        List<Card> cardsToPlay = c.getCards();
        
        if (!handContainsAll(cardsToPlay)) {
            return false;
        }
        
        for (Card card : cardsToPlay) {
            hand.remove(card);
        }
        
        return true;
    }
    
    private boolean handContainsAll(List<Card> cards) {
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
