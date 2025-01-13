package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.api.ItemBorderColorProvider;
import dev.ultimatchamp.enhancedtooltips.api.ItemDisplayNameProvider;
import dev.ultimatchamp.enhancedtooltips.api.ItemRarityNameProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

import java.util.Optional;

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

    private static class DefaultItemRarityNameProvider implements ItemRarityNameProvider {
        @Override
        public Text getRarityName(ItemStack stack) {
            String markKey = EnhancedTooltips.MOD_ID + ".rarity." + stack.getRarity().name().toLowerCase();
            return Text.translatable(markKey)
                    .setStyle(Style.EMPTY.withColor(Colors.GRAY));
        }
    }

    private static class DefaultItemDisplayNameProvider implements ItemDisplayNameProvider {
        @Override
        public Text getDisplayName(ItemStack stack) {
            return Text.empty().append(stack.getName()).formatted(
                    //? if >1.20.4 {
                    stack.getRarity().getFormatting()
                    //?} else {
                    /*stack.getRarity().formatting
                    *///?}
            );
        }
    }

    private static class DefaultItemBorderColorProvider implements ItemBorderColorProvider {
        @Override
        public int getItemBorderColor(ItemStack itemStack) {
            Optional<RegistryKey<Item>> optional = itemStack.getRegistryEntry().getKey();
            if (optional.isPresent()) {
                String key = optional.get().getValue().toString();
                Integer value = BorderColorLoader.INSTANCE.getBorderColorMap().get(key);
                if (value != null) {
                    return value;
                }
            }
            //? if >1.20.4 {
            var color = itemStack.getRarity().getFormatting().getColorValue();
             //?} else {
            /*var color = itemStack.getRarity().formatting.getColorValue();
            *///?}
            if (color == null) {
                color = 0xffffffff;
            }
            return color;
        }
    }
}
