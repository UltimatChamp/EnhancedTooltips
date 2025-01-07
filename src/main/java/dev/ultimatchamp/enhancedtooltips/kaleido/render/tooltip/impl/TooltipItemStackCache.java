package dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.impl;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TooltipItemStackCache {
    private static ItemStack cache = null;

    public static void saveItemStack(ItemStack stack) {
        cache = stack;
    }

    @Nullable
    public static ItemStack getItemStack() {
        return cache;
    }
}
