package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

import java.util.List;

public class TooltipBorderColorComponent extends TooltipBackgroundComponent {
    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;

    public TooltipBorderColorComponent(ItemStack stack) {
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
    }

    @Override
    protected void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int page) {
        List<Integer> color = TooltipHelper.getItemBorderColor(stack);
        int startColor = 0xff000000 | color.get(0);
        int endColor = EnhancedTooltipsConfig.BorderColor.END_COLOR.getColor().getRGB();
        if (color.get(0) == -1) startColor = EnhancedTooltipsConfig.BorderColor.COMMON.getColor().getRGB();

        if(EnhancedTooltipsConfig.load().border.borderColor == EnhancedTooltipsConfig.BorderColorMode.ITEM_NAME)
        {
            endColor = 0xff000000 | color.get(1);
        }

        if (config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM) {
            if (stack.getRarity() == Rarity.UNCOMMON) {
                startColor = config.border.customBorderColors.uncommon.getRGB();
            } else if (stack.getRarity() == Rarity.RARE) {
                startColor = config.border.customBorderColors.rare.getRGB();
            } else if (stack.getRarity() == Rarity.EPIC) {
                startColor = config.border.customBorderColors.epic.getRGB();
            } else {
                startColor = config.border.customBorderColors.common.getRGB();
            }

            endColor = config.border.customBorderColors.endColor.getRGB();
        }

        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }
}
