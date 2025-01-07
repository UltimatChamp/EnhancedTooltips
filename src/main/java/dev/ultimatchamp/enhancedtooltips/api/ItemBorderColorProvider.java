package dev.ultimatchamp.enhancedtooltips.api;

import net.minecraft.item.ItemStack;

public interface ItemBorderColorProvider {
    int getItemBorderColor(ItemStack stack);
}
