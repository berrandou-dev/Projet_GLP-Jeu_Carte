package gui;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;

public class gameGUI extends JFrame {

    private static final Dimension IDEAL_MAIN_DIMENSION = new Dimension(800, 400);
    private static final Dimension IDEAL_DASHBOARD_DIMENSION = new Dimension(800, 300);

    private static Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
   
    private void init() {
    }

    public gameGUI(String title) {
        super(title);
        init() ;

        setSize(IDEAL_MAIN_DIMENSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
    new gameGUI("Jeu Carte");
}

}

