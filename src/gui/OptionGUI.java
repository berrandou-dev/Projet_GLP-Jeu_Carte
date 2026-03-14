package gui;

import javax.swing.*;
import java.awt.*;

public class OptionGUI extends JFrame {

    private int nbJoueurs = 3;
    private JLabel lblNbJoueurs;
    private String difficulte = "Normal";

    public OptionGUI() {

        setTitle("Options");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 110, 20));

        // Titre
        JLabel titre = new JLabel("Options", SwingConstants.CENTER);
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("Arial", Font.BOLD, 40));
        titre.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        mainPanel.add(titre, BorderLayout.NORTH);

        // Panneau central
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 20, 20));
        centerPanel.setBackground(new Color(20, 110, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Ligne : nombre de joueurs
        JPanel joueursPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        joueursPanel.setBackground(new Color(20, 110, 20));

        JLabel lblJoueur = new JLabel("Nombre de joueurs : ");
        lblJoueur.setForeground(Color.WHITE);
        lblJoueur.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnMoins = createSmallButton("-");

        lblNbJoueurs = new JLabel(String.valueOf(nbJoueurs));
        lblNbJoueurs.setForeground(Color.WHITE);
        lblNbJoueurs.setFont(new Font("Arial", Font.BOLD, 22));
        lblNbJoueurs.setPreferredSize(new Dimension(40, 30));
        lblNbJoueurs.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnPlus = createSmallButton("+");

        btnMoins.addActionListener(e -> {
            if (nbJoueurs > 3) {
                nbJoueurs--;
                lblNbJoueurs.setText(String.valueOf(nbJoueurs));
            }
        });

        btnPlus.addActionListener(e -> {
            if (nbJoueurs < 5) {
                nbJoueurs++;
                lblNbJoueurs.setText(String.valueOf(nbJoueurs));
            }
        });

        joueursPanel.add(lblJoueur);
        joueursPanel.add(btnMoins);
        joueursPanel.add(lblNbJoueurs);
        joueursPanel.add(btnPlus);

        // Ligne : difficulté
        JPanel difficultePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        difficultePanel.setBackground(new Color(20, 110, 20));

        JLabel lblDiff = new JLabel("Difficulté : ");
        lblDiff.setForeground(Color.WHITE);
        lblDiff.setFont(new Font("Arial", Font.BOLD, 22));

        JButton btnFacile = createButton("Facile");
        JButton btnNormal = createButton("Normal");
        JButton btnDifficile = createButton("Difficile");

        btnFacile.addActionListener(e -> difficulte = "Facile");
        btnNormal.addActionListener(e -> difficulte = "Normal");
        btnDifficile.addActionListener(e -> difficulte = "Difficile");

        difficultePanel.add(lblDiff);
        difficultePanel.add(btnFacile);
        difficultePanel.add(btnNormal);
        difficultePanel.add(btnDifficile);

        // Ligne : boutons bas
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        boutonsPanel.setBackground(new Color(20, 110, 20));

        JButton btnRetour = createButton("Retour");
        btnRetour.setPreferredSize(new Dimension(200, 50));

        JButton btnDemarrer = createButton("Démarrer");
        btnDemarrer.setPreferredSize(new Dimension(200, 50));

        btnRetour.addActionListener(e -> {
            new MenuGUI().setVisible(true);
            dispose();
        });

        btnDemarrer.addActionListener(e -> {
        
            MainGUI mainGUI = new MainGUI("Jeu de cartes", nbJoueurs, difficulte);
            mainGUI.setVisible(true);
            
            dispose();
        });

        boutonsPanel.add(btnRetour);
        boutonsPanel.add(btnDemarrer);

        // Ajout des lignes dans le panneau central
        centerPanel.add(joueursPanel);
        centerPanel.add(difficultePanel);
        centerPanel.add(boutonsPanel);

        // Ajout du panneau central à la fenêtre
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        return button;
    }

    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        button.setPreferredSize(new Dimension(50, 40));
        return button;
    }
}
