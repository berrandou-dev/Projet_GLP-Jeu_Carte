package gui.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panneau d'affichage de l'historique des actions dans la GUI.
 * Le logger Log4j ecrit dans les fichiers (game-log.txt / game-log.html).
 * Ce panneau affiche les messages directement dans l'interface graphique.
 */
public class GameLogPanel extends JPanel {

	private JTextArea logArea;
	private List<String> logs;
	private static final int MAX_LOGS = 20;

	public GameLogPanel() {
		logs = new ArrayList<>();
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(250, 400));
		setBackground(new Color(40, 40, 60));
		setBorder(BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
			"HISTORIQUE",
			TitledBorder.CENTER,
			TitledBorder.TOP,
			new Font("Arial", Font.BOLD, 14),
			new Color(255, 215, 0)
		));

		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBackground(new Color(30, 30, 50));
		logArea.setForeground(Color.WHITE);
		logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JScrollPane scrollPane = new JScrollPane(logArea);
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Ajoute un message dans le panneau d'historique GUI.
	 * A appeler depuis MainGUI apres chaque action de jeu.
	 */
	public void addLog(String message) {
    	logs.add(message);  //
    	while (logs.size() > MAX_LOGS) {
        	logs.remove(0);
    	}
    	StringBuilder sb = new StringBuilder();
    	for (String log : logs) {
        	sb.append("• ").append(log).append("\n");
    	}
    	logArea.setText(sb.toString());
    	logArea.setCaretPosition(logArea.getDocument().getLength());
	}
}
