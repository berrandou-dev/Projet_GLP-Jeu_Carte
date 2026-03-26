package engine.process;

import java.util.ArrayList;
import java.util.List;
import engine.data.*;
import config.GameConfig;

public class GameBuilder {

    public static Game buildGame(int nbJoueurs, String difficulte) {
        if (nbJoueurs < GameConfig.MIN_PLAYERS) nbJoueurs = GameConfig.MIN_PLAYERS;
        if (nbJoueurs > GameConfig.MAX_PLAYERS) nbJoueurs = GameConfig.MAX_PLAYERS;

        Deck deck = new Deck();
        List<Player> players = new ArrayList<>();

        // Joueur humain
        players.add(new Player("Vous"));

        // Robots selon la difficulté
        for (int i = 1; i < nbJoueurs; i++) {
        	players.add(createBot("Robot " + i, difficulte));
		}

        return new Game(players, deck);
    }

    private static BotPlayer createBot(String nom, String difficulte) {
    	switch (difficulte.toLowerCase()) {
        	case "facile": return new EasyBot(nom);
        	case "difficile": return new HardBot(nom);
        	default: return new MediumBot(nom);
    	}
	}

    public static Player getHumanPlayer(Game game) {
        for (Player p : game.getPlayers()) {
            if (p.getId().equals("Vous")) return p;
        }
        return null;
    }
}
