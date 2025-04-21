package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OrderedTextTooltipComponent.class)
public interface OrderedTextTooltipComponentAccessor {
    @Accessor("text")
    OrderedText getText();
}
