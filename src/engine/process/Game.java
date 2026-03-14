package process;

import java.util.ArrayList;
import java.util.List;
import data.*;

public class Game {
    private List<Player> players;
    private Deck deck;
    private Round currentRound;
    private int currentPlayerIndex;
    private Combination lastCombination;
    
    public Game(List<Player> players, Deck deck) {
        this.players = new ArrayList<>(players);
        this.deck = deck;
        this.currentPlayerIndex = 0;
        this.lastCombination = null;
        
        initializeHands();
        this.currentRound = new Round(1, getCurrentPlayer(), deck);
    }
    
    private void initializeHands() {
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                if (!deck.isEmpty()) {
                    player.drawCard(deck);
                }
            }
        }
    }
    
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentRound = new Round(currentRound.getRoundNumber() + 1, 
                                 getCurrentPlayer(), deck);
        System.out.println("Nouveau tour: " + getCurrentPlayer().getId());
    }
    
    public boolean playCombination(Combination combination) {
        Player currentPlayer = getCurrentPlayer();
        
        if (!combination.canBeat(lastCombination)) {
            System.out.println("Cette combinaison ne peut pas battre la précédente !");
            return false;
        }
        
        if (currentPlayer.playCombination(combination)) {
            lastCombination = combination;
            System.out.println(currentPlayer.getId() + " a joué: " + combination);
            
            if (!currentPlayer.hasCard()) {
                System.out.println(currentPlayer.getId() + " a gagné la partie !");
                return true;
            }
            
            return true;
        }
        
        return false;
    }
    
    public void pass() {
        System.out.println(getCurrentPlayer().getId() + " passe son tour");
        nextTurn();
    }
    
    public void drawCard() {
        Player currentPlayer = getCurrentPlayer();
        if (!deck.isEmpty()) {
            currentPlayer.drawCard(deck);
            System.out.println(currentPlayer.getId() + " a pioché 1 carte");
        } else {
            System.out.println("Le deck est vide !");
        }
    }
    
    public Deck getDeck() {
        return deck;
    }
    
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    
    public Round getCurrentRound() {
        return currentRound;
    }
    
    public Combination getLastCombination() {
        return lastCombination;
    }
    
    public boolean isGameOver() {
        for (Player player : players) {
            if (!player.hasCard()) {
                return true;
            }
        }
        return deck.isEmpty();
    }
    
    public Player getWinner() {
        for (Player player : players) {
            if (!player.hasCard()) {
                return player;
            }
        }
        return null;
    }
}
