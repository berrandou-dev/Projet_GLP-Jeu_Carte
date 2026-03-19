package engine.process;

import engine.data.*;

public class Round {
    private int roundNumber;
    private Player currentPlayer;
    private Deck deck;
    private boolean roundComplete;
    
    public Round(int roundNumber, Player player, Deck deck) {
        this.roundNumber = roundNumber;
        this.currentPlayer = player;
        this.setDeck(deck);
        this.roundComplete = false;
    }

    public void endRound() {
        this.roundComplete = true;
    }
    
    public void nextRound() {
        this.roundNumber++;
        this.roundComplete = false;
    }

    public boolean isRoundComplete() {
        return roundComplete;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}
}
