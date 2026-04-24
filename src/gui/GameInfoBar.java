package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import engine.process.*;
import config.GameConfig;

public class GameInfoBar extends JPanel {

    private Game    game;
    private JLabel  roundLabel;
    private JLabel  currentPlayerLabel;
    private JLabel  lastCombinationLabel;
    private JButton drawButton;
    private JButton btnMute;

    private static final String DICE   = "\uD83C\uDFB2";  // 🎲
    private static final String PLAYER = "\uD83D\uDC64";  // 👤
    private static final String CARD   = "\uD83C\uDCCF";  // 🃏

    private final Color COLOR_DRAW     = new Color(60, 120, 180);
    private final Color COLOR_DISABLED = new Color(100, 100, 100);

    /**
     * Permet d'afficher emojis ET texte latin dans le même composant.
     */
    private static Font emojiFont(int style, int size) {
        return new Font("Segoe UI Emoji", style, size);
    }

    public GameInfoBar(Game game, final MainGUI mainGUI) {
        this.game = game;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 80));
        setBackground(GameStyle.BG_DEEP);
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GameStyle.BORDER_GOLD));

        //Info labels 
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        infoPanel.setBackground(GameStyle.BG_DEEP);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // makeLabel utilise emojiFont → 🎲 👤 🃏 s'affichent correctement
        roundLabel           = makeLabel(DICE + " Round: 1",
                                GameStyle.TEXT_MAIN, GameConfig.FONT_LARGE);
        currentPlayerLabel   = makeLabel(PLAYER + " " + game.getCurrentPlayer().getId(),
                                GameStyle.TEXT_MAIN, GameConfig.FONT_LARGE);
        lastCombinationLabel = makeLabel(CARD + " Aucune",
                                GameStyle.GOLD_DIM,  GameConfig.FONT_LARGE);

        infoPanel.add(roundLabel);
        infoPanel.add(currentPlayerLabel);
        infoPanel.add(lastCombinationLabel);

        // Boutons Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    	controlPanel.setBackground(GameStyle.BG_DEEP);
    	controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 15));

        // Bouton Piocher (texte seul, pas d'emoji)
        drawButton = makeButton("PIOCHER", COLOR_DRAW);
    	drawButton.setPreferredSize(new Dimension(110, 45));
    	drawButton.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, 14));
    	drawButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
            	mainGUI.onHumanDraw();
        	}
    	});

        // Bouton Mute — emojiFont pour afficher 
        btnMute = GameStyle.blueButton("ON");
    	btnMute.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, 12));
    	btnMute.setForeground(Color.WHITE);
    	btnMute.setPreferredSize(new Dimension(70, 48));
    	btnMute.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
            	MusicPlayer.togglePause();
            	if (MusicPlayer.isPlaying()) {
                	btnMute.setText("ON");
            	} else {
                	btnMute.setText("MUTE");
            	}
        	}
    	});

        controlPanel.add(drawButton);
        controlPanel.add(btnMute);

        add(infoPanel,    BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
    }

    //Contrôles

    public void setControlsEnabled(boolean enabled) {
        drawButton.setEnabled(enabled);
        drawButton.setBackground(enabled ? COLOR_DRAW : COLOR_DISABLED);
    }

    // Mise à jour de l'affichage

    public void updateDisplay() {
        roundLabel.setText(DICE + " Round: "
                + game.getCurrentRound().getRoundNumber());

        String id = game.getCurrentPlayer().getId();
        if (id.equals("Vous")) {
            currentPlayerLabel.setText(PLAYER + " \u00C0 VOUS !");
            currentPlayerLabel.setForeground(GameStyle.GOLD);
        } else {
            currentPlayerLabel.setText(PLAYER + " " + id + " r\u00E9fl\u00E9chit\u2026");
            currentPlayerLabel.setForeground(GameStyle.TEXT_MUTED);
        }

        Combination last = game.getLastCombination();
        if (last != null) {
            lastCombinationLabel.setText(CARD + " "
                    + last.getType() + " (" + last.getCards().size() + ")");
            lastCombinationLabel.setForeground(GameStyle.GREEN_BRIGHT);
        } else {
            lastCombinationLabel.setText(CARD + " Aucune");
            lastCombinationLabel.setForeground(GameStyle.GOLD_DIM);
        }

        repaint();
    }

    // Factories

    /**
     * JLabel avec police emoji — supporte 🎲 👤 🃏 et texte latin.
     */
    private JLabel makeLabel(String text, Color color, int size) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setForeground(color);
        lbl.setFont(emojiFont(Font.BOLD, size));  // ← emojiFont, pas Arial
        return lbl;
    }

    private JButton makeButton(String text, final Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(color.brighter());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(color);
            }
        });
        return btn;
    }
}
