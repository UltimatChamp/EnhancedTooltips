package dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip;

import dev.ultimatchamp.enhancedtooltips.kaleido.IKaleidoModule;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.impl.TooltipItemStackCache;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class TooltipModule implements IKaleidoModule {
    @Override
    public void load() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            TooltipItemStackCache.saveItemStack(stack);
        });
    }
}
