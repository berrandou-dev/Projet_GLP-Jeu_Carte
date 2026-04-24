package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import config.GameConfig;

/**
 * Écran de configuration de la partie (nb joueurs, difficulté).
 */
public class OptionGUI extends JFrame {

    private int    nbJoueurs  = 3;
    private String difficulte = "moyen";

    private JLabel  lblNbJoueurs;
    private JLabel  lblDiffSelected;
    private JPanel  playersSection;
    private JPanel  difficultySection;
    
    private JButton btnMoins;
    private JButton btnPlus;
    private JButton btnFacile;
    private JButton btnNormal;
    private JButton btnDifficile;
    private JButton btnRetour;
    private JButton btnDemarrer;
    private JPanel  footerButtonsRow;

    public OptionGUI() {
        super("Options – Tu n'y peux rien");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(true);
        
          if (GameStyle.isFullscreen) {
    setExtendedState(JFrame.MAXIMIZED_BOTH);
} else {
    setSize(GameConfig.WINDOW_WIDTH , GameConfig.WINDOW_HEIGHT);
}
        
        setLocationRelativeTo(null);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        // Root: felt gradient
        JPanel root = GameStyle.feltPanel(new BorderLayout());
        root.setOpaque(true);
        root.setBorder(new EmptyBorder(30, 60, 30, 60));

        // Header
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

        // Center: options
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

        // Adapter les tailles à la fenêtre
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                adaptSizes();
            }
        });
        
        adaptSizes();
        setContentPane(root);
    }

    private void adaptSizes() {
    int w = getWidth();
    int h = getHeight();

    boolean fullscreen = GameStyle.isFullscreen;

    // Ratios différents selon mode
    double btnRatioW   = fullscreen ? 0.11 : 0.14;
    double btnRatioH   = fullscreen ? 0.05 : 0.07;
    double fontRatio   = fullscreen ? 1.1  : 0.9;

    int btnWidth  = (int)(w * btnRatioW);
    int btnHeight = (int)(h * btnRatioH);

    int baseFont = Math.max(12, Math.min(24, w / 50));
    int fontSize = (int)(baseFont * fontRatio);

    // ---- JOUEURS ----
    if (btnMoins != null) {
        btnMoins.setPreferredSize(new Dimension(btnWidth, btnHeight));
        btnPlus.setPreferredSize(new Dimension(btnWidth, btnHeight));

        btnMoins.setFont(new Font("Arial", Font.BOLD, fontSize));
        btnPlus.setFont(new Font("Arial", Font.BOLD, fontSize));

        lblNbJoueurs.setFont(new Font("Georgia", Font.BOLD, fontSize + 10));
    }
    if (playersSection != null) {
        int sectionW = fullscreen ? Math.min(980, w - 160) : 700;
        playersSection.setMaximumSize(new Dimension(sectionW, Math.max(90, btnHeight + 34)));
    }

    // ---- DIFFICULTÉ ----
    if (btnFacile != null) {
        int diffW = (int)(w * (fullscreen ? 0.13 : 0.12));
        int diffH = (int)(h * (fullscreen ? 0.05 : 0.06));

        btnFacile.setPreferredSize(new Dimension(diffW, diffH));
        btnNormal.setPreferredSize(new Dimension(diffW, diffH));
        btnDifficile.setPreferredSize(new Dimension(diffW, diffH));

        btnFacile.setFont(new Font("Arial", Font.BOLD, fontSize - 2));
        btnNormal.setFont(new Font("Arial", Font.BOLD, fontSize - 2));
        btnDifficile.setFont(new Font("Arial", Font.BOLD, fontSize - 2));

        lblDiffSelected.setFont(new Font("Arial", Font.BOLD, fontSize));
    }
    if (difficultySection != null) {
        int sectionW = fullscreen ? Math.min(1160, w - 120) : 700;
        difficultySection.setMaximumSize(new Dimension(sectionW, Math.max(96, (int)(h * 0.11))));
    }

    // ---- FOOTER ----
    if (btnRetour != null) {
        int footerW = (int)(w * (fullscreen ? 0.16 : 0.18));
        int footerH = (int)(h * (fullscreen ? 0.055 : 0.075));

        btnRetour.setPreferredSize(new Dimension(footerW, footerH));
        btnDemarrer.setPreferredSize(new Dimension(footerW, footerH));

        btnRetour.setFont(new Font("Arial", Font.BOLD, fontSize));
        btnDemarrer.setFont(new Font("Arial", Font.BOLD, fontSize));
    }
    if (footerButtonsRow != null) {
        footerButtonsRow.setLayout(new FlowLayout(
                FlowLayout.CENTER,
                fullscreen ? 50 : 30,
                0
        ));
    }

    revalidate();
    repaint();
}

    // Section : nombre de joueurs

    private JPanel buildPlayersSection() {
        JPanel card = GameStyle.roundedPanel(GameStyle.BG_SURFACE, GameStyle.BORDER_GOLD);
        card.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 16));
        card.setMaximumSize(new Dimension(700, 80));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersSection = card;

        JLabel lbl = new JLabel("  Nombre de joueurs :"); 
        lbl.setFont(GameStyle.FONT_BOLD);
        lbl.setForeground(GameStyle.TEXT_MAIN);

        // Bouton -
        btnMoins = GameStyle.roundButton("-", GameStyle.GOLD, GameStyle.BG_DEEP);
        btnMoins.setFont(new Font("Arial", Font.BOLD, 20));

        lblNbJoueurs = new JLabel(String.valueOf(nbJoueurs), SwingConstants.CENTER);
        lblNbJoueurs.setFont(new Font("Georgia", Font.BOLD, 26));
        lblNbJoueurs.setForeground(GameStyle.GOLD);
        lblNbJoueurs.setPreferredSize(new Dimension(50, 40));

        // Bouton +
        btnPlus = GameStyle.roundButton("+", GameStyle.GOLD, GameStyle.BG_DEEP);
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
        difficultySection = card;

        JLabel lbl = new JLabel("  Difficulté :"); 
        lbl.setFont(GameStyle.FONT_BOLD);
        lbl.setForeground(GameStyle.TEXT_MAIN);

        btnFacile    = GameStyle.roundButton("Facile",    GameStyle.GREEN_BRIGHT, GameStyle.BG_DEEP);
        btnNormal    = GameStyle.roundButton("Normal",    GameStyle.GOLD,         GameStyle.BG_DEEP);
        btnDifficile = GameStyle.roundButton("Difficile", GameStyle.ACCENT_RED,   GameStyle.BG_DEEP);

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

    // Footer : Retour / Démarrer 

    private JPanel buildFooter() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(GameStyle.goldSeparator());
        p.add(Box.createVerticalStrut(16));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        btnRow.setOpaque(false);
        footerButtonsRow = btnRow;

        btnRetour   = GameStyle.blueButton("  Retour");          
        btnDemarrer = GameStyle.goldButton(" Démarrer");  

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
