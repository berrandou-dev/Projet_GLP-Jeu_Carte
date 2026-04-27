package gui.screens;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import config.GameConfig;
import engine.process.BotPlayer;
import engine.data.Card;
import engine.process.Combination;
import engine.data.CombinationType;
import engine.data.Player;
import engine.process.Game;
import engine.process.GameBuilder;
import log.LoggerUtility;
import gui.panels.*;
import gui.utils.*;


/**
 * Main game window. Handles GUI layout, user interactions, and bot turn automation.
 */
public class MainGUI extends JFrame {

    private static final Logger logger = LoggerUtility.getLogger(MainGUI.class, "html");

    private Game game;
    private GameInfoBar gameInfoBar;
    private JPanel bottomPanel;
    private JLayeredPane layeredPane;
    private Player humanPlayer;
    private DeckPanel deckPanel;
    private JPanel pilePanel;
    private GameLogPanel logPanel;

    private JPanel botTopLeft;
    private JPanel botTopRight;
    private JPanel botLeft;
    private JPanel botRight;
    private JPanel botBotLeft;
    private JPanel botBotRight;

    public MainGUI(String title, int nbJoueurs, String difficulte) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        if (GameStyle.isFullscreen) {
    		setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
    		setSize(GameConfig.WINDOW_WIDTH , GameConfig.WINDOW_HEIGHT);
		}

        setLocationRelativeTo(null);

        game = GameBuilder.buildGame(nbJoueurs, difficulte);
        humanPlayer = GameBuilder.getHumanPlayer(game);

