package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

public class TooltipBorderColorComponent extends TooltipBackgroundComponent {
    private final ItemStack stack;

    public TooltipBorderColorComponent(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    protected void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int page) {
        int startColor = 0xff000000 | TooltipHelper.getItemBorderColor(stack);
        if (TooltipHelper.getItemBorderColor(stack) == -1) startColor = EnhancedTooltipsConfig.BorderColor.COMMON.getColor().getRGB();

        int endColor = EnhancedTooltipsConfig.BorderColor.END_COLOR.getColor().getRGB();

        if (EnhancedTooltipsConfig.load().borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM) {
            if (stack.getRarity() == Rarity.UNCOMMON) {
                startColor = EnhancedTooltipsConfig.load().customBorderColors.uncommon.getRGB();
            } else if (stack.getRarity() == Rarity.RARE) {
                startColor = EnhancedTooltipsConfig.load().customBorderColors.rare.getRGB();
            } else if (stack.getRarity() == Rarity.EPIC) {
                startColor = EnhancedTooltipsConfig.load().customBorderColors.epic.getRGB();
            } else {
                startColor = EnhancedTooltipsConfig.load().customBorderColors.common.getRGB();
            }

            endColor = EnhancedTooltipsConfig.load().customBorderColors.endColor.getRGB();
        }

        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }
}
