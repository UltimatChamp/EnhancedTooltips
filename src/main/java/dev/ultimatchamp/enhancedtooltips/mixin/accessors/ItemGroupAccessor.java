package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemGroup.class)
public interface ItemGroupAccessor {
    @Accessor
    ItemGroup.EntryCollector getEntryCollector();
}
