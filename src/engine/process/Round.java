package process;

import data.*;

public class Round {
    private int roundNumber;
    private Player currentPlayer;
    private Deck deck;
    
    public Round(int roundNumber, Player player, Deck deck) {
        this.roundNumber = roundNumber;
        this.currentPlayer = player;
        this.deck = deck;
    }

    public void nextRound() {
        this.roundNumber++;
        // NE RIEN METTRE ICI - PAS DE drawCard() pour l'instant
    }

    public int getRoundNumber() {
        return roundNumber;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
