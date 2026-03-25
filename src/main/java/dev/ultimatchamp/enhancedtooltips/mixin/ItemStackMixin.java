package dev.ultimatchamp.enhancedtooltips.mixin;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipItemStackCache;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    //? if <=1.21.5 || !neoforge {
    @Inject(method = "getTooltipLines", at = @At(value = "RETURN", ordinal = 1))
    private void enhancedTooltips$getTooltip(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> cir) {
        if (cir.getReturnValue() == null || cir.getReturnValue().isEmpty()) return;
        TooltipItemStackCache.saveItemStack((ItemStack) (Object) this);
    }
    //?}
}
