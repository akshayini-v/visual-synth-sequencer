package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import model.SequencerModel;

public class GridPanel extends JPanel {
    private final SequencerModel model;
    private int playheadStep = -1;

    private int hoverRow = -1;
    private int hoverCol = -1;
    private int lastDraggedRow = -1;
    private int lastDraggedCol = -1;
    private boolean dragPaintValue = true;

    private static final String[] ROW_LABELS = {"C5", "B4", "A4", "G4", "F4", "E4", "D4", "C4"};
    private static final String[] FREQ_LABELS = {"523 Hz", "494 Hz", "440 Hz", "392 Hz", "349 Hz", "330 Hz", "294 Hz", "262 Hz"};

    public GridPanel(SequencerModel model) {
        this.model = model;

        int preferredWidth = 95 + UITheme.STEPS * (UITheme.CELL_SIZE + UITheme.GRID_GAP) + 50;
        int preferredHeight = 55 + UITheme.ROWS * (UITheme.CELL_SIZE + UITheme.GRID_GAP) + 50;
        setPreferredSize(new Dimension(Math.max(960, preferredWidth), Math.max(480, preferredHeight)));
        setBackground(UITheme.BG_DARK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point cell = getCellAt(e.getX(), e.getY());
                if (cell != null && model != null) {
                    boolean newState = !model.isCellActive(cell.x, cell.y);
                    dragPaintValue = newState;
                    model.setCell(cell.x, cell.y, newState);
                    lastDraggedRow = cell.x;
                    lastDraggedCol = cell.y;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastDraggedRow = -1;
                lastDraggedCol = -1;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverRow = -1;
                hoverCol = -1;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point cell = getCellAt(e.getX(), e.getY());
                int newRow = (cell != null) ? cell.x : -1;
                int newCol = (cell != null) ? cell.y : -1;
                if (newRow != hoverRow || newCol != hoverCol) {
                    hoverRow = newRow;
                    hoverCol = newCol;
                    setCursor(cell != null
                        ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getDefaultCursor());
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point cell = getCellAt(e.getX(), e.getY());
                if (cell != null && model != null) {
                    hoverRow = cell.x;
                    hoverCol = cell.y;
                    if (cell.x != lastDraggedRow || cell.y != lastDraggedCol) {
                        model.setCell(cell.x, cell.y, dragPaintValue);
                        lastDraggedRow = cell.x;
                        lastDraggedCol = cell.y;
                        repaint();
                    }
                }
            }
        });
    }

    public void setPlayheadStep(int step) {
        this.playheadStep = step;
        repaint();
    }

    public int getPlayheadStep() {
        return playheadStep;
    }

    private int getStartX() {
        int cellsW = UITheme.STEPS * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - UITheme.GRID_GAP;
        return Math.max(85, (getWidth() - cellsW) / 2 + 25);
    }

    private int getStartY() {
        int cellsH = UITheme.ROWS * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - UITheme.GRID_GAP;
        return Math.max(45, (getHeight() - cellsH) / 2 + 12);
    }

