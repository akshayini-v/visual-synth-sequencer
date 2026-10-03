package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSliderUI;

public class GridPanel extends JPanel {

    private static final Color BG_DARK = new Color(20, 20, 24);
    private static final Color TEXT_LIGHT = new Color(220, 220, 230);
    private static final Color TEXT_MUTED = new Color(140, 140, 160);
    private static final Color PURPLE_GLOW = new Color(160, 80, 255);
    private static final Color PURPLE_ACTIVE = new Color(200, 140, 255);
    private static final Color CELL_INACTIVE = new Color(40, 40, 48);
    private static final Color PLAYHEAD_COLOR = new Color(255, 255, 255, 20);

    public GridPanel() {
        setLayout(new BorderLayout());
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_DARK);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("MUSIC SEQUENCER");
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(TEXT_LIGHT);

        JLabel subtitleLabel = new JLabel("16-Step Pattern");
        subtitleLabel.setFont(UITheme.SMALL_FONT);
        subtitleLabel.setForeground(TEXT_MUTED);

        JPanel titles = new JPanel(new GridLayout(2, 1));
        titles.setBackground(BG_DARK);
        titles.add(titleLabel);
        titles.add(subtitleLabel);

        headerPanel.add(titles, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Center Grid Canvas
        add(new SequencerCanvas(), BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_DARK);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        buttonPanel.setBackground(BG_DARK);

        buttonPanel.add(createStyledButton("PLAY"));
        buttonPanel.add(createStyledButton("STOP"));
        buttonPanel.add(createStyledButton("CLEAR"));

        JPanel bpmPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        bpmPanel.setBackground(BG_DARK);
        
        JLabel bpmLabel = new JLabel("BPM: 120");
        bpmLabel.setFont(UITheme.NORMAL_FONT);
        bpmLabel.setForeground(TEXT_LIGHT);
        
        JSlider bpmSlider = new JSlider(60, 200, 120);
        bpmSlider.setBackground(BG_DARK);
        bpmSlider.setUI(new CustomSliderUI(bpmSlider));
        bpmSlider.setPreferredSize(new Dimension(150, 30));
        
        bpmPanel.add(bpmLabel);
        bpmPanel.add(bpmSlider);

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(bpmPanel, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.NORMAL_FONT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(PURPLE_GLOW.darker());
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PURPLE_GLOW, 1, true),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private static class SequencerCanvas extends JPanel {
        private final String[] ROW_LABELS = {"C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4"};
        // A dummy pattern to look nice visually
        private final int[][] pattern = {
            {0,0,1,0, 0,0,0,0, 1,0,0,0, 0,0,1,0},
            {0,0,0,0, 1,0,0,0, 0,0,1,0, 0,0,0,0},
            {1,0,0,0, 0,0,1,0, 0,0,0,0, 1,0,0,0},
            {0,1,0,1, 0,0,0,0, 0,1,0,1, 0,0,0,0},
            {0,0,0,0, 0,1,0,0, 0,0,0,0, 0,1,0,0},
            {1,0,1,0, 1,0,1,0, 1,0,1,0, 1,0,1,0},
            {0,0,0,0, 0,0,0,1, 0,0,0,0, 0,0,0,1},
            {1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0}
        };

        public SequencerCanvas() {
            setBackground(BG_DARK);
            int width = 50 + UITheme.STEPS * (UITheme.CELL_SIZE + UITheme.GRID_GAP);
            int height = 30 + UITheme.ROWS * (UITheme.CELL_SIZE + UITheme.GRID_GAP);
            setPreferredSize(new Dimension(width, height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int startX = 40;
            int startY = 30;

            // Draw grid border & background
            int gridW = UITheme.STEPS * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - UITheme.GRID_GAP + 20;
            int gridH = UITheme.ROWS * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - UITheme.GRID_GAP + 20;
            
            g2d.setColor(new Color(30, 30, 36));
            g2d.fillRoundRect(startX - 10, startY - 10, gridW, gridH, 15, 15);
            g2d.setColor(new Color(60, 60, 70));
            g2d.setStroke(new BasicStroke(1f));
            g2d.drawRoundRect(startX - 10, startY - 10, gridW, gridH, 15, 15);

            // Playhead column index
            int currentStep = 4; // visual playhead position

            // Draw Playhead highlight
            g2d.setColor(PLAYHEAD_COLOR);
            int pX = startX + currentStep * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - UITheme.GRID_GAP/2;
            g2d.fillRoundRect(pX, startY - 5, UITheme.CELL_SIZE + UITheme.GRID_GAP, gridH - 10, 10, 10);

            // Draw Column numbers
            g2d.setFont(UITheme.SMALL_FONT);
            g2d.setColor(TEXT_MUTED);
            for (int col = 0; col < UITheme.STEPS; col++) {
                String num = String.valueOf(col + 1);
                int cx = startX + col * (UITheme.CELL_SIZE + UITheme.GRID_GAP) + UITheme.CELL_SIZE / 2;
                FontMetrics fm = g2d.getFontMetrics();
                int nx = cx - fm.stringWidth(num) / 2;
                g2d.drawString(num, nx, startY - 20);
            }

            // Draw Grid
            for (int row = 0; row < UITheme.ROWS; row++) {
                // Row labels
                g2d.setColor(TEXT_LIGHT);
                g2d.setFont(UITheme.NORMAL_FONT);
                FontMetrics fm = g2d.getFontMetrics();
                int lx = startX - 25 - fm.stringWidth(ROW_LABELS[row]) / 2;
                int ly = startY + row * (UITheme.CELL_SIZE + UITheme.GRID_GAP) + UITheme.CELL_SIZE / 2 + fm.getAscent() / 2 - 2;
                g2d.drawString(ROW_LABELS[row], lx, ly);

                for (int col = 0; col < UITheme.STEPS; col++) {
                    int x = startX + col * (UITheme.CELL_SIZE + UITheme.GRID_GAP);
                    int y = startY + row * (UITheme.CELL_SIZE + UITheme.GRID_GAP);

                    boolean isActive = pattern[row][col] == 1;

                    if (isActive) {
                        // Outer glow
                        g2d.setColor(PURPLE_GLOW);
                        g2d.fillRoundRect(x - 2, y - 2, UITheme.CELL_SIZE + 4, UITheme.CELL_SIZE + 4, 12, 12);
                        // Inner active cell
                        g2d.setColor(PURPLE_ACTIVE);
                    } else {
                        g2d.setColor(CELL_INACTIVE);
                    }

                    g2d.fillRoundRect(x, y, UITheme.CELL_SIZE, UITheme.CELL_SIZE, 8, 8);
                }
            }
        }
    }

    private static class CustomSliderUI extends BasicSliderUI {
        public CustomSliderUI(JSlider b) {
            super(b);
        }
        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(PURPLE_ACTIVE);
            g2d.fillOval(thumbRect.x, thumbRect.y + 5, 12, 12);
        }
        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(CELL_INACTIVE);
            g2d.fillRoundRect(trackRect.x, trackRect.y + trackRect.height / 2 - 2, trackRect.width, 4, 4, 4);
            
            int fillW = thumbRect.x - trackRect.x;
            if (fillW > 0) {
                g2d.setColor(PURPLE_GLOW);
                g2d.fillRoundRect(trackRect.x, trackRect.y + trackRect.height / 2 - 2, fillW, 4, 4, 4);
            }
        }
    }
}
