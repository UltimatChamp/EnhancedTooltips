package dev.ultimatchamp.enhancedtooltips.api;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface ItemDisplayNameProvider {
    Text getDisplayName(ItemStack stack);
}
