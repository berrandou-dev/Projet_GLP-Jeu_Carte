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
            players.add(creerRobot("Robot " + i, difficulte));
        }

        return new Game(players, deck);
    }

    private static Player creerRobot(String nom, String difficulte) {
        if (difficulte == null) return new RobotMoyen(nom);
        switch (difficulte.trim().toLowerCase()) {
            case "facile":    return new RobotFacile(nom);
            case "difficile": return new RobotDifficile(nom);
            default:          return new RobotMoyen(nom);
        }
    }

    public static Player getHumanPlayer(Game game) {
        for (Player p : game.getPlayers()) {
            if (p.getId().equals("Vous")) return p;
        }
        return null;
    }
}