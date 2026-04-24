package gui.panels;

import engine.data.Player;
import engine.process.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import gui.utils.GameStyle;

public class BotsPanel extends JPanel {

    private static final String ROBOT_IMAGE_PATH = "/resources/petitbonhommeglp.jpeg";
    private static final int AVATAR_SIZE = 70;
    private static final int BADGE_R     = 11;
    private static final int LABEL_H     = 18;
    private static final int PANEL_W     = AVATAR_SIZE + BADGE_R * 2 + 4;
    private static final int PANEL_H     = AVATAR_SIZE + LABEL_H + BADGE_R + 8;
    private static final BufferedImage ROBOT_IMAGE = loadRobotImage();

    private final Player player;
    private final Game   game;

    public BotsPanel(Player player, Game game) {
        this.player = player;
        this.game   = game;
        setOpaque(false);
        setPreferredSize(new Dimension(PANEL_W + 16, PANEL_H + 16));
    }

    private static BufferedImage loadRobotImage() {
        try {
            URL url = BotsPanel.class.getResource(ROBOT_IMAGE_PATH);
            if (url == null) {
                System.err.println("[BotsPanel] Image introuvable : " + ROBOT_IMAGE_PATH);
                return null;
            }
            return ImageIO.read(url);
        } catch (IOException ex) {
            System.err.println("[BotsPanel] Erreur chargement image : " + ex.getMessage());
            return null;
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int cx = getWidth()  / 2;
        int cy = getHeight() / 2 - LABEL_H / 2;

        boolean isCurrent = player.equals(game.getCurrentPlayer());
        int half = AVATAR_SIZE / 2;

        // 1. Halo doré si joueur courant
        if (isCurrent) {
            int glowMax = half + 14;
            for (int r = glowMax; r > half + 3; r--) {
                float alpha = 0.07f * (glowMax - r + 1);
                g2.setColor(new Color(1f, 0.843f, 0f, Math.min(alpha, 1f)));
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            }
            g2.setColor(GameStyle.GOLD);
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(cx - half - 5, cy - half - 5, AVATAR_SIZE + 10, AVATAR_SIZE + 10);
        }

        // 2. Ombre
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fillOval(cx - half + 3, cy - half + 5, AVATAR_SIZE, AVATAR_SIZE);

        // 3. Fond dégradé (gris foncé pour les bots)
        // Guard : le rayon doit être strictement positif, sinon fallback couleur unie
        float gradRadius = (float)(AVATAR_SIZE * 0.72);
        if (gradRadius > 0 && cx > 0 && cy > 0) {
            RadialGradientPaint rgp = new RadialGradientPaint(
                    new java.awt.geom.Point2D.Float(cx, cy - half / 3f),
                    gradRadius,
                    new float[]{0f, 1f},
                    new Color[]{GameStyle.BG_SURFACE, GameStyle.BG_DEEP});
            g2.setPaint(rgp);
        } else {
            g2.setColor(GameStyle.BG_SURFACE);
        }
        g2.fillOval(cx - half, cy - half, AVATAR_SIZE, AVATAR_SIZE);

        // 4. Initiale (pas d'image chargée ici, rester simple)
        Shape oldClip = g2.getClip();
        g2.setClip(new Ellipse2D.Float(cx - half, cy - half, AVATAR_SIZE, AVATAR_SIZE));
        if (ROBOT_IMAGE != null) {
            g2.drawImage(ROBOT_IMAGE, cx - half, cy - half, AVATAR_SIZE, AVATAR_SIZE, null);
        } else {
            g2.setColor(GameStyle.SILVER);
            String init = player.getId() != null && !player.getId().isEmpty()
                    ? String.valueOf(player.getId().charAt(0)).toUpperCase() : "?";
            g2.setFont(new Font("Arial", Font.BOLD, AVATAR_SIZE / 2));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(init, cx - fm.stringWidth(init) / 2,
                    cy + fm.getAscent() / 2 - 2);
        }
        g2.setClip(oldClip);

        // 5. Contour
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(isCurrent ? GameStyle.GOLD : new Color(180, 150, 20, 180));
        g2.drawOval(cx - half, cy - half, AVATAR_SIZE, AVATAR_SIZE);

        // 6. Badge nb cartes (coin haut-droit)
        int bx = cx + half - 1;
        int by = cy - half + 1;
        g2.setColor(GameStyle.ACCENT_RED);
        g2.fillOval(bx - BADGE_R, by - BADGE_R, BADGE_R * 2, BADGE_R * 2);
        g2.setColor(new Color(160, 0, 0));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawOval(bx - BADGE_R, by - BADGE_R, BADGE_R * 2, BADGE_R * 2);
        String cnt = String.valueOf(player.getHand() != null ? player.getHand().size() : 0);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 9));
        FontMetrics fmB = g2.getFontMetrics();
        g2.drawString(cnt, bx - fmB.stringWidth(cnt) / 2,
                by + fmB.getAscent() / 2 - 1);

        // 7. Nom sous l'avatar
        String name = player.getId() != null ? player.getId() : "?";
        if (name.length() > 9) name = name.substring(0, 8) + "…";
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        FontMetrics fmN = g2.getFontMetrics();
        int nx = cx - fmN.stringWidth(name) / 2;
        int ny = cy + half + LABEL_H - 1;

        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(name, nx + 1, ny + 1);
        g2.setColor(isCurrent ? GameStyle.GOLD : GameStyle.TEXT_MAIN);
        g2.drawString(name, nx, ny);

        g2.dispose();
    }
}
