package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.item.HangingEntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HangingEntityItem.class)
public interface HangingEntityItemTypeAccessor {
    @Accessor("type")
    EntityType<? extends HangingEntity> get();
}
