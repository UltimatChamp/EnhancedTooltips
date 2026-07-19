package dev.ultimatchamp.enhancedtooltips.tooltip;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TooltipItemStackCache {
    private static ItemStack cache = ItemStack.EMPTY;

    public static void saveItemStack(@NotNull ItemStack stack) {
        cache = stack;
    }

    public static @NotNull ItemStack getItemStack() {
        return cache;
    }
}
