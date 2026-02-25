package com.Chagui68.weaponsaddon.utils;

import org.bukkit.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6})");

    /**
     * translates & and HEX color codes in a string.
     * 
     * @param message The message to colorize
     * @return The colorized string
     */
    public static String translate(String message) {
        if (message == null)
            return null;

        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String color = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char c : color.toCharArray()) {
                replacement.append('§').append(c);
            }
            matcher.appendReplacement(sb, replacement.toString());
        }
        matcher.appendTail(sb);

        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }

    /**
     * Translates a list of strings.
     */
    public static java.util.List<String> translateList(java.util.List<String> list) {
        if (list == null)
            return null;
        java.util.List<String> translated = new java.util.ArrayList<>();
        for (String s : list) {
            translated.add(translate(s));
        }
        return translated;
    }
}
