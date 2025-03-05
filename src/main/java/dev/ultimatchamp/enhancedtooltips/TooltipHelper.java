package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.api.ItemBorderColorProvider;
import dev.ultimatchamp.enhancedtooltips.api.ItemDisplayNameProvider;
import dev.ultimatchamp.enhancedtooltips.api.ItemRarityNameProvider;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.TranslationStringColorParser;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class TooltipHelper {
    static ItemRarityNameProvider rarityNameProvider = new DefaultItemRarityNameProvider();
    static ItemDisplayNameProvider displayNameProvider = new DefaultItemDisplayNameProvider();
    static ItemBorderColorProvider borderColorProvider = new DefaultItemBorderColorProvider();

    public static Text getRarityName(ItemStack stack) {
        return rarityNameProvider.getRarityName(stack);
    }

    public static Text getDisplayName(ItemStack stack) {
        return displayNameProvider.getDisplayName(stack);
    }

    public static int getItemBorderColor(ItemStack stack) {
        return borderColorProvider.getItemBorderColor(stack);
    }

    private static class DefaultItemRarityNameProvider implements ItemRarityNameProvider {
        @Override
        public Text getRarityName(ItemStack stack) {
            String key = EnhancedTooltips.MOD_ID + ".rarity." + stack.getRarity().name().toLowerCase();
            return Text.translatable(key)
                    .setStyle(Style.EMPTY.withColor(Colors.GRAY));
        }
    }

    private static class DefaultItemDisplayNameProvider implements ItemDisplayNameProvider {
        @Override
        public Text getDisplayName(ItemStack stack) {
            return Text.empty().append(stack.getName()).formatted(
                    stack.getRarity().getFormatting()
            );
        }
    }

    private static class DefaultItemBorderColorProvider implements ItemBorderColorProvider {
        @Override
        public int getItemBorderColor(ItemStack stack) {
            Integer color = null;

            if (EnhancedTooltipsConfig.load().borderColor == EnhancedTooltipsConfig.BorderColorMode.ITEM_NAME) {
                color = TranslationStringColorParser.getColorFromTranslation(getDisplayName(stack));
            }

            if (color == null || color == -1) {
                color = stack.getRarity().getFormatting().getColorValue();
                if (color == null || color == 0xFFFFFF) color = 0xFFFFFFFF;
            }

            return color;
        }
    }
}
