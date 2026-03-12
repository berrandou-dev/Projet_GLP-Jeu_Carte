package engine;
package data;

public class Round {
    private int roundNumber = 0;
    
    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public void nextRound() {
        this.roundNumber++;
        drawCard();
    }

    public void  initRound() {
        this.roundNumber = 0;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}
