package dev.ultimatchamp.enhancedtooltips.tooltip;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.TranslationStringColorParser;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Colors;

public class TooltipHelper {
    public static Text getRarityName(ItemStack stack) {
        String key = EnhancedTooltips.MOD_ID + ".rarity." + stack.getRarity().name().toLowerCase();
        return Text.translatable(key)
                .setStyle(Style.EMPTY.withColor(Colors.GRAY));
    }

    public static Text getDisplayName(ItemStack stack) {
        //? if >1.21.3 {
        return stack.getFormattedName();
        //?} else {
        /*return Text.empty().append(stack.getName())
                .formatted(stack.getRarity().getFormatting());
        *///?}
    }

    public static int getItemBorderColor(ItemStack stack) {
        Integer color = null;

        if (EnhancedTooltipsConfig.load().border.borderColor == EnhancedTooltipsConfig.BorderColorMode.ITEM_NAME) {
            Text displayName = getDisplayName(stack);

            color = TranslationStringColorParser.getColorFromTranslation(displayName);

            TextColor textColor = displayName.getStyle().getColor();
            if (color == -1 && textColor != null) color = displayName.getStyle().getColor().getRgb();
        }

        if (color == null || color == -1 || color == 0xFFFFFF) {
            color = stack.getRarity().getFormatting().getColorValue();
            if (color == null || color == 0xFFFFFF) color = -1;
        }

        return color;
    }
}
