package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    //? if <1.21.6 {
    @Accessor
    MultiBufferSource.BufferSource getBufferSource();
    //?}
}
