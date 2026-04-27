package gui.screens;

import gui.utils.GameStyle;
import gui.utils.MusicPlayer;
import gui.panels.*;
import engine.data.GameStats;
import engine.data.GameStats.PlayerStats;
import gui.utils.ChartManager;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.*;
import java.util.List;

/**
 * End-of-game screen displaying scores, podium, statistics table,
 * and jFreeChart graphs (pie and bar charts).
 */
public class EndGameGUI extends JFrame {

    private final GameStats stats;
    private final int       totalPlayers;
    private final Runnable  onReplay;
    private final Runnable  onMenu;


    public EndGameGUI(GameStats stats, int totalPlayers,
                      Runnable onReplay, Runnable onMenu) {
        super("Fin de Partie – Résultats");
        this.stats        = stats;
        this.totalPlayers = totalPlayers;
        this.onReplay     = onReplay;
        this.onMenu       = onMenu;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800); 
        setLocationRelativeTo(null);
        setResizable(true);
        MusicPlayer.stop();
        buildUI();
        setVisible(true);
    }

    // ── UI construction ────────────────────────────────────────────────────────

    private void buildUI() {
    	JPanel root = GameStyle.feltPanel(new BorderLayout(0, 0));
    	root.setOpaque(true);
    	root.setBorder(new EmptyBorder(24, 28, 20, 28));

    	root.add(buildHeader(), BorderLayout.NORTH);
    
    	JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
    	centerPanel.setOpaque(false);
    	centerPanel.add(buildCenter(), BorderLayout.CENTER);      // Podium + Tableau
    	centerPanel.add(buildChartsSection(), BorderLayout.SOUTH); // Graphiques
    
    	root.add(centerPanel, BorderLayout.CENTER);
    	root.add(buildFooter(), BorderLayout.SOUTH);

    	setContentPane(root);
	}

    // ── Header ─────────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        // Trophy emoji
        JLabel trophy = new JLabel("\uD83C\uDFC6", SwingConstants.CENTER);  // 🏆
        trophy.setFont(GameStyle.FONT_EMOJI_LG);
        trophy.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel title = GameStyle.titleLabel("FIN DE PARTIE");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Winner subtitle
        String winnerId = stats.getWinnerId();
        String subText = (winnerId != null)
                ? (winnerId.equals("Vous") ? "Vous avez gagné !" : winnerId + " a gagné !")
                : "Partie terminée";
        JLabel sub = GameStyle.subtitleLabel("🎉  " + subText);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Rounds info
        JLabel rounds = GameStyle.mutedLabel("Rounds joués : " + stats.getTotalRounds());
        rounds.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(trophy);
        p.add(Box.createVerticalStrut(4));
        p.add(title);
        p.add(Box.createVerticalStrut(6));
        p.add(sub);
        p.add(Box.createVerticalStrut(4));
        p.add(rounds);
        p.add(Box.createVerticalStrut(18));
        return p;
    }

    // ── Center: podium + table ─────────────────────────────────────────────────

    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout(16, 0));
        p.setOpaque(false);

        List<PlayerStats> sorted = getSortedPlayers();

        if (sorted.size() >= 2) {
            p.add(buildPodium(sorted), BorderLayout.WEST);
        }
        p.add(buildStatsTable(sorted), BorderLayout.CENTER);

        return p;
    }

    // ── Podium ─────────────────────────────────────────────────────────────────

    private JPanel buildPodium(List<PlayerStats> sorted) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(200, 0));

        JLabel podTitle = new JLabel("Classement", SwingConstants.CENTER);
        podTitle.setFont(GameStyle.FONT_HEADER);
        podTitle.setForeground(GameStyle.SILVER);
        podTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(podTitle);
        panel.add(Box.createVerticalStrut(10));

        // Medal labels (compatible Java 8 — use unicode escapes for safety)
        String[] medals = {"\uD83E\uDD47", "\uD83E\uDD48", "\uD83E\uDD49", "4.", "5.", "6."};
        Color[]  colors = {
            GameStyle.GOLD,
            GameStyle.SILVER,
            GameStyle.BRONZE,
            GameStyle.TEXT_MUTED,
            GameStyle.TEXT_MUTED,
            GameStyle.TEXT_MUTED
        };

        for (int i = 0; i < Math.min(sorted.size(), 6); i++) {
            PlayerStats ps = sorted.get(i);
            boolean isWinner = ps.getPlayerId().equals(stats.getWinnerId());
            JPanel row = buildPodiumRow(medals[i], ps.getPlayerId(),
                    ps.getScore(), colors[i], isWinner);
            row.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(row);
            panel.add(Box.createVerticalStrut(8));
        }
        return panel;
    }

    private JPanel buildPodiumRow(String medal, String playerId,
                                   int score, Color medalColor, boolean winner) {
        final boolean isWinner = winner;

        JPanel row = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isWinner ? GameStyle.BG_HIGHLIGHT : GameStyle.BG_SURFACE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                if (isWinner) {
                    g2.setColor(GameStyle.GOLD);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.draw(new RoundRectangle2D.Float(0, 0,
                            getWidth() - 1, getHeight() - 1, 12, 12));
                }
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(8, 10, 8, 10));
        row.setMaximumSize(new Dimension(190, 50));

        JLabel medLbl = new JLabel(medal);
        medLbl.setFont(GameStyle.FONT_EMOJI);
        medLbl.setForeground(medalColor);

        JLabel nameLbl = new JLabel(playerId);
        nameLbl.setFont(GameStyle.FONT_BOLD);
        nameLbl.setForeground(isWinner ? GameStyle.GOLD : GameStyle.TEXT_MAIN);

        JLabel scoreLbl = new JLabel(score + " pts");
        scoreLbl.setFont(GameStyle.FONT_BOLD);
        scoreLbl.setForeground(GameStyle.GOLD);

        row.add(medLbl,   BorderLayout.WEST);
        row.add(nameLbl,  BorderLayout.CENTER);
        row.add(scoreLbl, BorderLayout.EAST);
        return row;
    }

    // ── Stats table ────────────────────────────────────────────────────────────

    private JScrollPane buildStatsTable(final List<PlayerStats> sorted) {
        String[] cols = {"Joueur", "Score", "Cartes jouées", "Bombes", "Jokers", "Tours"};
        Object[][] data = new Object[sorted.size()][cols.length];

        for (int i = 0; i < sorted.size(); i++) {
            PlayerStats ps = sorted.get(i);
            boolean isWinner = ps.getPlayerId().equals(stats.getWinnerId());
            data[i][0] = (isWinner ? "\uD83D\uDC51 " : "") + ps.getPlayerId(); // 👑
            data[i][1] = ps.getScore();
            data[i][2] = ps.getCardsPlayed();
            data[i][3] = ps.getBombsPlayed();
            data[i][4] = ps.getJokersPlayed();
            data[i][5] = ps.getTurnsPlayed();
        }

        JTable table = new JTable(data, cols) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }

            @Override
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                PlayerStats ps = sorted.get(row);
                boolean win = ps.getPlayerId().equals(stats.getWinnerId());

                if (win) {
                    comp.setBackground(GameStyle.BG_HIGHLIGHT);
                } else if (row % 2 == 0) {
                    comp.setBackground(GameStyle.BG_SURFACE);
                } else {
                    comp.setBackground(GameStyle.BG_ROW_ALT);
                }

                comp.setForeground(col == 1 ? GameStyle.GOLD : GameStyle.TEXT_MAIN);

                if (comp instanceof JLabel) {
                    ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
                }
                return comp;
            }
        };

        table.setBackground(GameStyle.BG_SURFACE);
        table.setForeground(GameStyle.TEXT_MAIN);
        table.setFont(GameStyle.FONT_BODY);
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 2));
        table.setSelectionBackground(GameStyle.BG_HIGHLIGHT);
        table.setSelectionForeground(GameStyle.GOLD);

        // Header
        table.getTableHeader().setBackground(GameStyle.BG_DEEP);
        table.getTableHeader().setForeground(GameStyle.GOLD);
        table.getTableHeader().setFont(GameStyle.FONT_HEADER);
        table.getTableHeader().setReorderingAllowed(false);

        // Column widths
        int[] widths = {130, 70, 110, 80, 70, 60};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(GameStyle.BG_DEEP);
        scroll.getViewport().setBackground(GameStyle.BG_DEEP);
        scroll.setBorder(BorderFactory.createLineBorder(GameStyle.BORDER_GOLD, 1));
        return scroll;
    }

    // ── Legend ─────────────────────────────────────────────────────────────────

    private JPanel buildLegendPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        p.setOpaque(false);

        addLegendItem(p, "\uD83C\uDCCF", "Carte",        "= 1 pt",    GameStyle.ACCENT_BLUE);   // 🃏
        addLegendItem(p, "\uD83D\uDCA3", "Bombe",        "= ×2 pts",  GameStyle.ACCENT_RED);    // 💣
        addLegendItem(p, "\u2605",        "Double Joker", "= ×4 pts",  GameStyle.ACCENT_PURPLE); // ★
        return p;
    }

    private void addLegendItem(JPanel parent, String icon,
                                String name, String formula, Color accentColor) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        item.setOpaque(false);

        JLabel ico = new JLabel(icon);
        ico.setFont(GameStyle.FONT_EMOJI);

        JLabel lbl = new JLabel(name + " ");
        lbl.setFont(GameStyle.FONT_BOLD);
        lbl.setForeground(GameStyle.TEXT_MAIN);

        JLabel form = new JLabel(formula);
        form.setFont(GameStyle.FONT_BODY);
        form.setForeground(accentColor);

        item.add(ico);
        item.add(lbl);
        item.add(form);
        parent.add(item);
    }

    // ── Footer ─────────────────────────────────────────────────────────────────

    private JPanel buildFooter() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(GameStyle.goldSeparator());
        p.add(Box.createVerticalStrut(10));

        JPanel legend = buildLegendPanel();
        legend.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(legend);
        p.add(Box.createVerticalStrut(14));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnRow.setOpaque(false);

        JButton replayBtn = GameStyle.goldButton("\uD83D\uDD04  Rejouer");       // 🔄
        JButton menuBtn   = GameStyle.blueButton("\uD83C\uDFE0  Menu Principal"); // 🏠

        final EndGameGUI self = this;

        replayBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.dispose();
                onReplay.run();
            }
        });
        menuBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                self.dispose();
                onMenu.run();
            }
        });

        btnRow.add(replayBtn);
        btnRow.add(menuBtn);
        p.add(btnRow);
        return p;
    }
    
    // Chart Graphic	
    private JPanel buildChartsSection() {
    if (stats == null || stats.getAllStats().isEmpty()) {
        JPanel empty = new JPanel();
        empty.setOpaque(false);
        empty.add(new JLabel("Statistiques non disponibles"));
        return empty;
    }
    
    ChartManager cm = new ChartManager(stats);
    
    ChartPanel piePanel = new ChartPanel(cm.getScorePieChart());
    piePanel.setPreferredSize(new Dimension(320, 240));
    piePanel.setBackground(GameStyle.BG_SURFACE);
    
    ChartPanel barPanel = new ChartPanel(cm.getCardsPlayedBarChart());
    barPanel.setPreferredSize(new Dimension(420, 240));
    barPanel.setBackground(GameStyle.BG_SURFACE);
    
    JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
    panel.setOpaque(false);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panel.add(piePanel);
    panel.add(barPanel);
    
    return panel;
}

    // ── Helpers ────────────────────────────────────────────────────────────────

    private List<PlayerStats> getSortedPlayers() {
        List<PlayerStats> list = new ArrayList<PlayerStats>(stats.getAllStats().values());
        Collections.sort(list, new Comparator<PlayerStats>() {
            @Override
            public int compare(PlayerStats a, PlayerStats b) {
                return Integer.compare(b.getScore(), a.getScore());
            }
        });
        return list;
    }
}
