package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Menu principal du jeu.
 */
public class MenuGUI extends JFrame {

    public MenuGUI() {
        super("Tu n'y peux rien");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
       
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        //Root: felt gradient
        JPanel root = GameStyle.feltPanel(new BorderLayout());
        root.setOpaque(true);
        root.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel cardIcon = new JLabel("\uD83C\uDCCF", SwingConstants.CENTER); 
        cardIcon.setFont(GameStyle.FONT_EMOJI_LG);
        cardIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = GameStyle.titleLabel("Tu n'y peux rien");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(cardIcon);
        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(6));
        header.add(Box.createVerticalStrut(18));
        header.add(GameStyle.goldSeparator());

        root.add(header, BorderLayout.NORTH);

        //Center: buttons
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(Box.createVerticalGlue());

        // Nouvelle partie
        JButton btnNewGame = GameStyle.goldButton(" Nouvelle partie"); 
        btnNewGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OptionGUI();
                dispose();
            }
        });

        // Plein écran
        JButton btnFullscreen = GameStyle.blueButton("  Plein écran"); 
        btnFullscreen.setAlignmentX(Component.CENTER_ALIGNMENT);
        final MenuGUI self = this;
        btnFullscreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                self.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        // Quitter
        JButton btnQuit = GameStyle.roundButton(
                "\u274C  Quitter", GameStyle.ACCENT_RED, GameStyle.BG_DEEP); 
        btnQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        center.add(btnNewGame);
        center.add(Box.createVerticalStrut(18));
        center.add(btnFullscreen);
        center.add(Box.createVerticalStrut(18));
        center.add(btnQuit);

        center.add(Box.createVerticalGlue());

        root.add(center, BorderLayout.CENTER);

        // Footer: version muted
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));
        footer.add(GameStyle.goldSeparator());
        footer.add(Box.createVerticalStrut(10));
        JLabel version = GameStyle.mutedLabel("v1.0  –  Projet GLP  –  CY Cergy Paris Université");
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        footer.add(version);

        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

}