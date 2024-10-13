package ru.AlertKaput.nationsForge.utils;

import org.bukkit.ChatColor;

public class ColorUtils {

    public static String hexToRgb(String hex) {
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return String.format("§x§%01x§%01x§%01x§%01x§%01x§%01x",
                (r >> 4) & 0xF, r & 0xF,
                (g >> 4) & 0xF, g & 0xF,
                (b >> 4) & 0xF, b & 0xF
        );
    }
}