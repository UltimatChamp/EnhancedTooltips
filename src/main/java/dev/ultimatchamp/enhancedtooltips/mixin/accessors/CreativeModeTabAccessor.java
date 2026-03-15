package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeModeTab.class)
public interface CreativeModeTabAccessor {
    @Accessor
    CreativeModeTab.DisplayItemsGenerator getDisplayItemsGenerator();
}
