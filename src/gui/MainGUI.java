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
        
        initGame(nbJoueurs, difficulte);
        setupUI();
        
        setVisible(true);
    }
    
    private void initGame(int nbJoueurs, String difficulte) {
        game = GameBuilder.buildGame(nbJoueurs, difficulte);
        humanPlayer = GameBuilder.getHumanPlayer(game);
    }
    
    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Barre d'information (en haut)
        gameInfoBar = new GameInfoBar(game, this);
        mainPanel.add(gameInfoBar, BorderLayout.NORTH);
        
        // Panneau de la main du joueur + bouton jouer (en bas)
        bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(0, 128, 0));
        bottomPanel.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 170));
        
        JPanel southPanel = new JPanel(new BorderLayout());
        
        JPanel redLinePanel = new JPanel();
        redLinePanel.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 3));
        redLinePanel.setBackground(Color.RED);
        southPanel.add(redLinePanel, BorderLayout.NORTH);
        
        southPanel.add(bottomPanel, BorderLayout.CENTER);
        
        JButton playSelectedButton = new JButton("JOUER LES CARTES SÉLECTIONNÉES");
        playSelectedButton.setFont(new Font(GameConfig.FONT_NAME, Font.BOLD, GameConfig.FONT_LARGE));
        playSelectedButton.setBackground(new Color(255, 215, 0));
        playSelectedButton.setForeground(Color.BLACK);
        playSelectedButton.addActionListener(e -> playSelectedCards());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 128, 0));
        buttonPanel.add(playSelectedButton);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        // Layered pane pour la pioche et le tas (au centre)
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, 200));
        layeredPane.setBackground(GameConfig.TABLE_COLOR);
        layeredPane.setOpaque(true);
        
        // Pioche (à gauche)
        deckPanel = new DeckPanel(game.getDeck(), layeredPane, bottomPanel);
        deckPanel.setBounds(20, 20, GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT);
        layeredPane.add(deckPanel, JLayeredPane.DEFAULT_LAYER);
        
        // Tas des cartes jouées (au centre)
        pilePanel = new JPanel();
        pilePanel.setBounds(350, 20, GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT);
        pilePanel.setBackground(new Color(0, 100, 0));
        pilePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        pilePanel.setLayout(new BorderLayout());
        layeredPane.add(pilePanel, JLayeredPane.DEFAULT_LAYER);
        
        mainPanel.add(layeredPane, BorderLayout.CENTER);
        
        add(mainPanel);
        
        refreshHand();
    }
    
    public void refreshHand() {
        bottomPanel.removeAll();
        
        if (humanPlayer != null) {
            for (Card card : humanPlayer.getHand()) {
                CardPanel cardPanel = new CardPanel(card, layeredPane);
                bottomPanel.add(cardPanel);
            }
        }
        
        bottomPanel.revalidate();
        bottomPanel.repaint();
        CardPanel.clearSelection();
        
        if (deckPanel != null) {
            deckPanel.refreshCount();
        }
        
        updatePilePanel();
    }
    
    private void updatePilePanel() {
        pilePanel.removeAll();
        
        if (lastPlayedCard != null) {
            CardPanel cardPanel = new CardPanel(lastPlayedCard, layeredPane);
            cardPanel.setEnabled(false);
            for (java.awt.event.MouseListener ml : cardPanel.getMouseListeners()) {
                cardPanel.removeMouseListener(ml);
            }
            pilePanel.add(cardPanel, BorderLayout.CENTER);
        } else {
            JLabel emptyLabel = new JLabel("Tas vide");
            emptyLabel.setForeground(Color.WHITE);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            pilePanel.add(emptyLabel, BorderLayout.CENTER);
        }
        
        pilePanel.revalidate();
        pilePanel.repaint();
    }
    
    public void refreshDisplay() {
        gameInfoBar.updateDisplay();
        
        if (deckPanel != null) {
            deckPanel.refreshCount();
        }
        
        updatePilePanel();
    }

    public void playSelectedCards() {
        List<CardPanel> selected = CardPanel.getSelectedPanels();
        
        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Sélectionnez d'abord des cartes à jouer !", 
                "Attention", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Card> cardsToPlay = new ArrayList<>();
        for (CardPanel panel : selected) {
            cardsToPlay.add(panel.getCard());
        }
        
        CombinationType type = Combination.determineType(cardsToPlay);
        Combination combination = new Combination(cardsToPlay, type);
        
        if (game.playCombination(combination)) {
            lastPlayedCard = cardsToPlay.get(cardsToPlay.size() - 1);
            
            refreshHand();
            refreshDisplay();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Cette combinaison n'est pas valide !", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public Game getGame() {
        return game;
    }
}
