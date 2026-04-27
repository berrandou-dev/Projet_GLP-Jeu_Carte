package engine.process;

import java.util.ArrayList;
import java.util.List;
import engine.data.*;
import config.GameConfig;
import log.LoggerUtility;
import org.apache.log4j.Logger;


/**
 * Factory for creating Game instances with bots at different difficulty levels.
 */
public class GameBuilder {

	private static final Logger logger = LoggerUtility.getLogger(GameBuilder.class, "html");

	public static Game buildGame(int nbJoueurs, String difficulte) {
		if (nbJoueurs < GameConfig.MIN_PLAYERS) {
			logger.warn("Nombre de joueurs trop faible (" + nbJoueurs + "), ajuste a " + GameConfig.MIN_PLAYERS);
			nbJoueurs = GameConfig.MIN_PLAYERS;
		}
		if (nbJoueurs > GameConfig.MAX_PLAYERS) {
			logger.warn("Nombre de joueurs trop eleve (" + nbJoueurs + "), ajuste a " + GameConfig.MAX_PLAYERS);
			nbJoueurs = GameConfig.MAX_PLAYERS;
		}

		logger.info("Construction d'une partie : " + nbJoueurs + " joueurs, difficulte '" + difficulte + "'");

		Deck deck = new Deck();
		List<Player> players = new ArrayList<>();
		players.add(new Player("Vous"));

		for (int i = 1; i < nbJoueurs; i++) {
			BotPlayer bot = createBot("Robot " + i, difficulte);
			players.add(bot);
			logger.info("Bot cree : " + bot.getId() + " (" + difficulte + ")");
		}

		return new Game(players, deck);
	}

	private static BotPlayer createBot(String nom, String difficulte) {
		switch (difficulte.toLowerCase()) {
			case "facile":    return new EasyBot(nom);
			case "difficile": return new HardBot(nom);
			default:          return new MediumBot(nom);
		}
	}

	public static Player getHumanPlayer(Game game) {
		for (Player p : game.getPlayers()) {
			if (p.getId().equals("Vous")) return p;
		}
		logger.error("Joueur humain introuvable dans la partie !");
		return null;
	}
}
