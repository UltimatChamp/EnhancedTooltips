package dev.ultimatchamp.enhancedtooltips.mixin;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipItemStackCache;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    //? if <=1.21.5 || !neoforge {
    @Inject(method = "getTooltip", at = @At(value = "RETURN", ordinal = 1))
    private void enhancedTooltips$getTooltip(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir) {
        if (cir.getReturnValue() == null || cir.getReturnValue().isEmpty()) return;
        TooltipItemStackCache.saveItemStack((ItemStack) (Object) this);
    }
    //?}
}
