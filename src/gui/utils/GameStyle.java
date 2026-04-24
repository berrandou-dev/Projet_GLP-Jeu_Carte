package gui.utils;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

/**
 * Shared visual constants and factory methods for the game's GUI.
 * All components use this class to guarantee a consistent look.
 */
public final class GameStyle {

    private GameStyle() {}
    
    //Caractéristiques écrans
    public static boolean isFullscreen = false;

    // Palette 

    /** Deepest background – almost black-green */
    public static final Color BG_DEEP      = new Color(  5,  40,  10);
    /** Main panel/table background */
    public static final Color BG_TABLE     = new Color(  0, 100,   0);
    /** Slightly lighter surface (cards, rows) */
    public static final Color BG_SURFACE   = new Color(  0, 115,  15);
    /** Highlighted surface for winner / selected */
    public static final Color BG_HIGHLIGHT = new Color(  0, 150,  40);
    /** Alternate row colour */
    public static final Color BG_ROW_ALT   = new Color(  0, 128,  20);

    /** Primary accent – casino gold */
    public static final Color GOLD         = new Color(255, 215,   0);
    /** Dimmed gold for secondary items */
    public static final Color GOLD_DIM     = new Color(200, 165,   0);
    /** Bronze for third place */
    public static final Color BRONZE       = new Color(180, 120,  40);

    /** Bright lime for positive feedback (play, win) */
    public static final Color GREEN_BRIGHT = new Color( 50, 220,  80);
    /** Muted green-white for body text */
    public static final Color TEXT_MAIN    = new Color(230, 255, 230);
    /** Dimmed text */
    public static final Color TEXT_MUTED   = new Color(140, 180, 140);
    /** Silver for subtitles / headers */
    public static final Color SILVER       = new Color(200, 220, 190);

    /** Red for bombs / errors */
    public static final Color ACCENT_RED   = new Color(220,  53,  69);
    /** Blue for info / menu button */
    public static final Color ACCENT_BLUE  = new Color( 70, 180, 255);
    /** Purple for jokers */
    public static final Color ACCENT_PURPLE= new Color(180, 130, 255);

    /** Border/separator line */
    public static final Color BORDER_GOLD  = new Color(180, 150,  20);

    // Fonts

    public static final Font FONT_TITLE    = new Font("Georgia", Font.BOLD,  28);
    public static final Font FONT_SUBTITLE = new Font("Georgia", Font.ITALIC,16);
    public static final Font FONT_HEADER   = new Font("Arial",   Font.BOLD,  13);
    public static final Font FONT_BODY     = new Font("Arial",   Font.PLAIN, 13);
    public static final Font FONT_BOLD     = new Font("Arial",   Font.BOLD,  13);
    public static final Font FONT_BUTTON   = new Font("Arial",   Font.BOLD,  14);
    public static final Font FONT_SMALL    = new Font("Arial",   Font.PLAIN, 11);
    public static final Font FONT_EMOJI    = new Font("Segoe UI Emoji", Font.PLAIN, 20);
    public static final Font FONT_EMOJI_LG = new Font("Segoe UI Emoji", Font.PLAIN, 48);

    // Background panels

    /**
     * Creates a JPanel whose background is the felt gradient
     * (BG_TABLE → BG_DEEP, top to bottom).
     */
    public static JPanel feltPanel(LayoutManager layout) {
        JPanel p = new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0,           BG_TABLE,
                        0, getHeight(), BG_DEEP);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setOpaque(false);
        return p;
    }

    //Buttons

    /**
     * Rounded button with gold border and hover highlight.
     *
     * @param text  label
     * @param fg    foreground / border colour
     * @param bg    resting background colour
     */
    public static JButton roundButton(String text, final Color fg, final Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill;
                if (getModel().isPressed()) {
                    fill = fg.darker();
                } else if (getModel().isRollover()) {
                    fill = new Color(0, 140, 30);
                } else {
                    fill = bg;
                }
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(fg);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(fg);
        btn.setPreferredSize(new Dimension(210, 46));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /** Convenience: gold-bordered button (most common). */
    public static JButton goldButton(String text) {
        return roundButton(text, GOLD, BG_DEEP);
    }

    /** Convenience: blue-bordered button (secondary actions). */
    public static JButton blueButton(String text) {
        return roundButton(text, ACCENT_BLUE, BG_DEEP);
    }
    
    /** Convenience: purple-bordered button. */
    public static JButton purpleButton(String text) {
        return roundButton(text, ACCENT_PURPLE, BG_DEEP);
    }

    //Labels

    public static JLabel titleLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(FONT_TITLE);
        l.setForeground(GOLD);
        return l;
    }

    public static JLabel subtitleLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(FONT_SUBTITLE);
        l.setForeground(GREEN_BRIGHT);
        return l;
    }

    public static JLabel mutedLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(FONT_SMALL);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    // Separator

    public static JSeparator goldSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_GOLD);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    // Rounded card panel

    /**
     * A rounded-rectangle panel that paints its own background.
     *
     * @param bg      background colour
     * @param border  if non-null, draws a 1.5-px border in this colour
     */
    public static JPanel roundedPanel(final Color bg, final Color border) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                if (border != null) {
                    g2.setColor(border);
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.draw(new RoundRectangle2D.Float(0, 0,
                            getWidth() - 1, getHeight() - 1, 14, 14));
                }
                g2.dispose();
            }
        };
        p.setOpaque(false);
        return p;
    }
    
}
