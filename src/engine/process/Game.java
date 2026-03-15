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
    private int playersPassed;
    private Player humanPlayer;
    private static final int PASS_LIMIT = 3;
    
    public Game(List<Player> players, Deck deck) {
        this.players = new ArrayList<>(players);
        this.deck = deck;
        this.lastCombination = null;
        this.playersPassed = 0;
        
        for (Player p : players) {
            if (p.getId().equals("Vous")) {
                this.humanPlayer = p;
                break;
            }
        }

        this.currentPlayerIndex = players.indexOf(humanPlayer);
        
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
    
    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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
            playersPassed = 0;
            System.out.println(currentPlayer.getId() + " a joué: " + combination);
            
            if (!currentPlayer.hasCard()) {
                System.out.println(currentPlayer.getId() + " a gagné la partie !");
                return true;
            }
            
            nextTurn();
            return true;
        }
        
        return false;
    }
    
    public void pass() {
        System.out.println(getCurrentPlayer().getId() + " passe son tour");
        playersPassed++;
        
        if (playersPassed >= players.size() - 1) {
            endRound();
        } else {
            nextTurn();
        }
    }
    
    public void drawCard() {
        Player currentPlayer = getCurrentPlayer();
        if (!deck.isEmpty()) {
            currentPlayer.drawCard(deck);
            System.out.println(currentPlayer.getId() + " a pioché 1 carte");
            nextTurn();
        } else {
            System.out.println("Le deck est vide !");
        }
    }
    
    private void endRound() {
        System.out.println("=== FIN DU ROUND " + currentRound.getRoundNumber() + " ===");
        
        for (Player player : players) {
            if (!deck.isEmpty()) {
                player.drawCard(deck);
                System.out.println(player.getId() + " pioche 1 carte");
            }
        }
        
        lastCombination = null;
        playersPassed = 0;
        currentRound.endRound();
        currentRound.nextRound();
        

        currentPlayerIndex = players.indexOf(humanPlayer);
        
        System.out.println("=== DÉBUT DU ROUND " + currentRound.getRoundNumber() + " ===");
        System.out.println("C'est à " + getCurrentPlayer().getId() + " de jouer !");
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
