package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;

public class TooltipBackgroundComponent implements TooltipComponent {
    protected static final int INNER_PADDING = 4;

    public void render(DrawContext context, int x, int y, int width, int height, int z, int page) throws Exception {
        int i = x - INNER_PADDING;
        int j = y - INNER_PADDING;
        int k = width + INNER_PADDING * 2;
        int l = height + INNER_PADDING * 2;

        renderHorizontalLine(context, i, j - 1, k, z, -267386864);
        renderHorizontalLine(context, i, j + l, k, z, -267386864);
        renderRectangle(context, i, j, k, l, z);
        renderVerticalLine(context, i - 1, j, l, z);
        renderVerticalLine(context, i + k, j, l, z);
        renderBorder(context, i, j + 1, k, l, z, page);
    }

    protected void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int page) {
        int startColor = 1347420415;
        int endColor = 1347420415;
        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }

    private void renderVerticalLine(DrawContext context, int x, int y, int height, int z) {
        context.fill(x, y, x + 1, y + height, z, -267386864);
    }

    protected void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    protected void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }

    protected void renderRectangle(DrawContext context, int x, int y, int width, int height, int z) {
        context.fill(x, y, x + width, y + height, z, -267386864);
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        return 0;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 0;
    }
}
