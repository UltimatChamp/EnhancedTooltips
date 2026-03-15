package dev.ultimatchamp.enhancedtooltips.mixin;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.alchemy.PotionContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

//? if >1.21.4 {
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
//?}

@Mixin(PotionContents.class)
public class PotionContentsMixin {
    @Inject(
            //? if >1.21.4 {
            method = "addToTooltip",
            //?} else {
            /*method = "addPotionTooltip(Ljava/util/function/Consumer;FF)V",
            *///?}
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/alchemy/PotionContents;addPotionTooltip(Ljava/lang/Iterable;Ljava/util/function/Consumer;FF)V"),
            cancellable = true
    )
    //? if >1.21.4 {
    private void enhancedTooltips$cancelEffectTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components, CallbackInfo ci) {
    //?} else {
    /*private void enhancedTooltips$cancelEffectTooltip(Consumer<Component> textConsumer, float durationMultiplier, float tickRate, CallbackInfo ci) {
    *///?}
        if (EnhancedTooltipsConfig.load().foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS)
            ci.cancel();
    }
}
