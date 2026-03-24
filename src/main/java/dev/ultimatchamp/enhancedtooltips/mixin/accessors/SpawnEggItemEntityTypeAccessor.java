package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnEggItem.class)
public interface SpawnEggItemEntityTypeAccessor {
    //? if <1.21.9 {
    /*@Accessor("defaultType")
    EntityType<? extends Mob> get();
    *///?}
}
