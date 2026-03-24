package dev.ultimatchamp.enhancedtooltips.tooltip;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.TranslationStringColorParser;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.CommonColors;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;

public class TooltipHelper {
    public static void renderText(final GuiGraphicsExtractor graphics, final Font font, final Component str, final int x, final int y, final int color, final boolean dropShadow) {
        //? if >1.21.11 {
        graphics.text(
        //?} else {
        /*graphics.drawString(
        *///?}
                font, str, x, y, color, dropShadow
        );
    }

    public static Component getRarityName(ItemStack stack) {
        String key = EnhancedTooltips.MOD_ID + ".rarity." + stack.getRarity().name().toLowerCase();
        return Component.translatable(key)
                .setStyle(Style.EMPTY.withColor(CommonColors.GRAY));
    }

    public static Component getDisplayName(ItemStack stack) {
        //? if >1.21.3 {
        return stack.getStyledHoverName();
        //?} else {
        /*return Component.empty().append(stack.getHoverName())
                .withStyle(stack.getRarity().color());
        *///?}
    }

    public static Integer[] getItemBorderColor(ItemStack stack) {
        Component displayName = getDisplayName(stack);
        Integer[] colors = {null, null};

        if (EnhancedTooltipsConfig.load().border.borderColor == EnhancedTooltipsConfig.BorderColorMode.ITEM_NAME) {
            displayName.visit((style, text) -> {
                var color = style.getColor();
                if (color != null) {
                    if (colors[0] == null)
                        colors[0] = color.getValue();
                    else if (color.getValue() != colors[0]) {
                        colors[1] = color.getValue();
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
                    colors[0] = clr.getValue();
                    if (colors[0] == 0xffffff) colors[0] = -1;
                }
            }
        } else {
            var clr = displayName.getStyle().getColor();
            if (clr != null) {
                colors[0] = clr.getValue();
                if (colors[0] == 0xffffff) colors[0] = -1;
            }
        }

        return colors;
    }
}
