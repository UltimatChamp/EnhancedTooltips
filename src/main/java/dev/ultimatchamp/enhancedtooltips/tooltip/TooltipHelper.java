package dev.ultimatchamp.enhancedtooltips.tooltip;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.TranslationStringColorParser;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Colors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<Integer> getItemBorderColor(ItemStack stack) {
        List<Integer> color = new ArrayList<>(Arrays.asList(null, null));;

        if (EnhancedTooltipsConfig.load().border.borderColor == EnhancedTooltipsConfig.BorderColorMode.ITEM_NAME) {
            Text displayName = getDisplayName(stack);
            List<Text> outerSiblings = displayName.getSiblings();

            if (outerSiblings != null && !outerSiblings.isEmpty()) {
                for (Text outerSibling : outerSiblings) {
                    List<Text> innerSiblings = outerSibling.getSiblings();
                    if (innerSiblings != null && !innerSiblings.isEmpty()) {
                        for (Text innerSibling : innerSiblings) {
                            TextColor siblingColor = innerSibling.getStyle().getColor();
                            if (siblingColor != null) {
                                if (color.get(0) == null) {
                                    color.set(0, siblingColor.getRgb());
                                }
                                color.set(1, siblingColor.getRgb());
                            }
                        }
                    }
                    else {
                        TextColor siblingColor = outerSibling.getStyle().getColor();
                        if (siblingColor != null) {
                            color.set(0, siblingColor.getRgb());
                            color.set(1, siblingColor.getRgb());
                            break;
                        }
                    }
                }
            }
            if (color.get(0) == null || color.get(0) == -1) {
                Integer clr = stack.getRarity().getFormatting().getColorValue();
                color.set(0, clr);
                color.set(1, clr);
            }
        }
        else{
            if (color.get(0) == null || color.get(0) == -1 || color.get(0) == 0xFFFFFF) {
                color.set(0, stack.getRarity().getFormatting().getColorValue());
                if (color.get(0) == null || color.get(0) == 0xFFFFFF) color.set(0, -1);
            }
        }
        return color;
    }
}
