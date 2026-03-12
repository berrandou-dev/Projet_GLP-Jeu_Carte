package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

	//Attributs
	private String id;
	private List<Card> hand;
	
	//Constructeur
	public Player(String id){
		this.id = id;
		hand = new ArrayList<>();
	}
	
	//Méthodes
	public void playCombination(Combination c){
		
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
		return hand;
	}
	
	public String getId() {
        return id;
    }

}
