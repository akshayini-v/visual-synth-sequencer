package ui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * UITheme — Centralized visual constants and dynamic theme system.
 * Supports Gothic/Industrial and Neon Cyberpunk themes with real-time switching.
 * 
 * Original grid constants (ROWS, STEPS, CELL_SIZE, GRID_GAP) are preserved.
 */
public final class UITheme {

    private UITheme() {
        // Prevent creating objects from this class
    }

    // ═══════════════════════════════════════════════════════
    //  Grid Constants (unchanged from original)
    // ═══════════════════════════════════════════════════════
    public static final int ROWS = 8;
    public static final int STEPS = 16;
    public static final int CELL_SIZE = 45;
    public static final int GRID_GAP = 5;

    // ═══════════════════════════════════════════════════════
    //  Fonts (preserved + additions)
    // ═══════════════════════════════════════════════════════
    public static final Font TITLE_FONT  = new Font("SansSerif", Font.BOLD, 24);
    public static final Font NORMAL_FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font SMALL_FONT  = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font LABEL_FONT  = new Font("SansSerif", Font.BOLD, 10);
    public static final Font TINY_FONT   = new Font("SansSerif", Font.PLAIN, 9);
    public static final Font MONO_FONT   = new Font("Consolas", Font.PLAIN, 11);

    // ═══════════════════════════════════════════════════════
    //  Theme Types
    // ═══════════════════════════════════════════════════════
    public enum ThemeType {
        GOTHIC("Gothic Industrial"),
        CYBERPUNK("Neon Cyberpunk");

        public final String displayName;

        ThemeType(String name) { this.displayName = name; }

        @Override
        public String toString() { return displayName; }
    }

    // ═══════════════════════════════════════════════════════
    //  Current Theme State
    // ═══════════════════════════════════════════════════════
    private static ThemeType currentTheme = ThemeType.CYBERPUNK;

    // ═══════════════════════════════════════════════════════
    //  Dynamic Theme Colors — Updated on theme switch
    // ═══════════════════════════════════════════════════════

    // Backgrounds
    public static Color bgDark;
    public static Color bgPanel;
    public static Color bgSurface;

    // Grid
    public static Color cellInactive;
    public static Color cellInactiveHover;
    public static Color gridContainerBg;
    public static Color gridContainerBorder;

    // Accents
    public static Color accentPrimary;
    public static Color accentGlow;
    public static Color accentSecondary;

    // Playhead
    public static Color playheadOverlay;
    public static Color playheadAccent;
    public static Color playheadGlow;

    // Text
    public static Color textPrimary;
    public static Color textSecondary;
    public static Color textMuted;

    // Controls
    public static Color controlBg;
    public static Color controlBorder;
    public static Color buttonBg;
    public static Color buttonText;
    public static Color buttonHoverBg;
    public static Color buttonActiveBg;
    public static Color buttonActiveText;
    public static Color sliderTrack;
    public static Color sliderFilled;
    public static Color sliderThumb;

    // Oscilloscope
    public static Color oscilloscopeBg;
    public static Color oscilloscopeGrid;
    public static Color oscilloscopeWave;
    public static Color oscilloscopeWave2;
    public static Color oscilloscopeCenterLine;

    // Effects
    public static Color particleColor;
    public static Color separatorColor;
    public static Color shadowColor;

    // ═══════════════════════════════════════════════════════
    //  Theme Change Listeners
    // ═══════════════════════════════════════════════════════
    private static final List<Runnable> themeChangeListeners = new ArrayList<>();

    static {
        applyTheme(ThemeType.CYBERPUNK);
    }

    public static ThemeType getCurrentTheme() {
        return currentTheme;
    }

    public static void addThemeChangeListener(Runnable listener) {
        themeChangeListeners.add(listener);
    }

    public static void removeThemeChangeListener(Runnable listener) {
        themeChangeListeners.remove(listener);
    }

