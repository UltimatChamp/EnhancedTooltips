package dev.ultimatchamp.enhancedtooltips.mixin;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

//? if >1.21.4 {
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
//?}

@Mixin(PotionContentsComponent.class)
public class PotionContentsComponentMixin {
    @Inject(
            //? if >1.21.4 {
            method = "appendTooltip",
            //?} else {
            /*method = "buildTooltip(Ljava/util/function/Consumer;FF)V",
            *///?}
            at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/PotionContentsComponent;buildTooltip(Ljava/lang/Iterable;Ljava/util/function/Consumer;FF)V"),
            cancellable = true
    )
    //? if >1.21.4 {
    private void enhancedTooltips$cancelEffectTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components, CallbackInfo ci) {
    //?} else {
    /*private void enhancedTooltips$cancelEffectTooltip(Consumer<Text> textConsumer, float durationMultiplier, float tickRate, CallbackInfo ci) {
    *///?}
        if (EnhancedTooltipsConfig.load().foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS)
            ci.cancel();
    }
}
