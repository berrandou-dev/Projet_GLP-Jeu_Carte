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
    private JButton drawButton;
    private static final String DICE   = "\uD83C\uDFB2";
    private static final String PLAYER = "\uD83D\uDC64";
    private static final String CARD   = "\uD83C\uDCCF";

    private final Color COLOR_DRAW = new Color(60, 120, 180);
    private final Color COLOR_DISABLED = new Color(100, 100, 100);

    public GameInfoBar(Game game, MainGUI mainGUI) {
        this.game = game;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 80));
        setBackground(new Color(40, 40, 60));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 215, 0)));

        // Info labels
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        infoPanel.setBackground(new Color(40, 40, 60));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        roundLabel = makeLabel(DICE + " Round: 1", Color.WHITE, GameConfig.FONT_LARGE);
        currentPlayerLabel = makeLabel(PLAYER + " " + game.getCurrentPlayer().getId(), Color.WHITE, GameConfig.FONT_LARGE);
        lastCombinationLabel = makeLabel(CARD + " Aucune", new Color(255, 200, 100), GameConfig.FONT_LARGE);

        infoPanel.add(roundLabel);
        infoPanel.add(currentPlayerLabel);
        infoPanel.add(lastCombinationLabel);

        // Bouton PIOCHER uniquement
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBackground(new Color(40, 40, 60));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 15));

        drawButton = makeButton("\uD83C\uDCCF PIOCHER", COLOR_DRAW);
        drawButton.addActionListener(e -> mainGUI.onHumanDraw());

        controlPanel.add(drawButton);  // Seulement le bouton pioche

        add(infoPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
    }

    public void setControlsEnabled(boolean enabled) {
        drawButton.setEnabled(enabled);
        drawButton.setBackground(enabled ? COLOR_DRAW : COLOR_DISABLED);
    }

    public void updateDisplay() {
        roundLabel.setText(DICE + " Round: " + game.getCurrentRound().getRoundNumber());

        String id = game.getCurrentPlayer().getId();
        if (id.equals("Vous")) {
            currentPlayerLabel.setText(PLAYER + " À VOUS !");
            currentPlayerLabel.setForeground(new Color(255, 215, 0));
        } else {
            currentPlayerLabel.setText(PLAYER + " " + id + " réfléchit…");
            currentPlayerLabel.setForeground(new Color(180, 180, 180));
        }

        Combination last = game.getLastCombination();
        if (last != null) {
            lastCombinationLabel.setText(CARD + " " + last.getType() + " (" + last.getCards().size() + ")");
            lastCombinationLabel.setForeground(new Color(100, 200, 100));
        } else {
            lastCombinationLabel.setText(CARD + " Aucune");
            lastCombinationLabel.setForeground(new Color(255, 200, 100));
        }
        

        repaint();
    }

    private JLabel makeLabel(String text, Color color, int size) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setForeground(color);
        lbl.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, size));
        return lbl;
    }

    private JButton makeButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(color);
            }
        });
        return btn;
    }
}
