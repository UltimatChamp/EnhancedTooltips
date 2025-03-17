package dev.ultimatchamp.enhancedtooltips.tooltip;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class TooltipModule {
    public void load() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            if (lines.isEmpty()) return;
            TooltipItemStackCache.saveItemStack(stack);
        });
    }
}
