package dev.ultimatchamp.enhancedtooltips.util;

import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class TranslationStringColorParser {
    private static final Map<Character, Integer> COLOR_MAP = new HashMap<>();

    static {
        COLOR_MAP.put('0', 0x000000); // Black
        COLOR_MAP.put('1', 0x0000AA); // Dark Blue
        COLOR_MAP.put('2', 0x00AA00); // Dark Green
        COLOR_MAP.put('3', 0x00AAAA); // Dark Aqua
        COLOR_MAP.put('4', 0xAA0000); // Dark Red
        COLOR_MAP.put('5', 0xAA00AA); // Dark Purple
        COLOR_MAP.put('6', 0xFFAA00); // Gold
        COLOR_MAP.put('7', 0xAAAAAA); // Gray
        COLOR_MAP.put('8', 0x555555); // Dark Gray
        COLOR_MAP.put('9', 0x5555FF); // Blue
        COLOR_MAP.put('a', 0x55FF55); // Green
        COLOR_MAP.put('b', 0x55FFFF); // Aqua
        COLOR_MAP.put('c', 0xFF5555); // Red
        COLOR_MAP.put('d', 0xFF55FF); // Light Purple
        COLOR_MAP.put('e', 0xFFFF55); // Yellow
        COLOR_MAP.put('f', 0xFFFFFF); // White
    }

    public static int getColorFromTranslation(Text text) {
        return getColorFromTranslation(text.getString());
    }

    public static int getColorFromTranslation(String text) {
        char[] charArray = text.toCharArray();

        if (charArray.length < 2) return 0xFFFFFFFF;

        for (int i = 0; i < charArray.length - 1; i++) {
            if (charArray[i] == '§') {
                int color = COLOR_MAP.getOrDefault(charArray[i + 1], 0xFFFFFFFF);
                if (color != 0xFFFFFFFF) return color;
            }
        }

        return 0xFFFFFFFF;
    }
}