        logger.info("Interface graphique demarree : " + nbJoueurs
                + " joueurs, difficulte=" + difficulte);
        MusicPlayer.play();
        setupUI();
        setVisible(true);
        runRobotTurns();
    }
	
	// Nouveau constructeur (pour deck personnalisé)
	public MainGUI(Game game, String title) {
    	super(title);
    	this.game = game;
    	this.humanPlayer = GameBuilder.getHumanPlayer(game);
    
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setResizable(true);
    	if (GameStyle.isFullscreen) {
    	    setExtendedState(JFrame.MAXIMIZED_BOTH);
    	} else {
    	    setSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
    	}
    	setLocationRelativeTo(null);
    	
    	logger.info("Interface graphique demarree avec deck personnalise");
    	MusicPlayer.play();
    	setupUI();
    	setVisible(true);
    	//runRobotTurns();
	}
    private void runRobotTurns() {
        game.checkResetAtTurnStart();
        String resetMsg = game.consumeResetMessage();
        if (resetMsg != null) {
            logPanel.addLog(resetMsg);
        }

        if (game.isGameOver()) {
            showGameOver();
            return;
        }

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
                try {
                    Thread.sleep(900);
                } catch (InterruptedException ex) {
                    return;
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (game.isGameOver()) {
                            showGameOver();
                            return;
                        }

                        Player current = game.getCurrentPlayer();
                        if (current instanceof BotPlayer) {
                            BotPlayer bot = (BotPlayer) current;
                            Combination choix = bot.choisirCombinaison(
                                    current.getHand(), game.getLastCombination());

                            if (choix != null) {
                                game.playCombination(choix);
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

    public void playSelectedCards() {
        game.checkResetAtTurnStart();
        String resetMsg = game.consumeResetMessage();
        if (resetMsg != null) {
            logPanel.addLog(resetMsg);
        }

        List<CardPanel> selected = CardPanel.getSelectedPanels();
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Selectionnez d'abord des cartes a jouer !",
                    "Attention", JOptionPane.WARNING_MESSAGE);
            logger.warn("Le joueur humain a tente de jouer sans selectionner de cartes.");
            return;
        }

        List<Card> cardsToPlay = new ArrayList<Card>();
        for (CardPanel cp : selected) {
            cardsToPlay.add(cp.getCard());
        }

        CombinationType type = Combination.determineType(cardsToPlay);
        Combination combination = new Combination(cardsToPlay, type);

        boolean ok = game.playCombination(combination);
        if (ok) {
            String msg = "Vous jouez : " + combination.toString();
            logPanel.addLog(msg);
            logger.info("[HUMAIN] " + msg);
            refreshHand();
            refreshDisplay();
            if (game.isGameOver()) {
                showGameOver();
                return;
            }
            runRobotTurns();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Combinaison invalide ou ne bat pas la precedente !",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            logger.warn("[HUMAIN] Combinaison invalide jouee.");
        }
    }

    public void onHumanDraw() {
        game.checkResetAtTurnStart();
        String resetMsg = game.consumeResetMessage();
        if (resetMsg != null) {
            logPanel.addLog(resetMsg);
        }

        game.drawCard();
        String msg = "Vous piochez une carte.";
        logPanel.addLog(msg);
        logger.info("[HUMAIN] " + msg);
        refreshHand();
        refreshDisplay();
        if (game.isGameOver()) {
            showGameOver();
            return;
        }
        runRobotTurns();
    }

    public void refreshHand() {
        bottomPanel.removeAll();
        for (Card card : humanPlayer.getHand()) {
            bottomPanel.add(new CardPanel(card, layeredPane));
        }
        bottomPanel.revalidate();
        bottomPanel.repaint();
        CardPanel.clearSelection();
        if (deckPanel != null) {
            deckPanel.refreshCount();
        }
        updatePilePanel();
    }

    public void refreshDisplay() {
        String resetMsg = game.consumeResetMessage();
        if (resetMsg != null) {
            logPanel.addLog(resetMsg);
        }
        gameInfoBar.updateDisplay();
        if (deckPanel != null) {
            deckPanel.refreshCount();
        }
        updatePilePanel();
        refreshBotSlots();
    }

    private void updatePilePanel() {
        pilePanel.removeAll();

        Combination last = game.getLastCombination();
        if (last != null && !last.getCards().isEmpty()) {
            JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));
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

    private void refreshBotSlots() {
        JPanel[] slots = { botTopLeft, botTopRight, botLeft, botRight, botBotLeft, botBotRight };
        for (JPanel slot : slots) {
            if (slot != null) {
                slot.repaint();
            }
        }
    }

    private void showGameOver() {
        Player winner = game.getWinner();
        String msg = winner == null
                ? "Partie terminee !"
                : winner.getId().equals("Vous")
                    ? "Vous avez gagne !"
                    : winner.getId() + " a gagne !";

        logger.info("=== FIN DE PARTIE : " + msg + " ===");
        logPanel.addLog("=== " + msg + " ===");

        if (winner != null) {
            game.getGameStats().setWinner(winner.getId());
        }
        if (game.getGameStats().getTotalRounds() == 0) {
            game.getGameStats().setTotalRounds(game.getCurrentRound().getRoundNumber());
        }

        final int nbJoueurs = game.getPlayers().size();
        final String difficulte = "moyen";
        final String frameTitle = getTitle();

        dispose();

        new EndGameGUI(
                game.getGameStats(),
                nbJoueurs,
                new Runnable() {
                    @Override
                    public void run() {
                        new MainGUI(frameTitle, nbJoueurs, difficulte);
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        new MenuGUI();
                    }
                }
        );
    }

    private JPanel createBotSlot(Player player) {
        if (player == null) {
            return null;
        }
        return new BotsPanel(player, game);
    }

    private Player getBotAt(List<Player> bots, int index) {
        return index >= 0 && index < bots.size() ? bots.get(index) : null;
    }

    private JPanel createLabeledZone(String title, JComponent content, Dimension preferredSize) {
        JPanel zone = new JPanel(new BorderLayout());
        zone.setOpaque(false);
        if (preferredSize != null) {
            zone.setPreferredSize(preferredSize);
        }
        zone.add(content, BorderLayout.CENTER);
        return zone;
    }

    private void setupUI() {
        JPanel mainPanel = GameStyle.feltPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(new EmptyBorder(8, 10, 10, 10));

        gameInfoBar = new GameInfoBar(game, this);
        gameInfoBar.setBackground(GameStyle.BG_DEEP);
        gameInfoBar.setOpaque(true);
        mainPanel.add(gameInfoBar, BorderLayout.NORTH);

        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);

        List<Player> bots = new ArrayList<Player>();
        for (Player p : game.getPlayers()) {
            if (!"Vous".equals(p.getId())) {
                bots.add(p);
            }
        }

        botTopLeft = createBotSlot(getBotAt(bots, 0));
        botTopRight = createBotSlot(getBotAt(bots, 1));
        botLeft = createBotSlot(getBotAt(bots, 2));
        botRight = createBotSlot(getBotAt(bots, 3));
        botBotLeft = createBotSlot(getBotAt(bots, 4));
        botBotRight = createBotSlot(getBotAt(bots, 5));

        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(GameStyle.BG_TABLE);
        bottomPanel.setPreferredSize(new Dimension(0, 172));
        bottomPanel.setBorder(new EmptyBorder(2, 8, 2, 8));

        deckPanel = new DeckPanel(game.getDeck(), layeredPane, bottomPanel);
        deckPanel.setBorder(BorderFactory.createLineBorder(GameStyle.BORDER_GOLD, 2));

        pilePanel = new JPanel(new BorderLayout());
        pilePanel.setBackground(GameStyle.BG_TABLE);
        pilePanel.setBorder(BorderFactory.createLineBorder(GameStyle.BORDER_GOLD, 2));
        pilePanel.setPreferredSize(new Dimension(GameConfig.CARD_WIDTH * 3 + 40, GameConfig.CARD_HEIGHT + 28));

        final JPanel deckZone = createLabeledZone("Pioche", deckPanel,
                new Dimension(GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT));
        final JPanel pileZone = createLabeledZone("Tas", pilePanel,
                new Dimension(GameConfig.CARD_WIDTH * 3 + 40, GameConfig.CARD_HEIGHT + 28));

        final JPanel tableArea = GameStyle.roundedPanel(GameStyle.BG_SURFACE, GameStyle.BORDER_GOLD);
        tableArea.setLayout(null);
        tableArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        tableArea.setPreferredSize(new Dimension(860, 360));

        tableArea.add(deckZone);
        tableArea.add(pileZone);
        if (botTopLeft != null) {
            tableArea.add(botTopLeft);
        }
        if (botTopRight != null) {
            tableArea.add(botTopRight);
        }
        if (botLeft != null) {
            tableArea.add(botLeft);
        }
        if (botRight != null) {
            tableArea.add(botRight);
        }
        if (botBotLeft != null) {
            tableArea.add(botBotLeft);
        }
        if (botBotRight != null) {
            tableArea.add(botBotRight);
        }

        tableArea.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = tableArea.getWidth();
                int h = tableArea.getHeight();

                Dimension deckSize = deckZone.getPreferredSize();
                Dimension pileSize = pileZone.getPreferredSize();

                int deckX = Math.max(8, (int) (w * 0.06));
                int deckY = Math.max(26, (int) (h * 0.14));
                int pileY = Math.max(110, (h - pileSize.height) / 2 + 8);
                int botTopLeftCenterX = (int) (w * 0.34);
                int botTopRightCenterX = (int) (w * 0.80);
                int botLeftCenterX = (int) (w * 0.28);
                int botRightCenterX = (int) (w * 0.80);
                int pileX = (w - pileSize.width) / 2 + 72;

                deckZone.setBounds(deckX, deckY, deckSize.width, deckSize.height);
                pileZone.setBounds(pileX, pileY, pileSize.width, pileSize.height);

                if (botTopLeft != null) {
                    Dimension d = botTopLeft.getPreferredSize();
                    botTopLeft.setBounds(botTopLeftCenterX - d.width / 2, 28, d.width, d.height);
                }
                if (botTopRight != null) {
                    Dimension d = botTopRight.getPreferredSize();
                    botTopRight.setBounds(botTopRightCenterX - d.width / 2, 36, d.width, d.height);
                }
                if (botLeft != null) {
                    Dimension d = botLeft.getPreferredSize();
                    botLeft.setBounds(botLeftCenterX - d.width / 2, (int) (h * 0.58), d.width, d.height);
                }
                if (botRight != null) {
                    Dimension d = botRight.getPreferredSize();
                    botRight.setBounds(botRightCenterX - d.width / 2, (int) (h * 0.62), d.width, d.height);
                }
                if (botBotLeft != null) {
                    Dimension d = botBotLeft.getPreferredSize();
                    botBotLeft.setBounds(18, (h - d.height) / 2 + 18, d.width, d.height);
                }
                if (botBotRight != null) {
                    Dimension d = botBotRight.getPreferredSize();
                    botBotRight.setBounds(w - d.width - 18, (h - d.height) / 2 + 24, d.width, d.height);
                }
            }
        });

        logPanel = new GameLogPanel();
        logPanel.setPreferredSize(new Dimension(230, 0));

        JPanel centerPanel = new JPanel(new BorderLayout(12, 0));
        centerPanel.setOpaque(false);
        centerPanel.add(tableArea, BorderLayout.CENTER);
        centerPanel.add(logPanel, BorderLayout.EAST);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(0, 2));
        southPanel.setOpaque(false);

        JButton playBtn = GameStyle.goldButton("JOUER LES CARTES SELECTIONNEES");
        playBtn.setPreferredSize(new Dimension(340, 32));
        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSelectedCards();
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(playBtn);

        JPanel handPanel = new JPanel(new BorderLayout(0, 2));
        handPanel.setOpaque(false);
        handPanel.setBorder(new EmptyBorder(2, 12, 1, 12));
        handPanel.add(bottomPanel, BorderLayout.CENTER);
        handPanel.add(btnPanel, BorderLayout.SOUTH);

        southPanel.add(handPanel, BorderLayout.CENTER);
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        refreshHand();
        updatePilePanel();
        refreshBotSlots();
    }

    public Game getGame() {
        return game;
    }
}
