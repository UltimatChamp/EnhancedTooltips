package dev.ultimatchamp.enhancedtooltips.tooltip;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.TranslationStringColorParser;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class TooltipHelper {
    public static Text getRarityName(ItemStack stack) {
        String key = EnhancedTooltips.MOD_ID + ".rarity." + stack.getRarity().name().toLowerCase();
        return Text.translatable(key)
                .setStyle(Style.EMPTY.withColor(Colors.GRAY));
    }

    public static Text getDisplayName(ItemStack stack) {
        return Text.empty().append(stack.getName())
                .formatted(stack.getRarity().getFormatting());
    }

    public static int getItemBorderColor(ItemStack stack) {
        Integer color = null;

        if (EnhancedTooltipsConfig.load().border.borderColor == EnhancedTooltipsConfig.BorderColorMode.ITEM_NAME) {
            color = TranslationStringColorParser.getColorFromTranslation(getDisplayName(stack));
        }

        if (color == null || color == -1) {
            color = stack.getRarity().getFormatting().getColorValue();
            if (color == null || color == 0xFFFFFF) color = 0xFFFFFFFF;
        }

        return color;
    }
}
