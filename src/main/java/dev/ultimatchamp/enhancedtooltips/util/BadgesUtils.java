package dev.ultimatchamp.enhancedtooltips.util;

import net.minecraft.client.gui.DrawContext;

public class BadgesUtils {
    public static int darkenColor(int color, float factor) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = Math.max(0, (int) (red * factor));
        green = Math.max(0, (int) (green * factor));
        blue = Math.max(0, (int) (blue * factor));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static void drawFrame(DrawContext context, int x, int y, int width, int height, int z, int color) {
        renderVerticalLine(context, x, y, height - 2, z, color);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1, width - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1 + height - 1, width - 2, z, color);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }
}
