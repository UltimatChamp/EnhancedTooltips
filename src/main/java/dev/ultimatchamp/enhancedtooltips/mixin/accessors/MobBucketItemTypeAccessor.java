package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.MobBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobBucketItem.class)
public interface MobBucketItemTypeAccessor {
    @Accessor("type")
    EntityType<? extends Mob> get();
}
