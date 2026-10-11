package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UITheme {

    private UITheme() {
        // Prevent instantiation
    }

    public static final int ROWS = 8;
    public static final int STEPS = 16;

    public static final int CELL_SIZE = 46;
    public static final int GRID_GAP = 6;

    // Studio Palette
    public static final Color BG_DARK = new Color(18, 18, 22);
    public static final Color PANEL_BG = new Color(28, 28, 35);
    public static final Color PANEL_BORDER = new Color(55, 55, 68);
    public static final Color CELL_INACTIVE_A = new Color(38, 38, 46);
    public static final Color CELL_INACTIVE_B = new Color(44, 44, 54); // Alternating 4-beat grouping
    public static final Color CELL_HOVER = new Color(65, 60, 82);

    public static final Color PURPLE_PRIMARY = new Color(112, 56, 178);
    public static final Color PURPLE_GLOW = new Color(160, 80, 255);
    public static final Color PURPLE_ACTIVE = new Color(200, 140, 255);
    public static final Color GREEN_LIVE = new Color(46, 213, 115);

    public static final Color TEXT_LIGHT = new Color(230, 230, 240);
    public static final Color TEXT_MUTED = new Color(140, 140, 160);

    // Typography
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 20);
    public static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font NORMAL_FONT = new Font("SansSerif", Font.BOLD, 13);
    public static final Font SMALL_FONT = new Font("SansSerif", Font.PLAIN, 11);

    /**
     * Creates a custom-rendered modern rounded button for the toolbar.
     */
    public static JButton createStyledButton(String text, boolean isPrimary) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            private boolean pressed = false;

            {
                setFont(NORMAL_FONT);
                setForeground(TEXT_LIGHT);
                setFocusPainted(false);
                setBorderPainted(false);
                setContentAreaFilled(false);
                setOpaque(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(new EmptyBorder(7, 16, 7, 16));

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        pressed = false;
                        repaint();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        pressed = true;
                        repaint();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        pressed = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg;
                Color border;
                if (isPrimary) {
                    bg = pressed ? new Color(90, 42, 148)
                       : hovered ? new Color(135, 70, 215)
                       : PURPLE_PRIMARY;
                    border = PURPLE_GLOW;
                } else {
                    bg = pressed ? new Color(35, 35, 44)
                       : hovered ? new Color(58, 58, 72)
                       : new Color(42, 42, 52);
                    border = hovered ? PURPLE_GLOW : PANEL_BORDER;
                }

                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.setColor(border);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };
        return btn;
    }

    /**
     * Styles a JSlider with a neon purple track and glowing thumb.
     */
    public static void styleSlider(JSlider slider) {
        slider.setOpaque(false);
        slider.setFocusable(false);
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        slider.setUI(new BasicSliderUI(slider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cy = trackRect.y + (trackRect.height / 2) - 2;
                int fillW = thumbRect.x + (thumbRect.width / 2) - trackRect.x;

                // Inactive track
                g2.setColor(CELL_INACTIVE_A);
                g2.fillRoundRect(trackRect.x, cy, trackRect.width, 5, 5, 5);

                // Active purple progress track
                g2.setColor(PURPLE_GLOW);
                g2.fillRoundRect(trackRect.x, cy, Math.max(0, fillW), 5, 5, 5);
                g2.dispose();
            }

            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = 14;
                int x = thumbRect.x + (thumbRect.width - size) / 2;
                int y = thumbRect.y + (thumbRect.height - size) / 2;

                g2.setColor(PURPLE_GLOW);
                g2.fillOval(x - 2, y - 2, size + 4, size + 4);
                g2.setColor(TEXT_LIGHT);
                g2.fillOval(x, y, size, size);
                g2.dispose();
            }
        });
    }

    /**
     * Styles a JComboBox to blend cleanly into the dark theme.
     */
    public static void styleComboBox(JComboBox<String> combo) {
        combo.setFont(NORMAL_FONT);
        combo.setForeground(TEXT_LIGHT);
        combo.setBackground(new Color(42, 42, 52));
        combo.setFocusable(false);
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        combo.setBorder(BorderFactory.createLineBorder(PANEL_BORDER, 1, true));
        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton("▾");
                btn.setFont(SMALL_FONT);
                btn.setForeground(PURPLE_ACTIVE);
                btn.setBackground(new Color(42, 42, 52));
                btn.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 6));
                btn.setFocusPainted(false);
                btn.setContentAreaFilled(false);
                return btn;
            }
        });
    }
}
