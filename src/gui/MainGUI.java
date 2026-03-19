package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import engine.data.*;
import engine.process.*;

public class MainGUI extends JFrame {
    private Game game;
    private GameDisplay gameDisplay;
    private GamePanel gamePanel;
    private JPanel bottomPanel;
    private JLayeredPane layeredPane;
    private Player humanPlayer;
    
    public MainGUI(String title, int nbJoueurs, String difficulte) {
        super(title);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initGame(nbJoueurs, difficulte);
        setupUI();
        
        setVisible(true);
    }
    
    private void initGame(int nbJoueurs, String difficulte) {
        Deck deck = new Deck();
        
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= nbJoueurs; i++) {
            if (i == 1) {
                humanPlayer = new Player("Vous");
                players.add(humanPlayer);
            } else {
                players.add(new Player("Robot " + (i-1)));
            }
        }
        
        game = new Game(players, deck);
    }
    
    private void setupUI() {
    JPanel mainPanel = new JPanel(new BorderLayout());
    
    // Panneau de jeu
    gameDisplay = new GameDisplay(game);
    gameDisplay.setPreferredSize(new Dimension(800, 400));
    gameDisplay.setBackground(new Color(25, 100, 25));
    mainPanel.add(gameDisplay, BorderLayout.CENTER);
    
    // Panneau d'information (en haut)
    gamePanel = new GamePanel(game, this);
    mainPanel.add(gamePanel, BorderLayout.NORTH);
    
    // Panneau de la main du joueur + bouton jouer (en bas)
    bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.setBackground(new Color(0, 128, 0));
    bottomPanel.setPreferredSize(new Dimension(800, 170));
    
    JPanel southPanel = new JPanel(new BorderLayout());
    
    JPanel redLinePanel = new JPanel();
    redLinePanel.setPreferredSize(new Dimension(800, 3));
    redLinePanel.setBackground(Color.RED);
    southPanel.add(redLinePanel, BorderLayout.NORTH);
    
    southPanel.add(bottomPanel, BorderLayout.CENTER);
    
    JButton playSelectedButton = new JButton("JOUER LES CARTES SÉLECTIONNÉES");
    playSelectedButton.setFont(new Font("Arial", Font.BOLD, 16));
    playSelectedButton.setBackground(new Color(255, 215, 0));
    playSelectedButton.setForeground(Color.BLACK);
    playSelectedButton.addActionListener(e -> playSelectedCards());
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(new Color(0, 128, 0));
    buttonPanel.add(playSelectedButton);
    southPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    mainPanel.add(southPanel, BorderLayout.SOUTH);
    
    // Layered pane pour la pioche
    layeredPane = new JLayeredPane();
    layeredPane.setPreferredSize(new Dimension(800, 200));
    layeredPane.setBackground(new Color(30, 120, 30));
    layeredPane.setOpaque(true);
    
    DeckPanel deckPanel = new DeckPanel(game.getDeck(), layeredPane, bottomPanel);
    deckPanel.setBounds(20, 20, 100, 150);
    layeredPane.add(deckPanel, JLayeredPane.DEFAULT_LAYER);
    
    mainPanel.add(layeredPane, BorderLayout.CENTER);
    
    add(mainPanel);
    
    // Afficher les cartes initiales
    refreshHand();
}
    
    public void refreshHand() {
	bottomPanel.removeAll();
    
	Player currentHumanPlayer = null;
	for (Player p : game.getPlayers()) {
		if (p.getId().equals("Vous")) {
			currentHumanPlayer = p;
			break;
        	}
	}
    
	if (currentHumanPlayer != null) {
		for (Card card : currentHumanPlayer.getHand()) {
			CardPanel cardPanel = new CardPanel(card, layeredPane);
			bottomPanel.add(cardPanel);
		}
	}
    
	bottomPanel.revalidate();
	bottomPanel.repaint();
	CardPanel.clearSelection();
    }
    
    public void refreshDisplay() {
        gameDisplay.repaint();
        gamePanel.updateDisplay();
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
    
    // Récupérer les cartes des panneaux sélectionnés
    List<Card> cardsToPlay = new ArrayList<>();
    for (CardPanel panel : selected) {
        cardsToPlay.add(panel.getCard());
    }
    
    // Créer et jouer la combinaison
    CombinationType type = Combination.determineType(cardsToPlay);
    Combination combination = new Combination(cardsToPlay, type);
    
    if (game.playCombination(combination)) {
        // Rafraîchir l'affichage
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
