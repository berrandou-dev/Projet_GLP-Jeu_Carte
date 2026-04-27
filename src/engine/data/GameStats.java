package engine.data;

import java.util.HashMap;
import java.util.Map;
import engine.process.Combination;

/**
 * Tracks game statistics and scores for all players.
 * Score: 1 card = 1 point, BOMB = x2, DOUBLE_JOKER = x4.
 */
public class GameStats {

    public static class PlayerStats {
        private final String playerId;
        private int cardsPlayed, bombsPlayed, jokersPlayed, turnsPlayed, score;

        public PlayerStats(String playerId) { this.playerId = playerId; }

        public void recordCombination(Combination combo) {
            if (combo == null) return;
            int nbCards = combo.getCards().size();
            cardsPlayed += nbCards;
            turnsPlayed++;
            int jetons = nbCards;
            if (combo.getType() == CombinationType.BOMB) {
                bombsPlayed++;
                jetons = nbCards * 2;
            } else if (combo.getType() == CombinationType.DOUBLE_JOKER) {
                jokersPlayed++;
                jetons = nbCards * 4;
            }
            score += jetons;
        }

        public String getPlayerId() { return playerId; }
        public int getCardsPlayed() { return cardsPlayed; }
        public int getBombsPlayed() { return bombsPlayed; }
        public int getJokersPlayed() { return jokersPlayed; }
        public int getTurnsPlayed() { return turnsPlayed; }
        public int getScore() { return score; }
    }

    private final Map<String, PlayerStats> statsMap = new HashMap<>();
    private String winnerId = null;
    private int totalRounds = 0;

    public void registerPlayer(String playerId) {
        statsMap.putIfAbsent(playerId, new PlayerStats(playerId));
    }

    public void recordCombination(String playerId, Combination combo) {
        PlayerStats ps = statsMap.get(playerId);
        if (ps != null) ps.recordCombination(combo);
    }

    public void setWinner(String playerId) { this.winnerId = playerId; }
    public void setTotalRounds(int rounds) { this.totalRounds = rounds; }
    public PlayerStats getStats(String playerId) { return statsMap.get(playerId); }
    public Map<String, PlayerStats> getAllStats() { return statsMap; }
    public String getWinnerId() { return winnerId; }
    public int getTotalRounds() { return totalRounds; }
}
