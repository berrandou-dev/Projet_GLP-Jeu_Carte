package gui.utils;

import engine.data.GameStats;
import engine.data.GameStats.PlayerStats;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.util.Map;

public class ChartManager {

    private final GameStats stats;

    public ChartManager(GameStats stats) {
        this.stats = stats;
    }

    public JFreeChart getScorePieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (Map.Entry<String, PlayerStats> entry : stats.getAllStats().entrySet()) {
            String name = entry.getKey();
            int score = entry.getValue().getScore();
            if (name.length() > 12) name = name.substring(0, 10) + "...";
            dataset.setValue(name, score);
        }
        
        JFreeChart chart = ChartFactory.createPieChart("Scores par joueur", dataset, true, true, false);
        chart.setBackgroundPaint(GameStyle.BG_SURFACE);
        chart.getTitle().setPaint(GameStyle.GOLD);
        chart.getTitle().setFont(new Font("Georgia", Font.BOLD, 14));
        if (chart.getLegend() != null) chart.getLegend().setBackgroundPaint(GameStyle.BG_SURFACE);
        
        return chart;
    }

    public JFreeChart getCardsPlayedBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (Map.Entry<String, PlayerStats> entry : stats.getAllStats().entrySet()) {
            String name = entry.getKey();
            PlayerStats ps = entry.getValue();
            if (name.length() > 12) name = name.substring(0, 10) + "...";
            dataset.setValue(ps.getCardsPlayed(), "Cartes", name);
            dataset.setValue(ps.getBombsPlayed(), "Bombes", name);
            dataset.setValue(ps.getJokersPlayed(), "Jokers", name);
        }
        
        JFreeChart chart = ChartFactory.createBarChart("Statistiques", "Joueur", "Nombre", dataset);
        chart.setBackgroundPaint(GameStyle.BG_SURFACE);
        chart.getTitle().setPaint(GameStyle.GOLD);
        chart.getTitle().setFont(new Font("Georgia", Font.BOLD, 14));
        
        return chart;
    }
}
