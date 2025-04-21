package dev.ultimatchamp.enhancedtooltips.mixin.accessors;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.item.DecorationItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DecorationItem.class)
public interface DecorationItemEntityTypeAccessor {
    @Accessor("entityType")
    EntityType<? extends AbstractDecorationEntity> get();
}
