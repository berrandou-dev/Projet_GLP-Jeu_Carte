package engine.process;

import java.util.ArrayList;
import java.util.List;
import engine.data.*;
import config.GameConfig;

public class GameBuilder {
    
    public static Game buildGame(int nbJoueurs, String difficulte) {
        // Vérifier le nombre de joueurs
        if (nbJoueurs < GameConfig.MIN_PLAYERS) {
            System.out.println("Nombre minimum de joueurs: " + GameConfig.MIN_PLAYERS);
            nbJoueurs = GameConfig.MIN_PLAYERS;
        }
        if (nbJoueurs > GameConfig.MAX_PLAYERS) {
            System.out.println("Nombre maximum de joueurs: " + GameConfig.MAX_PLAYERS);
            nbJoueurs = GameConfig.MAX_PLAYERS;
        }
        
        Deck deck = new Deck();
        List<Player> players = new ArrayList<>();
        
        // Joueur humain
        players.add(new Player("Vous"));
        
        // Créer les bots selon la difficulté
        for (int i = 1; i < nbJoueurs; i++) {
            String botName = "Robot " + i;
            Player bot = new Player(botName);
            players.add(bot);
        }
        
        // Créer et retourner la partie
        return new Game(players, deck);
    }
    
    // Méthode utilitaire pour obtenir le joueur humain
    public static Player getHumanPlayer(Game game) {
        for (Player p : game.getPlayers()) {
            if (p.getId().equals("Vous")) {
                return p;
            }
        }
        return null;
    }
}
