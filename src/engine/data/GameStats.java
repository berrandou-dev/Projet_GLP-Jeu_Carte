package engine.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks game statistics for all players throughout a game session.
 * Used to display the end-game screen with scores and detailed stats.
 *
 * Score rules:
 *   - Each card played = 1 jeton
 *   - BOMB played     = x2 multiplier on that turn's jetons
 *   - DOUBLE_JOKER played = x4 multiplier on that turn's jetons
 */
public class GameStats {

    /** Per-player statistics */
    public static class PlayerStats {
        private final String playerId;
        private int cardsPlayed;
        private int bombsPlayed;
        private int jokersPlayed;   // double-joker combos
        private int turnsPlayed;
        private int score;

        public PlayerStats(String playerId) {
            this.playerId = playerId;
            this.cardsPlayed = 0;
            this.bombsPlayed = 0;
            this.jokersPlayed = 0;
            this.turnsPlayed = 0;
            this.score = 0;
        }

        /** Record a combination played by this player and update the score. */
        public void recordCombination(engine.process.Combination combo) {
            if (combo == null) return;

            int nbCards = combo.getCards().size();
            CombinationType type = combo.getType();

            cardsPlayed += nbCards;
            turnsPlayed++;

            int jetons = nbCards; // base: 1 carte = 1 jeton
            if (type == CombinationType.BOMB) {
                bombsPlayed++;
                jetons = nbCards * 2; // x2
            } else if (type == CombinationType.DOUBLE_JOKER) {
                jokersPlayed++;
                jetons = nbCards * 4; // x4
            }
            score += jetons;
        }

        // ---- Getters ----

        public String getPlayerId()  { return playerId;    }
        public int getCardsPlayed()  { return cardsPlayed; }
        public int getBombsPlayed()  { return bombsPlayed; }
        public int getJokersPlayed() { return jokersPlayed;}
        public int getTurnsPlayed()  { return turnsPlayed; }
        public int getScore()        { return score;       }
    }

    // ---- GameStats fields ----

    private final Map<String, PlayerStats> statsMap = new HashMap<>();
    private String winnerId = null;
    private int totalRounds = 0;

    public GameStats() {}

    /** Ensures a player entry exists. Call during game setup. */
    public void registerPlayer(String playerId) {
        statsMap.putIfAbsent(playerId, new PlayerStats(playerId));
    }

    /** Record a combination for a player and update their stats. */
    public void recordCombination(String playerId, engine.process.Combination combo) {
        PlayerStats ps = statsMap.get(playerId);
        if (ps != null) ps.recordCombination(combo);
    }

    public void setWinner(String playerId) {
        this.winnerId = playerId;
    }

    public void setTotalRounds(int rounds) {
        this.totalRounds = rounds;
    }

    // ---- Getters ----

    public PlayerStats getStats(String playerId) {
        return statsMap.get(playerId);
    }

    public Map<String, PlayerStats> getAllStats() {
        return statsMap;
    }

    public String getWinnerId()   { return winnerId;     }
    public int    getTotalRounds(){ return totalRounds;  }
}
