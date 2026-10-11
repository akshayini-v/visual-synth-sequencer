package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class UITheme {

    private UITheme() {
        // Prevent creating objects from this class
    }

    // Keep exact constants required by the original specification
    public static final int ROWS = 8;
    public static final int STEPS = 16;

    public static final int CELL_SIZE = 45;
    public static final int GRID_GAP = 5;

    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 20);
    public static final Font NORMAL_FONT = new Font("SansSerif", Font.BOLD, 13);
    public static final Font SMALL_FONT = new Font("SansSerif", Font.PLAIN, 12);

    // Studio Dark Palette
    public static final Color BG_DARK = new Color(18, 18, 22);
    public static final Color PANEL_BG = new Color(28, 28, 35);
    public static final Color PANEL_BORDER = new Color(58, 58, 72);
    public static final Color CELL_INACTIVE_A = new Color(38, 38, 46);
    public static final Color CELL_INACTIVE_B = new Color(45, 45, 55);
    public static final Color CELL_HOVER = new Color(65, 60, 82);

    public static final Color PURPLE_PRIMARY = new Color(112, 56, 178);
    public static final Color PURPLE_GLOW = new Color(160, 80, 255);
    public static final Color PURPLE_ACTIVE = new Color(200, 140, 255);
    public static final Color GREEN_LIVE = new Color(46, 213, 115);

    public static final Color TEXT_LIGHT = new Color(230, 230, 240);
    public static final Color TEXT_MUTED = new Color(145, 145, 165);

    /**
     * Automatically locates MainApp's JFrame and JToolBar at runtime and upgrades
     * all standard Swing controls into a dark DAW studio interface without
     * modifying MainApp.java.
     */
    public static void upgradeHostWindow(JComponent childComponent) {
        Window window = SwingUtilities.getWindowAncestor(childComponent);
        if (!(window instanceof JFrame frame)) {
            return;
        }

        frame.getContentPane().setBackground(BG_DARK);

        JButton[] playBtnRef = new JButton[1];
        JButton[] stopBtnRef = new JButton[1];

        for (Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JToolBar toolBar) {
                styleToolBar(toolBar, playBtnRef, stopBtnRef);
            }
        }

        // Bind Spacebar to toggle Play / Stop using MainApp's existing buttons
        JRootPane root = frame.getRootPane();
        root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("SPACE"), "studioTogglePlay");
        root.getActionMap().put("studioTogglePlay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (childComponent instanceof GridPanel gp) {
                    if (gp.getPlayheadStep() >= 0 && stopBtnRef[0] != null) {
                        stopBtnRef[0].doClick();
                    } else if (playBtnRef[0] != null) {
                        playBtnRef[0].doClick();
                    }
                }
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private static void styleToolBar(JToolBar toolBar, JButton[] playRef, JButton[] stopRef) {
        toolBar.setBackground(PANEL_BG);
        toolBar.setOpaque(true);
        toolBar.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));
        toolBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PANEL_BORDER),
            new EmptyBorder(6, 12, 6, 12)
        ));

        for (Component c : toolBar.getComponents()) {
            if (c instanceof JButton btn) {
                String text = btn.getText() != null ? btn.getText() : "";
                if (text.contains("Play")) playRef[0] = btn;
                if (text.contains("Stop")) stopRef[0] = btn;

                boolean isPrimary = text.contains("Play") || text.contains("Import");
                styleButton(btn, isPrimary);
            } else if (c instanceof JLabel lbl) {
                lbl.setFont(NORMAL_FONT);
                lbl.setForeground(TEXT_LIGHT);
            } else if (c instanceof JSlider slider) {
                styleSlider(slider);
            } else if (c instanceof JComboBox<?> combo) {
                styleComboBox(combo);
            } else if (c instanceof JSeparator sep) {
                sep.setForeground(PANEL_BORDER);
                sep.setBackground(PANEL_BG);
            } else if (c instanceof Box.Filler) {
                c.setPreferredSize(new Dimension(2, 1));
            }
        }
    }

    private static void styleButton(JButton btn, boolean isPrimary) {
        btn.setFont(NORMAL_FONT);
        btn.setForeground(TEXT_LIGHT);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color normalBg = isPrimary ? PURPLE_PRIMARY : new Color(42, 42, 52);
        Color hoverBg = isPrimary ? new Color(135, 70, 215) : new Color(58, 58, 72);
        Color borderCol = isPrimary ? PURPLE_GLOW : PANEL_BORDER;

        btn.setBackground(normalBg);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderCol, 1, true),
            new EmptyBorder(5, 11, 5, 11)
        ));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverBg);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PURPLE_GLOW, 1, true),
                    new EmptyBorder(5, 11, 5, 11)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(normalBg);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderCol, 1, true),
                    new EmptyBorder(5, 11, 5, 11)
                ));
            }
        });
    }

    private static void styleSlider(JSlider slider) {
        slider.setOpaque(false);
        slider.setFocusable(false);
        slider.setPreferredSize(new Dimension(125, 25));
        slider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        slider.setUI(new BasicSliderUI(slider) {
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cy = trackRect.y + (trackRect.height / 2) - 2;
                int fillW = thumbRect.x + (thumbRect.width / 2) - trackRect.x;

                g2.setColor(CELL_INACTIVE_A);
                g2.fillRoundRect(trackRect.x, cy, trackRect.width, 5, 5, 5);
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

    private static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(SMALL_FONT);
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