    public static void applyTheme(ThemeType theme) {
        currentTheme = theme;
        switch (theme) {
            case GOTHIC   -> applyGothicTheme();
            case CYBERPUNK -> applyCyberpunkTheme();
        }
        for (Runnable listener : themeChangeListeners) {
            listener.run();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  Gothic / Industrial Theme
    //  Charcoal blacks, steel greys, crimson/dark-red accents
    // ═══════════════════════════════════════════════════════
    private static void applyGothicTheme() {
        // Backgrounds
        bgDark               = new Color(16, 16, 20);
        bgPanel              = new Color(22, 22, 28);
        bgSurface            = new Color(28, 28, 34);

        // Grid
        cellInactive         = new Color(34, 34, 42);
        cellInactiveHover    = new Color(44, 44, 54);
        gridContainerBg      = new Color(26, 26, 32);
        gridContainerBorder  = new Color(52, 52, 62);

        // Accents — Crimson / Dark Red
        accentPrimary        = new Color(170, 40, 45);
        accentGlow           = new Color(210, 55, 60);
        accentSecondary      = new Color(130, 30, 35);

        // Playhead
        playheadOverlay      = new Color(170, 40, 45, 22);
        playheadAccent       = new Color(210, 55, 60, 55);
        playheadGlow         = new Color(170, 40, 45, 35);

        // Text
        textPrimary          = new Color(210, 210, 218);
        textSecondary        = new Color(165, 165, 180);
        textMuted            = new Color(105, 105, 125);

        // Controls
        controlBg            = new Color(20, 20, 26);
        controlBorder        = new Color(48, 48, 58);
        buttonBg             = new Color(38, 38, 46);
        buttonText           = new Color(185, 185, 198);
        buttonHoverBg        = new Color(50, 50, 60);
        buttonActiveBg       = new Color(170, 40, 45);
        buttonActiveText     = new Color(255, 255, 255);
        sliderTrack          = new Color(45, 45, 55);
        sliderFilled         = new Color(170, 40, 45);
        sliderThumb          = new Color(210, 55, 60);

        // Oscilloscope
        oscilloscopeBg       = new Color(12, 12, 16);
        oscilloscopeGrid     = new Color(30, 30, 38);
        oscilloscopeWave     = new Color(210, 55, 60);
        oscilloscopeWave2    = new Color(130, 30, 35);
        oscilloscopeCenterLine = new Color(52, 52, 62);

        // Effects
        particleColor        = new Color(210, 55, 60);
        separatorColor       = new Color(48, 48, 58);
        shadowColor          = new Color(0, 0, 0, 80);
    }

    // ═══════════════════════════════════════════════════════
    //  Neon Cyberpunk Theme
    //  Deep navy, neon cyan, electric purple, glowing accents
    // ═══════════════════════════════════════════════════════
    private static void applyCyberpunkTheme() {
        // Backgrounds
        bgDark               = new Color(6, 6, 22);
        bgPanel              = new Color(12, 12, 34);
        bgSurface            = new Color(18, 18, 42);

        // Grid
        cellInactive         = new Color(22, 22, 50);
        cellInactiveHover    = new Color(32, 32, 66);
        gridContainerBg      = new Color(14, 14, 38);
        gridContainerBorder  = new Color(38, 38, 78);

        // Accents — Neon Cyan + Electric Purple
        accentPrimary        = new Color(0, 230, 255);
        accentGlow           = new Color(80, 255, 255);
        accentSecondary      = new Color(168, 85, 247);

        // Playhead
        playheadOverlay      = new Color(0, 230, 255, 16);
        playheadAccent       = new Color(0, 230, 255, 45);
        playheadGlow         = new Color(0, 230, 255, 30);

        // Text
        textPrimary          = new Color(220, 220, 255);
        textSecondary        = new Color(155, 155, 210);
        textMuted            = new Color(85, 85, 145);

        // Controls
        controlBg            = new Color(8, 8, 28);
        controlBorder        = new Color(32, 32, 68);
        buttonBg             = new Color(22, 22, 52);
        buttonText           = new Color(175, 175, 215);
        buttonHoverBg        = new Color(32, 32, 68);
        buttonActiveBg       = new Color(0, 195, 215);
        buttonActiveText     = new Color(0, 0, 0);
        sliderTrack          = new Color(28, 28, 62);
        sliderFilled         = new Color(0, 195, 215);
        sliderThumb          = new Color(0, 230, 255);

        // Oscilloscope
        oscilloscopeBg       = new Color(4, 4, 18);
        oscilloscopeGrid     = new Color(18, 18, 46);
        oscilloscopeWave     = new Color(0, 230, 255);
        oscilloscopeWave2    = new Color(168, 85, 247);
        oscilloscopeCenterLine = new Color(38, 38, 78);

        // Effects
        particleColor        = new Color(0, 230, 255);
        separatorColor       = new Color(32, 32, 68);
        shadowColor          = new Color(0, 0, 0, 100);
    }

    // ═══════════════════════════════════════════════════════
    //  Utility: Create a color with modified alpha
    // ═══════════════════════════════════════════════════════
    public static Color withAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(),
                         Math.max(0, Math.min(255, alpha)));
    }
}

