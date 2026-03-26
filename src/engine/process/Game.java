package engine.process;

import java.util.ArrayList;
import java.util.List;
import engine.data.*;

public class Game {
    private List<Player> players;
    private Deck deck;
    private Round currentRound;
    private int currentPlayerIndex;
    private Combination lastCombination;
    private int playersPassed;
    private Player humanPlayer;

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

        // Deal cards
        initializeHands();
        
        // Determine first player (ignoring 2 and Jokers)
        this.currentPlayerIndex = findStartingPlayerIndex();
        
        // Fallback if no player found
        if (this.currentPlayerIndex == -1) {
            this.currentPlayerIndex = players.indexOf(humanPlayer);
        }
        
        this.currentRound = new Round(1, getCurrentPlayer(), deck);
    }

    private void initializeHands() {
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                if (!deck.isEmpty()) player.drawCard(deck);
            }
        }
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Tente de jouer une combinaison pour le joueur courant.
     * @return true si la combinaison a bien été jouée , false sinon.
     */
    public boolean playCombination(Combination combination) {
        Player currentPlayer = getCurrentPlayer();

        if (!combination.canBeat(lastCombination)) {
            System.out.println("Cette combinaison ne peut pas battre la précédente !");
            return false;
        }

        if (!currentPlayer.playCombination(combination)) {
            return false;
        }

        // Succès
        lastCombination = combination;
        playersPassed = 0;
        System.out.println(currentPlayer.getId() + " a joué : " + combination);

        if (!currentPlayer.hasCard()) {
            System.out.println(currentPlayer.getId() + " a gagné !");
            return true; // partie terminée 
        }

        nextTurn();
        return true;
    }

    public void pass() {
        System.out.println(getCurrentPlayer().getId() + " passe.");
        playersPassed++;

        if (playersPassed >= players.size() - 1) {
            endRound();
        } else {
            nextTurn();
        }
    }

    public void drawCard() {
        Player current = getCurrentPlayer();
        if (!deck.isEmpty()) {
            current.drawCard(deck);
            System.out.println(current.getId() + " a pioché.");
            nextTurn();
        } else {
            System.out.println("Deck vide !");
        }
    }
    
    
    /**
     * Finds the index of the player who starts the game
     * Rule: smallest card (excluding 2 and Joker)
     * Suit order: DIAMONDS < CLUBS < HEARTS < SPADES
     */
    private int findStartingPlayerIndex() {
        Card smallestCard = null;
        int startingIndex = -1;
        
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            
            for (Card card : player.getHand()) {
                // Ignore Jokers
                if (card.getValue() == Card.Value.JOKER) {
                    continue;
                }
                
                // Ignore 2s (special card)
                if (card.getValue() == Card.Value.TWO) {
                    continue;
                }
                
                if (smallestCard == null || isSmaller(card, smallestCard)) {
                    smallestCard = card;
                    startingIndex = i;
                }
            }
        }
        
        if (startingIndex != -1 && smallestCard != null) {
            System.out.println("🏆 First player: " + players.get(startingIndex).getId() 
                               + " with " + smallestCard.getValue().getSymbol() 
                               + " " + smallestCard.getSuit().getSymbol());
        }
        
        return startingIndex;
    }
    
    /**
     * Compares two cards to determine which is smaller
     * @return true if c1 is smaller than c2
     */
    private boolean isSmaller(Card c1, Card c2) {
        // First compare value
        int val1 = c1.getValue().ordinal();
        int val2 = c2.getValue().ordinal();
        
        if (val1 != val2) {
            return val1 < val2;
        }
        
        // If values are equal, compare suit
        return getSuitOrder(c1.getSuit()) < getSuitOrder(c2.getSuit());
    }
    
    /**
     * Returns the priority order of a suit
     * DIAMONDS (0) < CLUBS (1) < HEARTS (2) < SPADES (3)
     */
    private int getSuitOrder(Card.Suit suit) {
        switch (suit) {
            case DIAMONDS: return 0;
            case CLUBS:    return 1;
            case HEARTS:   return 2;
            case SPADES:   return 3;
            default:       return 4;
        }
    }
    
    

    private void endRound() {
        System.out.println("=== FIN DU ROUND " + currentRound.getRoundNumber() + " ===");
        for (Player player : players) {
            if (!deck.isEmpty()) player.drawCard(deck);
        }
        lastCombination = null;
        playersPassed = 0;
        currentRound.endRound();
        currentRound.nextRound();
        currentPlayerIndex = players.indexOf(humanPlayer);
        System.out.println("=== DÉBUT DU ROUND " + currentRound.getRoundNumber() + " ===");
    }

    public boolean isHumanTurn() {
        return getCurrentPlayer() == humanPlayer;
    }

    public boolean isGameOver() {
        for (Player player : players) {
            if (!player.hasCard()) return true;
        }
        return false;
    }

    public Player getWinner() {
        for (Player player : players) {
            if (!player.hasCard()) return player;
        }
        return null;
    }

    public Deck getDeck()                  { return deck; }
    public List<Player> getPlayers()       { return new ArrayList<>(players); }
    public Round getCurrentRound()         { return currentRound; }
    public Combination getLastCombination(){ return lastCombination; }
}
