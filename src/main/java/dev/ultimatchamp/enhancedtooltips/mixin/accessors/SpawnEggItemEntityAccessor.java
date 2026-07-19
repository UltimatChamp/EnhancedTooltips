package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.world.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;

//? if <1.21.9 {
/*import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.gen.Accessor;
*///?}

@Mixin(SpawnEggItem.class)
public interface SpawnEggItemEntityAccessor {
    //? if <1.21.9 {
    /*@Accessor("defaultType")
    EntityType<? extends Mob> get();
    *///?}
}
