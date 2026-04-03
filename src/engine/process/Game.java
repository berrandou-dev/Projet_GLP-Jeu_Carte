package engine.process;

import java.util.ArrayList;
import java.util.List;
import engine.data.*;
import engine.logger.GameLogger;

public class Game {
    private List<Player> players;
    private Deck deck;
    private Round currentRound;
    private int currentPlayerIndex;
    private Combination lastCombination;
    private Player humanPlayer;
    private List<Card> discardPile;
    private List<Card> allPlayedCards;
    private int turnsInRound;
    private GameLogger logger;

    public Game(List<Player> players, Deck deck) {
        this.players = new ArrayList<>(players);
        this.discardPile = new ArrayList<>();
        this.allPlayedCards = new ArrayList<>();
        this.deck = deck;
        this.lastCombination = null;
        this.turnsInRound = 0;
        this.logger = GameLogger.getInstance();

        for (Player p : players) {
            if (p.getId().equals("Vous")) {
                this.humanPlayer = p;
                break;
            }
        }

        initializeHands();
        this.currentPlayerIndex = findStartingPlayerIndex();
        
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
        turnsInRound++;
        
        if (turnsInRound >= players.size()) {
            endRound();
        }
    }

    public boolean playCombination(Combination combination) {
        Player currentPlayer = getCurrentPlayer();

        if (!combination.canBeat(lastCombination)) {
            logger.logError("Cette combinaison ne peut pas battre la precedente !");
            return false;
        }

        if (!currentPlayer.playCombination(combination)) {
            return false;
        }

        if (lastCombination != null) {
            discardPile.addAll(lastCombination.getCards());
        }
        
        allPlayedCards.addAll(combination.getCards());
        
        discardPile.clear();
        if (allPlayedCards.size() > combination.getCards().size()) {
            List<Card> temp = new ArrayList<>(allPlayedCards);
            temp.removeAll(combination.getCards());
            discardPile.addAll(temp);
        }
        
        lastCombination = combination;
        logger.logPlay(currentPlayer.getId(), combination.toString());

        if (!currentPlayer.hasCard()) {
            logger.logWin(currentPlayer.getId());
            return true;
        }

        nextTurn();
        
        if (deck.isEmpty() && !discardPile.isEmpty()) {
            reshuffleDiscardPile();
        }
        
        return true;
    }

    private void reshuffleDiscardPile() {
        logger.log("📦 Deck vide ! Recyclage de " + discardPile.size() + " cartes...");
        
        for (Card card : discardPile) {
            deck.getCards().add(card);
        }
        
        allPlayedCards.clear();
        if (lastCombination != null) {
            allPlayedCards.addAll(lastCombination.getCards());
        }
        
        discardPile.clear();
        deck.shuffle();
        
        logger.log("✅ Nouveau deck avec " + deck.size() + " cartes !");
    }

    public void drawCard() {
        Player current = getCurrentPlayer();
        
        if (deck.isEmpty()) {
            if (discardPile.isEmpty()) {
                logger.log("Deck vide et aucune carte a recycler !");
                nextTurn();
                return;
            }
            reshuffleDiscardPile();
        }
        
        current.drawCard(deck);
        logger.logDraw(current.getId());
        nextTurn();
    }

    private int findStartingPlayerIndex() {
        Card smallestCard = null;
        int startingIndex = -1;
        
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            
            for (Card card : player.getHand()) {
                if (card.getValue() == Card.Value.JOKER) {
                    continue;
                }
                
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
            logger.logFirstPlayer(players.get(startingIndex).getId(),
                smallestCard.getValue().getSymbol() + " " + smallestCard.getSuit().getSymbol());
        }
        
        return startingIndex;
    }
    
    private boolean isSmaller(Card c1, Card c2) {
        int val1 = c1.getValue().ordinal();
        int val2 = c2.getValue().ordinal();
        
        if (val1 != val2) {
            return val1 < val2;
        }
        
        return getSuitOrder(c1.getSuit()) < getSuitOrder(c2.getSuit());
    }
    
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
        logger.logRoundEnd(currentRound.getRoundNumber());
        
        for (Player player : players) {
            if (!deck.isEmpty()) player.drawCard(deck);
        }
        
        turnsInRound = 0;
        currentRound.endRound();
        currentRound.nextRound();
        currentPlayerIndex = players.indexOf(humanPlayer);
        logger.logRoundStart(currentRound.getRoundNumber());
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

    public Deck getDeck() { return deck; }
    public List<Player> getPlayers() { return new ArrayList<>(players); }
    public Round getCurrentRound() { return currentRound; }
    public Combination getLastCombination() { return lastCombination; }
}
