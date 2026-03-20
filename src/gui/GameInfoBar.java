package gui;

import javax.swing.*;
import java.awt.*;
import engine.process.*;
import config.GameConfig;

public class GameInfoBar extends JPanel {
    private Game game;
    private JLabel roundLabel;
    private JLabel currentPlayerLabel;
    private JLabel lastCombinationLabel;
    private JButton passButton;
    private JButton drawButton;
    private MainGUI mainGUI;
    
    // Emojis en notation u
    private static final String DICE = "\uD83C\uDFB2";
    private static final String PLAYER = "\uD83D\uDC64";
    private static final String CARD = "\uD83C\uDCCF";
    private static final String PASS = "\u23ED\uFE0F";
    private static final String DRAW = "\uD83C\uDCCF";
    
    public GameInfoBar(Game game, MainGUI mainGUI) {
        this.game = game;
        this.mainGUI = mainGUI;
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 80));
        setBackground(new Color(40, 40, 60));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 215, 0)));
        
        // Panneau d'information
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        infoPanel.setBackground(new Color(40, 40, 60));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Round
        roundLabel = new JLabel(DICE + " Round: 1");
        roundLabel.setForeground(Color.WHITE);
        roundLabel.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, GameConfig.FONT_LARGE));
        roundLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Joueur courant
        currentPlayerLabel = new JLabel(PLAYER + " " + game.getCurrentPlayer().getId());
        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, GameConfig.FONT_LARGE));
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Dernière combinaison
        lastCombinationLabel = new JLabel(CARD + " Aucune");
        lastCombinationLabel.setForeground(new Color(255, 200, 100));
        lastCombinationLabel.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, GameConfig.FONT_LARGE));
        lastCombinationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        infoPanel.add(roundLabel);
        infoPanel.add(currentPlayerLabel);
        infoPanel.add(lastCombinationLabel);
        
        // Panneau de contrôle
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(new Color(40, 40, 60));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 15));
        
        passButton = createStyledButton(PASS + " PASSER", new Color(180, 60, 60));
        drawButton = createStyledButton(DRAW + " PIOCHER", new Color(60, 120, 180));
        
        controlPanel.add(passButton);
        controlPanel.add(drawButton);
        
        add(infoPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        button.addActionListener(e -> {
            if (button == passButton) {
                pass();
            } else {
                drawCard();
            }
        });
        
        return button;
    }
    
    private void pass() {
        game.pass();
        updateDisplay();
        if (mainGUI != null) {
            mainGUI.refreshDisplay();
            mainGUI.refreshHand();
        }
    }
    
    private void drawCard() {
        if (game.getCurrentPlayer().getId().equals("Vous")) {
            game.drawCard();
            
            if (mainGUI != null) {
                mainGUI.refreshHand();
                mainGUI.refreshHand();
            }
        }
        updateDisplay();
    }
    
    public void updateDisplay() {
        roundLabel.setText(DICE + " Round: " + game.getCurrentRound().getRoundNumber());
        
        String currentPlayer = game.getCurrentPlayer().getId();
        if (currentPlayer.equals("Vous")) {
            currentPlayerLabel.setText(PLAYER + " À VOUS !");
            currentPlayerLabel.setForeground(new Color(255, 215, 0));
            currentPlayerLabel.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, GameConfig.FONT_LARGE));
        } else {
            currentPlayerLabel.setText(PLAYER + " " + currentPlayer);
            currentPlayerLabel.setForeground(Color.WHITE);
            currentPlayerLabel.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, GameConfig.FONT_MEDIUM));
        }
    
        Combination lastCombo = game.getLastCombination();
        if (lastCombo != null) {
            String comboText = lastCombo.getType().toString();
            int nbCartes = lastCombo.getCards().size();
            lastCombinationLabel.setText(CARD + " " + comboText + " (" + nbCartes + ")");
            lastCombinationLabel.setForeground(new Color(100, 200, 100));
        } else {
            lastCombinationLabel.setText(CARD + " Aucune");
            lastCombinationLabel.setForeground(new Color(255, 200, 100));
        }
    
        if (mainGUI != null) {
            mainGUI.repaint();
        }
    
        repaint();
    }
}
