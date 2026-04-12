package engine.process;

import java.util.ArrayList;
import java.util.List;
import engine.data.*;
import log.LoggerUtility;
import org.apache.log4j.Logger;

public class Game {

	private static final Logger logger = LoggerUtility.getLogger(Game.class, "html");

	private List<Player> players;
	private Deck deck;
	private Round currentRound;
	private int currentPlayerIndex;
	private Combination lastCombination;
	private Player humanPlayer;
	private List<Card> discardPile;
	private List<Card> allPlayedCards;
	private int turnsInRound;
	private int passCount = 0;
	private Player lastPlayerWhoPlayed = null;

	public Game(List<Player> players, Deck deck) {
		this.players = new ArrayList<>(players);
		this.discardPile = new ArrayList<>();
		this.allPlayedCards = new ArrayList<>();
		this.deck = deck;
		this.lastCombination = null;
		this.turnsInRound = 0;

		for (Player p : players) {
			if (p.getId().equals("Vous")) {
				this.humanPlayer = p;
				break;
			}
		}

		logger.info("=== NOUVELLE PARTIE DEMARREE ===");
		logger.info("Nombre de joueurs : " + players.size());

		initializeHands();

		this.currentPlayerIndex = findStartingPlayerIndex();

		if (this.currentPlayerIndex == -1) {
			logger.warn("Aucun joueur de depart trouve, on prend le joueur humain par defaut.");
			this.currentPlayerIndex = players.indexOf(humanPlayer);
		}

		this.currentRound = new Round(1, getCurrentPlayer(), deck);
		logger.info("Premier joueur : " + getCurrentPlayer().getId());
	}

	private void initializeHands() {
		for (Player player : players) {
			for (int i = 0; i < 5; i++) {
				if (!deck.isEmpty()) player.drawCard(deck);
			}
		}
		logger.info("Distribution initiale terminee. Cartes restantes dans le deck : " + deck.size());
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
			logger.warn(currentPlayer.getId() + " a joue une combinaison qui ne bat pas la precedente.");
			return false;
		}

		if (!currentPlayer.playCombination(combination)) {
			logger.warn(currentPlayer.getId() + " ne possede pas les cartes de cette combinaison.");
			return false;
		}

		if (lastCombination != null) discardPile.addAll(lastCombination.getCards());
		allPlayedCards.addAll(combination.getCards());
		discardPile.clear();
		int cardsToKeep = allPlayedCards.size() - combination.getCards().size();
		for (int i = 0; i < cardsToKeep; i++) {   			
			discardPile.add(allPlayedCards.get(i));
		}

		lastCombination = combination;
		lastPlayerWhoPlayed = currentPlayer;
		passCount = 0;
		logger.info(currentPlayer.getId() + " a joue : " + combination.toString());

		if (!currentPlayer.hasCard()) {
			logger.info("=== " + currentPlayer.getId() + " A GAGNE LA PARTIE ! ===");
			return true;
		}

		nextTurn();
		if (deck.isEmpty() && !discardPile.isEmpty()) reshuffleDiscardPile();
		return true;
	}

	private void reshuffleDiscardPile() {
		logger.info("Deck vide ! Recyclage de " + discardPile.size() + " cartes...");
		for (Card card : discardPile) deck.getCards().add(card);
		allPlayedCards.clear();
		if (lastCombination != null) allPlayedCards.addAll(lastCombination.getCards());
		discardPile.clear();
		deck.shuffle();
		logger.info("Nouveau deck avec " + deck.size() + " cartes.");
	}

	public void drawCard() {
    Player current = getCurrentPlayer();

    if (deck.isEmpty()) {
        if (discardPile.isEmpty()) {
            logger.warn("Deck vide et aucune carte a recycler !");
            passCount++;
            if (passCount >= players.size() - 1 && lastPlayerWhoPlayed != null && current == lastPlayerWhoPlayed) {
                handleResetOrPass();
                return;
            }
            nextTurn();
            return;
        }
        reshuffleDiscardPile();
    }

    current.drawCard(deck);
    logger.info(current.getId() + " a passe (pioche). Cartes restantes : " + deck.size());

    passCount++;

    if (passCount >= players.size() - 1 && lastPlayerWhoPlayed != null && current == lastPlayerWhoPlayed) {
        handleResetOrPass();
        return;
    }

    nextTurn();
}

private void handleResetOrPass() {
    // Ne pas reset si c'est un 2
    if (lastCombination != null && lastCombination.getType() == CombinationType.SIMPLE &&
        lastCombination.getCards().get(0).getValue() == Card.Value.TWO) {
        logger.info("Carte 2 sur le tas, pas de reset.");
        passCount = 0;
        nextTurn();  // Continuer normalement
    } else {
        resetTurn();  // Reset pour bombe ou combinaison normale
    }
}

	private void resetTurn() {
    logger.info("=== RESET DU TOUR ===");
    
    // Recycler les cartes de la défausse
    if (!discardPile.isEmpty()) {
        for (Card card : discardPile) {
            deck.getCards().add(card);
        }
        discardPile.clear();
        deck.shuffle();
    }
    
    lastCombination = null;
    passCount = 0;
    currentPlayerIndex = players.indexOf(lastPlayerWhoPlayed);
    turnsInRound = 0;
}

	private int findStartingPlayerIndex() {
		Card smallestCard = null;
		int startingIndex = -1;
		for (int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			for (Card card : player.getHand()) {
				if (card.getValue() == Card.Value.JOKER) continue;
				if (card.getValue() == Card.Value.TWO) continue;
				if (smallestCard == null || isSmaller(card, smallestCard)) {
					smallestCard = card;
					startingIndex = i;
				}
			}
		}
		if (startingIndex != -1 && smallestCard != null) {
			logger.info("Joueur de depart : " + players.get(startingIndex).getId()
					+ " avec " + smallestCard.getValue().getSymbol()
					+ " " + smallestCard.getSuit().getSymbol());
		}
		return startingIndex;
	}

	private boolean isSmaller(Card c1, Card c2) {
		int val1 = c1.getValue().ordinal();
		int val2 = c2.getValue().ordinal();
		if (val1 != val2) return val1 < val2;
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
		logger.info("=== FIN DU ROUND " + currentRound.getRoundNumber() + " ===");
		for (Player player : players) {
			if (!deck.isEmpty()) player.drawCard(deck);
		}
		turnsInRound = 0;
		currentRound.endRound();
		currentRound.nextRound();
		currentPlayerIndex = players.indexOf(humanPlayer);
		logger.info("=== DEBUT DU ROUND " + currentRound.getRoundNumber() + " ===");
	}

	public boolean isHumanTurn()  { return getCurrentPlayer() == humanPlayer; }
	public boolean isGameOver() {
		for (Player player : players) { if (!player.hasCard()) return true; }
		return false;
	}
	public Player getWinner() {
		for (Player player : players) { if (!player.hasCard()) return player; }
		return null;
	}
	public Deck getDeck()                   { return deck; }
	public List<Player> getPlayers()        { return new ArrayList<>(players); }
	public Round getCurrentRound()          { return currentRound; }
	public Combination getLastCombination() { return lastCombination; }
}
