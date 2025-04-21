package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.SpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SpawnEggItem.class)
public interface SpawnEggItemEntityTypeAccessor {
    @Accessor("type")
    EntityType<? extends MobEntity> get();
}
