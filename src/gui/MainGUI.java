package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import engine.data.*;
import engine.process.*;
import config.GameConfig;

public class MainGUI extends JFrame {
    private Game game;
    private GameInfoBar gameInfoBar;
    private JPanel bottomPanel;
    private JLayeredPane layeredPane;
    private Player humanPlayer;
    private DeckPanel deckPanel;
    private JPanel pilePanel;
    private Card lastPlayedCard;

    public MainGUI(String title, int nbJoueurs, String difficulte) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        game = GameBuilder.buildGame(nbJoueurs, difficulte);
        humanPlayer = GameBuilder.getHumanPlayer(game);

        setupUI();
        setVisible(true);

        // Au démarrage, si un robot doit jouer en premier
        runRobotTurns();
    }

    /**
     * Lance le tour du robot courant avec un délai de 900ms, puis se rappelle
     * jusqu'à ce que ce soit le tour du joueur humain.
     */
    private void runRobotTurns() {
        if (game.isGameOver()) { showGameOver(); return; }
        if (game.isHumanTurn()) {
            // Rendre la main au joueur
            gameInfoBar.setControlsEnabled(true);
            gameInfoBar.updateDisplay();
            refreshHand();
            return;
        }

        // Tour robot → bloquer les contrôles
        gameInfoBar.setControlsEnabled(false);
        gameInfoBar.updateDisplay();

        Timer timer = new Timer(900, e -> {
    		if (game.isGameOver()) { showGameOver(); return; }

    		Player current = game.getCurrentPlayer();
    		if (current instanceof BotPlayer) {
        		BotPlayer bot = (BotPlayer) current;
        		Combination choix = bot.choisirCombinaison(current.getHand(), game.getLastCombination());

        		if (choix != null) {
            		game.playCombination(choix);
            		lastPlayedCard = choix.getCards().get(choix.getCards().size() - 1);
        		}
        		else {
            		game.pass();
        		}
    		}

    		refreshDisplay();

    		runRobotTurns();
		});
        timer.setRepeats(false);
        timer.start();
    }

    /** Bouton "JOUER LES CARTES SÉLECTIONNÉES" */
    public void playSelectedCards() {
        List<CardPanel> selected = CardPanel.getSelectedPanels();

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Sélectionnez d'abord des cartes à jouer !",
                "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Card> cardsToPlay = new ArrayList<>();
        for (CardPanel cp : selected) cardsToPlay.add(cp.getCard());

        CombinationType type = Combination.determineType(cardsToPlay);
        Combination combination = new Combination(cardsToPlay, type);

        boolean ok = game.playCombination(combination);
        if (ok) {
            lastPlayedCard = cardsToPlay.get(cardsToPlay.size() - 1);
            refreshHand();
            refreshDisplay();
            if (game.isGameOver()) { showGameOver(); return; }
            runRobotTurns(); // lancer les robots
        } else {
            JOptionPane.showMessageDialog(this,
                "Combinaison invalide ou ne bat pas la précédente !",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Bouton PASSER */
    public void onHumanPass() {
        game.pass();
        refreshDisplay();
        if (game.isGameOver()) { showGameOver(); return; }
        runRobotTurns();
    }

    /** Bouton PIOCHER */
    public void onHumanDraw() {
        game.drawCard();
        refreshHand();
        refreshDisplay();
        if (game.isGameOver()) { showGameOver(); return; }
        runRobotTurns();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  AFFICHAGE
    // ─────────────────────────────────────────────────────────────────────────

    public void refreshHand() {
        bottomPanel.removeAll();
        for (Card card : humanPlayer.getHand()) {
            bottomPanel.add(new CardPanel(card, layeredPane));
        }
        bottomPanel.revalidate();
        bottomPanel.repaint();
        CardPanel.clearSelection();
        if (deckPanel != null) deckPanel.refreshCount();
        updatePilePanel();
    }

    public void refreshDisplay() {
        gameInfoBar.updateDisplay();
        if (deckPanel != null) deckPanel.refreshCount();
        updatePilePanel();
    }

    private void updatePilePanel() {
        pilePanel.removeAll();
        if (lastPlayedCard != null) {
            CardPanel cp = new CardPanel(lastPlayedCard, layeredPane);
            cp.setEnabled(false);
            for (java.awt.event.MouseListener ml : cp.getMouseListeners()) cp.removeMouseListener(ml);
            pilePanel.add(cp, BorderLayout.CENTER);
        } else {
            JLabel lbl = new JLabel("Tas vide");
            lbl.setForeground(Color.WHITE);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            pilePanel.add(lbl, BorderLayout.CENTER);
        }
        pilePanel.revalidate();
        pilePanel.repaint();
    }

    private void showGameOver() {
        Player winner = game.getWinner();
        String msg = winner == null ? "Partie terminée !"
                : winner.getId().equals("Vous") ? "🎉 Vous avez gagné !"
                : winner.getId() + " a gagné !";

        int choix = JOptionPane.showOptionDialog(this, msg, "Fin de partie",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Rejouer", "Menu principal"}, "Rejouer");

        dispose();
        if (choix == JOptionPane.YES_OPTION) {
            new MainGUI(getTitle(), game.getPlayers().size(), "moyen");
        } else {
            new MenuGUI();
        }
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        gameInfoBar = new GameInfoBar(game, this);
        mainPanel.add(gameInfoBar, BorderLayout.NORTH);

        bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(0, 128, 0));
        bottomPanel.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 170));

        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel redLine = new JPanel();
        redLine.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 3));
        redLine.setBackground(Color.RED);
        southPanel.add(redLine, BorderLayout.NORTH);
        southPanel.add(bottomPanel, BorderLayout.CENTER);

        JButton playBtn = new JButton("JOUER LES CARTES SÉLECTIONNÉES");
        playBtn.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, GameConfig.FONT_LARGE));
        playBtn.setBackground(new Color(255, 215, 0));
        playBtn.setForeground(Color.BLACK);
        playBtn.addActionListener(e -> playSelectedCards());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0, 128, 0));
        btnPanel.add(playBtn);
        southPanel.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 200));
        layeredPane.setBackground(GameConfig.TABLE_COLOR);
        layeredPane.setOpaque(true);

        deckPanel = new DeckPanel(game.getDeck(), layeredPane, bottomPanel);
        deckPanel.setBounds(20, 20, GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT);
        layeredPane.add(deckPanel, JLayeredPane.DEFAULT_LAYER);

        pilePanel = new JPanel(new BorderLayout());
        pilePanel.setBounds(350, 20, GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT);
        pilePanel.setBackground(new Color(0, 100, 0));
        pilePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        layeredPane.add(pilePanel, JLayeredPane.DEFAULT_LAYER);

        mainPanel.add(layeredPane, BorderLayout.CENTER);
        add(mainPanel);
        refreshHand();
    }

    public Game getGame() { return game; }
}