    private Point getCellAt(int mouseX, int mouseY) {
        int startX = getStartX();
        int startY = getStartY();

        for (int row = 0; row < UITheme.ROWS; row++) {
            for (int col = 0; col < UITheme.STEPS; col++) {
                int cellX = startX + col * (UITheme.CELL_SIZE + UITheme.GRID_GAP);
                int cellY = startY + row * (UITheme.CELL_SIZE + UITheme.GRID_GAP);

                if (mouseX >= cellX && mouseX <= cellX + UITheme.CELL_SIZE &&
                    mouseY >= cellY && mouseY <= cellY + UITheme.CELL_SIZE) {
                    return new Point(row, col);
                }
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int startX = getStartX();
        int startY = getStartY();

        int cellsW = UITheme.STEPS * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - UITheme.GRID_GAP;
        int cellsH = UITheme.ROWS * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - UITheme.GRID_GAP;
        int containerX = startX - 78;
        int containerY = startY - 38;
        int containerW = cellsW + 96;
        int containerH = cellsH + 56;

        // Outer subtle shadow/glow around matrix container
        g2d.setColor(new Color(0, 0, 0, 90));
        g2d.fillRoundRect(containerX + 3, containerY + 4, containerW, containerH, 18, 18);

        // Draw Matrix Container Panel
        g2d.setColor(UITheme.PANEL_BG);
        g2d.fillRoundRect(containerX, containerY, containerW, containerH, 18, 18);
        g2d.setColor(UITheme.PANEL_BORDER);
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.drawRoundRect(containerX, containerY, containerW, containerH, 18, 18);

        // Draw 4-Beat Bar Separator Lines (between steps 4-5, 8-9, 12-13)
        g2d.setColor(new Color(70, 70, 88, 110));
        for (int bar = 1; bar < 4; bar++) {
            int sepX = startX + bar * 4 * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - (UITheme.GRID_GAP / 2) - 1;
            g2d.fillRect(sepX, startY - 24, 2, cellsH + 26);
        }

        // Draw Playhead Column Highlight if active
        if (playheadStep >= 0 && playheadStep < UITheme.STEPS) {
            g2d.setColor(new Color(160, 80, 255, 38));
            int pX = startX + playheadStep * (UITheme.CELL_SIZE + UITheme.GRID_GAP) - UITheme.GRID_GAP / 2;
            g2d.fillRoundRect(pX, startY - 28, UITheme.CELL_SIZE + UITheme.GRID_GAP, cellsH + 34, 10, 10);
        }

        // Draw Column Step Numbers (1 to 16)
        g2d.setFont(UITheme.NORMAL_FONT);
        FontMetrics fmCol = g2d.getFontMetrics();
        for (int col = 0; col < UITheme.STEPS; col++) {
            String stepStr = String.valueOf(col + 1);
            int cx = startX + col * (UITheme.CELL_SIZE + UITheme.GRID_GAP) + UITheme.CELL_SIZE / 2;
            int nx = cx - fmCol.stringWidth(stepStr) / 2;

            if (col == playheadStep) {
                g2d.setColor(UITheme.PURPLE_ACTIVE);
            } else if (col % 4 == 0) {
                g2d.setColor(UITheme.TEXT_LIGHT); // Downbeat emphasis (1, 5, 9, 13)
            } else {
                g2d.setColor(UITheme.TEXT_MUTED);
            }
            g2d.drawString(stepStr, nx, startY - 12);
        }

        // Draw Matrix Grid & Row Note + Frequency Labels
        for (int row = 0; row < UITheme.ROWS; row++) {
            int rowCenterY = startY + row * (UITheme.CELL_SIZE + UITheme.GRID_GAP) + UITheme.CELL_SIZE / 2;

            // Note Name (e.g. C5)
            g2d.setFont(UITheme.NORMAL_FONT);
            g2d.setColor(UITheme.TEXT_LIGHT);
            String noteLabel = row < ROW_LABELS.length ? ROW_LABELS[row] : "R" + (row + 1);
            g2d.drawString(noteLabel, containerX + 16, rowCenterY - 2);

            // Frequency Sub-label (e.g. 523 Hz)
            g2d.setFont(UITheme.SMALL_FONT);
            g2d.setColor(UITheme.TEXT_MUTED);
            String freqLabel = row < FREQ_LABELS.length ? FREQ_LABELS[row] : "";
            g2d.drawString(freqLabel, containerX + 16, rowCenterY + 13);

            // Row Cells
            for (int col = 0; col < UITheme.STEPS; col++) {
                int x = startX + col * (UITheme.CELL_SIZE + UITheme.GRID_GAP);
                int y = startY + row * (UITheme.CELL_SIZE + UITheme.GRID_GAP);

                boolean active = (model != null) && model.isCellActive(row, col);
                boolean isTriggered = active && (col == playheadStep);
                boolean isHovered = (row == hoverRow && col == hoverCol);

                if (isTriggered) {
                    // Intense flash when playhead triggers an active note
                    g2d.setColor(new Color(220, 160, 255, 180));
                    g2d.fillRoundRect(x - 4, y - 4, UITheme.CELL_SIZE + 8, UITheme.CELL_SIZE + 8, 14, 14);
                    g2d.setColor(new Color(245, 230, 255));
                    g2d.fillRoundRect(x, y, UITheme.CELL_SIZE, UITheme.CELL_SIZE, 9, 9);
                } else if (active) {
                    // Standard active cell with purple glow
                    g2d.setColor(UITheme.PURPLE_GLOW);
                    g2d.fillRoundRect(x - 2, y - 2, UITheme.CELL_SIZE + 4, UITheme.CELL_SIZE + 4, 12, 12);
                    g2d.setColor(UITheme.PURPLE_ACTIVE);
                    g2d.fillRoundRect(x, y, UITheme.CELL_SIZE, UITheme.CELL_SIZE, 9, 9);

                    // Subtle inner highlight
                    g2d.setColor(new Color(255, 255, 255, 55));
                    g2d.fillRoundRect(x + 4, y + 4, UITheme.CELL_SIZE - 8, 6, 4, 4);
                } else {
                    // Alternating 4-step beat groups (1-4, 5-8, 9-12, 13-16)
                    boolean altBeatGroup = ((col / 4) % 2 == 1);
                    g2d.setColor(isHovered ? UITheme.CELL_HOVER
                               : altBeatGroup ? UITheme.CELL_INACTIVE_B
                               : UITheme.CELL_INACTIVE_A);
                    g2d.fillRoundRect(x, y, UITheme.CELL_SIZE, UITheme.CELL_SIZE, 9, 9);

                    if (col == playheadStep) {
                        g2d.setColor(new Color(200, 140, 255, 90));
                        g2d.drawRoundRect(x, y, UITheme.CELL_SIZE - 1, UITheme.CELL_SIZE - 1, 9, 9);
                    }
                }
            }
        }
    }
}
