package engine.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Système de logs pour le jeu
 * - Écrit dans un fichier
 * - Peut être affiché dans l'interface GUI
 */
public class GameLogger {
    
    private static GameLogger instance;
    private List<LogListener> listeners;
    private PrintWriter fileWriter;
    private String logFileName;
    
    private GameLogger() {
        listeners = new ArrayList<>();
        initLogFile();
    }
    
    public static GameLogger getInstance() {
        if (instance == null) {
            instance = new GameLogger();
        }
        return instance;
    }
    
    private void initLogFile() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = dtf.format(LocalDateTime.now());
        logFileName = "logs/game_" + timestamp + ".log";
        
        try {
            new java.io.File("logs").mkdirs();
            fileWriter = new PrintWriter(new FileWriter(logFileName, true));
            logToFile("=== DEBUT DE LA PARTIE ===");
        } catch (IOException e) {
            System.err.println("Impossible de créer le fichier de log: " + e.getMessage());
        }
    }
    
    public void addListener(LogListener listener) {
        listeners.add(listener);
    }
    
    private void logToFile(String message) {
        if (fileWriter != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
            fileWriter.println("[" + timestamp + "] " + message);
            fileWriter.flush();
        }
    }
    
    public void log(String message) {
        logToFile(message);
        for (LogListener listener : listeners) {
            listener.onLog(message);
        }
    }
    
    public void logPlay(String playerName, String combinationDetail) {
        log("🎴 " + playerName + " → " + combinationDetail);
    }
    
    public void logDraw(String playerName) {
        log("🃏 " + playerName + " pioche");
    }
    
    public void logFirstPlayer(String playerName, String cardInfo) {
        log("🎯 " + playerName + " commence avec " + cardInfo);
    }
    
    public void logRoundEnd(int roundNumber) {
        log("=== FIN DU ROUND " + roundNumber + " ===");
    }
    
    public void logRoundStart(int roundNumber) {
        log("=== DEBUT DU ROUND " + roundNumber + " ===");
    }
    
    public void logGameStart() {
        log("🎮 Debut de la partie");
    }
    
    public void logWin(String playerName) {
        log("🏆 " + playerName + " a gagne !");
    }
    
    public void logError(String message) {
        log("❌ " + message);
    }
    
    public void close() {
        if (fileWriter != null) {
            logToFile("=== FIN DE LA PARTIE ===");
            fileWriter.close();
        }
    }
    
    public interface LogListener {
        void onLog(String message);
    }
}
