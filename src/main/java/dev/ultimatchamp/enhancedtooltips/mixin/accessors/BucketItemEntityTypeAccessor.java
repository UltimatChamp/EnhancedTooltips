package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.EntityBucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityBucketItem.class)
public interface BucketItemEntityTypeAccessor {
    @Accessor("entityType")
    EntityType<? extends MobEntity> get();
}
