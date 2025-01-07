package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.gui.DrawContext;

public class ColorBorderComponent extends TooltipBackgroundComponent {
    private final int color;

    public ColorBorderComponent(int color) {
        this.color = color;
    }

    @Override
    protected void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int page) {
        int endColor = 1347420415;

        renderVerticalLine(context, x, y, height - 2, z, color, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, color, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, color);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }
}
