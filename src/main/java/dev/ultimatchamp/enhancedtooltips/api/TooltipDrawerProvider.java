package dev.ultimatchamp.enhancedtooltips.api;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TooltipDrawerProvider {
    private static ITooltipDrawer tooltipDrawer = null;

    public interface ITooltipDrawer {
        void drawTooltip(DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, ItemStack stack);
    }

    public static void setTooltipDrawerProvider(ITooltipDrawer provider) {
        tooltipDrawer = provider;
    }

    public static ITooltipDrawer getTooltipDrawer() {
        return tooltipDrawer;
    }
}
