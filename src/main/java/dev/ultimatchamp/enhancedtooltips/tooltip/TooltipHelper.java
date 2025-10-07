package dev.ultimatchamp.enhancedtooltips.tooltip;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.TranslationStringColorParser;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.Optional;

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

    public static Integer[] getItemBorderColor(ItemStack stack) {
        Text displayName = getDisplayName(stack);
        Integer[] colors = {null, null};

        if (EnhancedTooltipsConfig.load().border.borderColor == EnhancedTooltipsConfig.BorderColorMode.ITEM_NAME) {
            displayName.visit((style, text) -> {
                var color = style.getColor();
                if (color != null) {
                    if (colors[0] == null)
                        colors[0] = color.getRgb();
                    else if (color.getRgb() != colors[0]) {
                        colors[1] = color.getRgb();
                    }
                }
                return Optional.empty();
            }, displayName.getStyle());

            if (colors[0] == null || colors[0] == -1 || colors[0] == 0xffffff) {
                Integer[] trans = TranslationStringColorParser.getColorsFromTranslation(displayName);
                colors[0] = trans[0];
                colors[1] = trans[1];
            }

            if (colors[0] == null || colors[0] == -1 || colors[0] == 0xffffff) {
                var clr = displayName.getStyle().getColor();
                if (clr != null) {
                    colors[0] = clr.getRgb();
                    if (colors[0] == 0xffffff) colors[0] = -1;
                }
            }
        } else {
            var clr = displayName.getStyle().getColor();
            if (clr != null) {
                colors[0] = clr.getRgb();
                if (colors[0] == 0xffffff) colors[0] = -1;
            }
        }

        return colors;
    }
}
