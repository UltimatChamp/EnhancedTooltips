package dev.ultimatchamp.enhancedtooltips.tooltip;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TooltipItemStackCache {
    private static ItemStack cache = ItemStack.EMPTY;

    public static void saveItemStack(ItemStack stack) {
        cache = stack;
    }

    @Nullable
    public static ItemStack getItemStack() {
        return cache;
    }
}
