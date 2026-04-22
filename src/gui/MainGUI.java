package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import engine.data.*;
import engine.process.*;
import config.GameConfig;
import log.LoggerUtility;
import org.apache.log4j.Logger;

/**
 * Main game window.

 */
public class MainGUI extends JFrame {

    private static final Logger logger = LoggerUtility.getLogger(MainGUI.class, "html");

    private Game         game;
    private GameInfoBar  gameInfoBar;
    private JPanel       bottomPanel;
    private JLayeredPane layeredPane;
    private Player       humanPlayer;
    private DeckPanel    deckPanel;
    private JPanel       pilePanel;
    private Card         lastPlayedCard;
    private GameLogPanel logPanel;

    public MainGUI(String title, int nbJoueurs, String difficulte) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GameConfig.WINDOW_WIDTH + 250, GameConfig.WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        game        = GameBuilder.buildGame(nbJoueurs, difficulte);
        humanPlayer = GameBuilder.getHumanPlayer(game);

        logger.info("Interface graphique demarree : " + nbJoueurs
                + " joueurs, difficulte=" + difficulte);

        setupUI();
        setVisible(true);
        runRobotTurns();
    }

    // Robot turn loop

    private void runRobotTurns() {
        game.checkResetAtTurnStart();
        String resetMsg = game.consumeResetMessage();
        if (resetMsg != null) logPanel.addLog(resetMsg);

        if (game.isGameOver()) { showGameOver(); return; }

        if (game.isHumanTurn()) {
            gameInfoBar.setControlsEnabled(true);
            gameInfoBar.updateDisplay();
            refreshHand();
            return;
        }

        gameInfoBar.setControlsEnabled(false);
        gameInfoBar.updateDisplay();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try { Thread.sleep(900); } catch (InterruptedException ex) { return; }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (game.isGameOver()) { showGameOver(); return; }

                        Player current = game.getCurrentPlayer();
                        if (current instanceof BotPlayer) {
                            BotPlayer bot = (BotPlayer) current;
                            Combination choix = bot.choisirCombinaison(
                                    current.getHand(), game.getLastCombination());

                            if (choix != null) {
                                game.playCombination(choix);
                                lastPlayedCard = choix.getCards().get(
                                        choix.getCards().size() - 1);
                                String msg = current.getId() + " joue : " + choix.toString();
                                logPanel.addLog(msg);
                                logger.info("[BOT] " + msg);
                            } else {
                                game.drawCard();
                                String msg = current.getId() + " pioche.";
                                logPanel.addLog(msg);
                                logger.info("[BOT] " + msg);
                            }
                        }
                        refreshDisplay();
                        runRobotTurns();
                    }
                });
            }
        }).start();
    }

    // Human actions

    public void playSelectedCards() {
        game.checkResetAtTurnStart();
        String resetMsg = game.consumeResetMessage();
        if (resetMsg != null) logPanel.addLog(resetMsg);

        List<CardPanel> selected = CardPanel.getSelectedPanels();
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Sélectionnez d'abord des cartes à jouer !",
                    "Attention", JOptionPane.WARNING_MESSAGE);
            logger.warn("Le joueur humain a tenté de jouer sans sélectionner de cartes.");
            return;
        }

        List<Card> cardsToPlay = new ArrayList<Card>();
        for (CardPanel cp : selected) cardsToPlay.add(cp.getCard());

        CombinationType type     = Combination.determineType(cardsToPlay);
        Combination     combination = new Combination(cardsToPlay, type);

        boolean ok = game.playCombination(combination);
        if (ok) {
            lastPlayedCard = cardsToPlay.get(cardsToPlay.size() - 1);
            String msg = "Vous jouez : " + combination.toString();
            logPanel.addLog(msg);
            logger.info("[HUMAIN] " + msg);
            refreshHand();
            refreshDisplay();
            if (game.isGameOver()) { showGameOver(); return; }
            runRobotTurns();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Combinaison invalide ou ne bat pas la précédente !",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            logger.warn("[HUMAIN] Combinaison invalide jouée.");
        }
    }

    public void onHumanDraw() {
        game.checkResetAtTurnStart();
        String resetMsg = game.consumeResetMessage();
        if (resetMsg != null) logPanel.addLog(resetMsg);

        game.drawCard();
        String msg = "Vous piochez une carte.";
        logPanel.addLog(msg);
        logger.info("[HUMAIN] " + msg);
        refreshHand();
        refreshDisplay();
        if (game.isGameOver()) { showGameOver(); return; }
        runRobotTurns();
    }

    // Refresh helpers 

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
        String resetMsg = game.consumeResetMessage();
        if (resetMsg != null) logPanel.addLog(resetMsg);
        gameInfoBar.updateDisplay();
        if (deckPanel != null) deckPanel.refreshCount();
        updatePilePanel();
    }

    private void updatePilePanel() {
        pilePanel.removeAll();

        Combination last = game.getLastCombination();
        if (last != null && !last.getCards().isEmpty()) {
            JPanel comboPanel = new JPanel(new FlowLayout());
            comboPanel.setOpaque(false);
            for (Card card : last.getCards()) {
                CardPanel cp = new CardPanel(card, layeredPane);
                cp.setEnabled(false);
                comboPanel.add(cp);
            }
            pilePanel.add(comboPanel, BorderLayout.CENTER);
        } else {
            JLabel lbl = new JLabel("Tas vide");
            lbl.setForeground(GameStyle.TEXT_MUTED);
            lbl.setFont(GameStyle.FONT_SMALL);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            pilePanel.add(lbl, BorderLayout.CENTER);
        }

        pilePanel.revalidate();
        pilePanel.repaint();
    }

    //Game over

    private void showGameOver() {
        Player winner = game.getWinner();
        String msg = winner == null
                ? "Partie terminée !"
                : winner.getId().equals("Vous")
                    ? "Vous avez gagné !"
                    : winner.getId() + " a gagné !";

        logger.info("=== FIN DE PARTIE : " + msg + " ===");
        logPanel.addLog("=== " + msg + " ===");

        if (winner != null) {
            game.getGameStats().setWinner(winner.getId());
        }
        if (game.getGameStats().getTotalRounds() == 0) {
            game.getGameStats().setTotalRounds(
                    game.getCurrentRound().getRoundNumber());
        }

        final int    nbJoueurs  = game.getPlayers().size();
        final String difficulte = "moyen";
        final String frameTitle = getTitle();

        dispose();

        new EndGameGUI(
            game.getGameStats(),
            nbJoueurs,
            new Runnable() {
                @Override public void run() {
                    new MainGUI(frameTitle, nbJoueurs, difficulte);
                }
            },
            new Runnable() {
                @Override public void run() {
                    new MenuGUI();
                }
            }
        );
    }

    // UI setup

    private void setupUI() {
        // Root panel : felt gradient
        JPanel mainPanel = GameStyle.feltPanel(new BorderLayout());
        mainPanel.setOpaque(true);

        //  Top bar
        gameInfoBar = new GameInfoBar(game, this);
        // Style the bar to match felt theme
        gameInfoBar.setBackground(GameStyle.BG_DEEP);
        gameInfoBar.setOpaque(true);
        mainPanel.add(gameInfoBar, BorderLayout.NORTH);

        //  Center: table (layeredPane) + log
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 200));
        layeredPane.setBackground(GameStyle.BG_TABLE);
        layeredPane.setOpaque(true);

        // Deck
        deckPanel = new DeckPanel(game.getDeck(), layeredPane, bottomPanel);
        deckPanel.setBounds(20, 20, GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT);
        layeredPane.add(deckPanel, JLayeredPane.DEFAULT_LAYER);

        // Pile (last played combination)
        pilePanel = new JPanel(new BorderLayout());
        pilePanel.setBounds(350, 20, GameConfig.CARD_WIDTH * 3, GameConfig.CARD_HEIGHT);
        pilePanel.setBackground(GameStyle.BG_SURFACE);
        pilePanel.setOpaque(true);
        pilePanel.setBorder(BorderFactory.createLineBorder(GameStyle.BORDER_GOLD, 2));
        layeredPane.add(pilePanel, JLayeredPane.DEFAULT_LAYER);

        centerPanel.add(layeredPane, BorderLayout.CENTER);

        logPanel = new GameLogPanel();
        centerPanel.add(logPanel, BorderLayout.EAST);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // South: hand + separator + play button 
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);

        // Gold separator line
        JPanel goldLine = new JPanel();
        goldLine.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH + 250, 3));
        goldLine.setBackground(GameStyle.GOLD);
        southPanel.add(goldLine, BorderLayout.NORTH);

        // Hand area
        bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(GameStyle.BG_TABLE);
        bottomPanel.setOpaque(true);
        bottomPanel.setPreferredSize(
                new Dimension(GameConfig.WINDOW_WIDTH + 250, 170));
        southPanel.add(bottomPanel, BorderLayout.CENTER);

        // Play button — same gold style as GameStyle.goldButton
        JButton playBtn = GameStyle.goldButton("JOUER LES CARTES SÉLECTIONNÉES");
        playBtn.setFont(new Font("Arial", Font.BOLD, GameConfig.FONT_LARGE));
        playBtn.setPreferredSize(new Dimension(400, 46));

        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSelectedCards();
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        btnPanel.setBackground(GameStyle.BG_DEEP);
        btnPanel.setOpaque(true);
        btnPanel.add(playBtn);
        southPanel.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
        refreshHand();
        logger.info("Interface graphique initialisée.");
    }

    public Game getGame() { return game; }
}