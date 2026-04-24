package config;

import java.awt.Color;

public class GameConfig {
    
    // Fenêtre
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 650;
    
    // Jeu
    public static final int INITIAL_HAND_SIZE = 5;
    public static final int MIN_PLAYERS = 3;
    public static final int MAX_PLAYERS = 5;
    
    // Cartes
    public static final int CARD_WIDTH = 100;
    public static final int CARD_HEIGHT = 150;
    
    // Couleurs
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color DECK_COLOR = new Color(0, 80, 150);
    public static final Color TABLE_COLOR = new Color(30, 120, 30);
    public static final Color INFO_PANEL_COLOR = new Color(70, 70, 70);
    
    // Police
    public static final String FONT_NAME = "Arial";
    public static final int FONT_SMALL = 12;
    public static final int FONT_MEDIUM = 14;
    public static final int FONT_LARGE = 16;
    public static final int FONT_TITLE = 18;
}
