package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Écran de configuration de la partie (nb joueurs, difficulté).
 */
public class OptionGUI extends JFrame {

    private int    nbJoueurs  = 3;
    private String difficulte = "moyen";

    private JLabel  lblNbJoueurs;
    private JLabel  lblDiffSelected;

    public OptionGUI() {
        super("Options – Tu n'y peux rien");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        // Root: felt gradient
        JPanel root = GameStyle.feltPanel(new BorderLayout());
        root.setOpaque(true);
        root.setBorder(new EmptyBorder(30, 60, 30, 60));

        //Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel title = GameStyle.titleLabel(" Options de partie"); 
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(10));
        header.add(GameStyle.goldSeparator());
        header.add(Box.createVerticalStrut(20));

        root.add(header, BorderLayout.NORTH);

        //Center: options
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(Box.createVerticalGlue());
        center.add(buildPlayersSection());
        center.add(Box.createVerticalStrut(30));
        center.add(buildDifficultySection());
        center.add(Box.createVerticalGlue());

        root.add(center, BorderLayout.CENTER);

        // Footer: Retour / Démarrer
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
    }

    // Section : nombre de joueurs

    private JPanel buildPlayersSection() {
        JPanel card = GameStyle.roundedPanel(GameStyle.BG_SURFACE, GameStyle.BORDER_GOLD);
        card.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 16));
        card.setMaximumSize(new Dimension(700, 80));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel("  Nombre de joueurs :"); 
        lbl.setFont(GameStyle.FONT_BOLD);
        lbl.setForeground(GameStyle.TEXT_MAIN);

        // Bouton -
        JButton btnMoins = GameStyle.roundButton("-", GameStyle.GOLD, GameStyle.BG_DEEP);
        btnMoins.setPreferredSize(new Dimension(46, 46));
        btnMoins.setFont(new Font("Arial", Font.BOLD, 20));

        lblNbJoueurs = new JLabel(String.valueOf(nbJoueurs), SwingConstants.CENTER);
        lblNbJoueurs.setFont(new Font("Georgia", Font.BOLD, 26));
        lblNbJoueurs.setForeground(GameStyle.GOLD);
        lblNbJoueurs.setPreferredSize(new Dimension(50, 40));

        // Bouton +
        JButton btnPlus = GameStyle.roundButton("+", GameStyle.GOLD, GameStyle.BG_DEEP);
        btnPlus.setPreferredSize(new Dimension(46, 46));
        btnPlus.setFont(new Font("Arial", Font.BOLD, 20));


        btnMoins.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nbJoueurs > 3) {
                    nbJoueurs--;
                    lblNbJoueurs.setText(String.valueOf(nbJoueurs));
                }
            }
        });
        btnPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nbJoueurs < 5) {
                    nbJoueurs++;
                    lblNbJoueurs.setText(String.valueOf(nbJoueurs));
                }
            }
        });

        card.add(lbl);
        card.add(btnMoins);
        card.add(lblNbJoueurs);
        card.add(btnPlus);
        return card;
    }

    // Section : difficulté

    private JPanel buildDifficultySection() {
        JPanel card = GameStyle.roundedPanel(GameStyle.BG_SURFACE, GameStyle.BORDER_GOLD);
        card.setLayout(new FlowLayout(FlowLayout.CENTER, 16, 16));
        card.setMaximumSize(new Dimension(700, 80));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel("  Difficulté :"); 
        lbl.setFont(GameStyle.FONT_BOLD);
        lbl.setForeground(GameStyle.TEXT_MAIN);

        JButton btnFacile    = GameStyle.roundButton("Facile",    GameStyle.GREEN_BRIGHT, GameStyle.BG_DEEP);
        JButton btnNormal    = GameStyle.roundButton("Normal",    GameStyle.GOLD,         GameStyle.BG_DEEP);
        JButton btnDifficile = GameStyle.roundButton("Difficile", GameStyle.ACCENT_RED,   GameStyle.BG_DEEP);

        // Taille compacte pour les 3 boutons de difficulté
        Dimension diffBtnSize = new Dimension(130, 40);
        btnFacile.setPreferredSize(diffBtnSize);
        btnNormal.setPreferredSize(diffBtnSize);
        btnDifficile.setPreferredSize(diffBtnSize);

        lblDiffSelected = new JLabel("[ Normal ]");
        lblDiffSelected.setFont(GameStyle.FONT_BOLD);
        lblDiffSelected.setForeground(GameStyle.GOLD);

        btnFacile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficulte = "facile";
                lblDiffSelected.setText("[ Facile ]");
                lblDiffSelected.setForeground(GameStyle.GREEN_BRIGHT);
            }
        });
        btnNormal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficulte = "moyen";
                lblDiffSelected.setText("[ Normal ]");
                lblDiffSelected.setForeground(GameStyle.GOLD);
            }
        });
        btnDifficile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficulte = "difficile";
                lblDiffSelected.setText("[ Difficile ]");
                lblDiffSelected.setForeground(GameStyle.ACCENT_RED);
            }
        });

        card.add(lbl);
        card.add(btnFacile);
        card.add(btnNormal);
        card.add(btnDifficile);
        card.add(lblDiffSelected);
        return card;
    }

    //Footer : Retour / Démarrer 

    private JPanel buildFooter() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(GameStyle.goldSeparator());
        p.add(Box.createVerticalStrut(16));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        btnRow.setOpaque(false);

        JButton btnRetour   = GameStyle.blueButton("  Retour");          
        JButton btnDemarrer = GameStyle.goldButton(" Démarrer");  

        final OptionGUI self = this;

        btnRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuGUI();
                self.dispose();
            }
        });
        btnDemarrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainGUI("Jeu de cartes", nbJoueurs, difficulte);
                self.dispose();
            }
        });

        btnRow.add(btnRetour);
        btnRow.add(btnDemarrer);
        p.add(btnRow);
        return p;
    }
}