package dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.kaleido.IKaleidoModule;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.impl.TooltipItemStackCache;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;

public class TooltipModule implements IKaleidoModule {
    @Override
    public void load() {
        //? if >1.20.4 {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
        //?} else {
        /*ItemTooltipCallback.EVENT.register((stack, tooltipContext, lines) -> {
        *///?}
            TooltipItemStackCache.saveItemStack(stack);
            lines.add(Text.of(EnhancedTooltips.MARK_KEY));
        });
    }
}
