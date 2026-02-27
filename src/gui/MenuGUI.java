package gui;

import javax.swing.*;
import java.awt.*;

public class MenuGUI extends JFrame {

    public MenuGUI() {

        setTitle("MainGUI");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 110, 20));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; //l’index de la ligne
        gbc.insets = new Insets(15, 0, 15, 0);

        //Titre
        JLabel titre = new JLabel("Tu n'y peux rien");
        titre.setForeground(Color.WHITE);
        titre.setFont(new Font("Arial", Font.BOLD, 40));
        gbc.gridy = 0;
        panel.add(titre, gbc);

        //Nouvelle Partie
        JButton btnNewGame = createButton("Nouvelle partie");
         btnNewGame.setPreferredSize(new Dimension(250, 50));
         gbc.gridy = 1 ; 
        panel.add(btnNewGame, gbc);
        btnNewGame.addActionListener(e -> {
            new MainGUI(getTitle());
            dispose();
        });

        //Quitter 
        JButton btnQuit = createButton("Quitter");
        btnQuit.setPreferredSize(new Dimension(250, 50));
        gbc.gridy = 2;
        panel.add(btnQuit, gbc);
        btnQuit.addActionListener(e -> System.exit(0));

        //Plein écran
        JButton btnFullscreen = createButton("Plein écran");
         btnFullscreen.setPreferredSize(new Dimension(250, 50));
         gbc.gridy = 3 ; 
        panel.add(btnFullscreen, gbc);
    
        btnFullscreen.addActionListener(e ->
                setExtendedState(JFrame.MAXIMIZED_BOTH)
        );

        add(panel);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        return button;
    }

   
}