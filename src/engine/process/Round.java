package engine.process;

import engine.data.Player;
import engine.data.Deck;

/**
 * Manages a single round of the game.
 */
public class Round {
    private int roundNumber;
    private Player currentPlayer;
    private Deck deck;
    private boolean roundComplete;

    public Round(int roundNumber, Player player, Deck deck) {
        this.roundNumber = roundNumber;
        this.currentPlayer = player;
        this.deck = deck;
        this.roundComplete = false;
    }

    public void endRound() { this.roundComplete = true; }
    public void nextRound() { roundNumber++; roundComplete = false; }
    public boolean isRoundComplete() { return roundComplete; }
    public int getRoundNumber() { return roundNumber; }
    public Player getCurrentPlayer() { return currentPlayer; }
}
