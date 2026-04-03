package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import engine.data.*;
import engine.process.*;
import engine.logger.GameLogger;
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
    private GameLogPanel logPanel;

    public MainGUI(String title, int nbJoueurs, String difficulte) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GameConfig.WINDOW_WIDTH + 250, GameConfig.WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        game = GameBuilder.buildGame(nbJoueurs, difficulte);
        humanPlayer = GameBuilder.getHumanPlayer(game);

        setupUI();
        setVisible(true);

        Card firstCard = getFirstPlayerCard();
        if (firstCard != null) {
            GameLogger.getInstance().logFirstPlayer(game.getCurrentPlayer().getId(),
                firstCard.getValue().getSymbol() + " " + firstCard.getSuit().getSymbol());
        }

        // Fermer le logger à la fermeture de la fenêtre
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                GameLogger.getInstance().close();
            }
        });

        runRobotTurns();
    }
    
    private Card getFirstPlayerCard() {
        for (Card card : game.getCurrentPlayer().getHand()) {
            if (card.getValue() != Card.Value.JOKER && card.getValue() != Card.Value.TWO) {
                return card;
            }
        }
        return null;
    }

    private void runRobotTurns() {
        if (game.isGameOver()) { showGameOver(); return; }
        
        if (game.isHumanTurn()) {
            gameInfoBar.setControlsEnabled(true);
            gameInfoBar.updateDisplay();
            refreshHand();
            return;
        }

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
                } else {
                    game.drawCard();
                }
            }

            refreshDisplay();
            runRobotTurns();
        });
        timer.setRepeats(false);
        timer.start();
    }

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
            runRobotTurns();
        } else {
            JOptionPane.showMessageDialog(this,
                "Combinaison invalide ou ne bat pas la précédente !",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onHumanDraw() {
        game.drawCard();
        refreshHand();
        refreshDisplay();
        if (game.isGameOver()) { showGameOver(); return; }
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
        
        Combination last = game.getLastCombination();
        if (last != null && !last.getCards().isEmpty()) {
            JPanel comboPanel = new JPanel(new FlowLayout());
            for (Card card : last.getCards()) {
                CardPanel cp = new CardPanel(card, layeredPane);
                cp.setEnabled(false);
                comboPanel.add(cp);
            }
            pilePanel.add(comboPanel, BorderLayout.CENTER);
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
        bottomPanel.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH + 250, 170));
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        
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
        
        centerPanel.add(layeredPane, BorderLayout.CENTER);
        
        logPanel = new GameLogPanel();
        centerPanel.add(logPanel, BorderLayout.EAST);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel redLine = new JPanel();
        redLine.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH + 250, 3));
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

        add(mainPanel);
        refreshHand();
    }

    public Game getGame() { return game; }
}
