package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//? if >1.21.1 {
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
//?}

//? if >1.21.5 {
/*import net.minecraft.client.renderer.RenderPipelines;
*///?} else if >1.21.1 {
import net.minecraft.client.renderer.RenderType;
//?}

public class FoodTooltipComponent implements ClientTooltipComponent {
    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;

    public FoodTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
    }

    //? if >1.21.1 {
    public Consumable getConsumableComponent() {
        return this.stack.get(DataComponents.CONSUMABLE);
    }
    //?}

    public static FoodProperties getFoodComponent(ItemStack stack) {
        return stack.getItem().components().get(DataComponents.FOOD);
    }

    public int getHunger() {
        FoodProperties foodComponent = getFoodComponent(this.stack);
        int hunger = 0;

        //? if >1.21.1 {
        Consumable consumableComponent = getConsumableComponent();
        if (foodComponent != null && consumableComponent != null) {
            hunger = foodComponent.nutrition();
        //?} else {
        /*if (foodComponent != null) {
            hunger = foodComponent.nutrition();
        *///?}
        }

        return hunger;
    }

    public int getSaturation() {
        FoodProperties foodComponent = getFoodComponent(this.stack);
        int saturation = 0;
        int hunger = getHunger();

        if (foodComponent != null) {
            saturation = (int) ((foodComponent.saturation() / (hunger * 2.0)) * 100);
        }

        return saturation;
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/@NotNull Font textRenderer/*?}*/) {
        int height = 0;

        FoodProperties foodComponent = getFoodComponent(this.stack);
        //? if >1.21.1 {
        Consumable consumableComponent = getConsumableComponent();
        if (foodComponent != null && consumableComponent != null) {
        //?} else {
        /*if (foodComponent != null) {
        *///?}
            if (config.foodAndDrinks.hungerTooltip) height += 9 + 1;
            if (config.foodAndDrinks.saturationTooltip) height += 9 + 1;

            if (config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return height;

            //? if >1.21.1 {
            for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
                if (!(entry instanceof ApplyStatusEffectsConsumeEffect applyEffectsConsumeEffect)) {
                    continue;
                }

                for (MobEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                    height += textRenderer.lineHeight + 1;
                }
            //?} else {
            /*for (FoodProperties.PossibleEffect ignored : foodComponent.effects()) {
                height += 9 + 1;
            *///?}
            }
        }

        return height;
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        var foodWidth = 0;
        var effectsWidth = 0;

        FoodProperties foodComponent = getFoodComponent(this.stack);

        int hunger = getHunger();

        int hungerLine = 0;
        if (config.foodAndDrinks.hungerTooltip) hungerLine = (int) (textRenderer.width(Component.translatable("enhancedtooltips.tooltip.hunger")) + 1 + ((textRenderer.lineHeight - 2) * (hunger / 2f)));

        int saturationLine = 0;
        if (config.foodAndDrinks.saturationTooltip) saturationLine = textRenderer.width(Component.translatable("enhancedtooltips.tooltip.saturation"));

        foodWidth = Math.max(hungerLine, saturationLine);

        if (config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return foodWidth;

        if (foodComponent == null) return 0;

        //? if >1.21.1 {
        Consumable consumableComponent = getConsumableComponent();

        if (consumableComponent == null) return 0;

        for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
            if (!(entry instanceof ApplyStatusEffectsConsumeEffect applyEffectsConsumeEffect)) {
                continue;
            }

            for (MobEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                int amplifier = statusEffect.getAmplifier();
                float probability = applyEffectsConsumeEffect.probability();
                var text = Component.translatable(statusEffect.getDescriptionId()).append(Component.translatable("potion.potency." + amplifier)).append("  (99:99)");
                if (probability < 1f) text.append(" [100%]");
                effectsWidth = Math.max(effectsWidth, textRenderer.width(text));
            }
        //?} else {
        /*for (FoodProperties.PossibleEffect entry : foodComponent.effects()) {
            float probability = entry.probability();
            var text = Component.translatable(entry.effect().getDescriptionId()).append(" (99:99)");
            if (probability < 1f) text.append(" [100%]");
            effectsWidth = Math.max(effectsWidth, textRenderer.width(text));
        *///?}
        }

        if (effectsWidth != 0 && config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS) effectsWidth += textRenderer.lineHeight + 3;

        return Math.max(foodWidth, effectsWidth);
    }

    @Override
    //? if >1.21.1 {
    public void renderImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphics context) {
    //?} else {
    /*public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
    *///?}
        FoodProperties foodComponent = getFoodComponent(this.stack);
        int hunger = getHunger();
        int saturation = getSaturation();

        //? if >1.21.1 {
        Consumable consumableComponent = getConsumableComponent();
        if (consumableComponent == null) return;
        //?}

        if (foodComponent == null) return;

        Component hungerText = Component.translatable("enhancedtooltips.tooltip.hunger");
        Component saturationText = Component.translatable("enhancedtooltips.tooltip.saturation", saturation);

        var lineY = y;

        if (config.foodAndDrinks.hungerTooltip) {
            context.drawString(textRenderer, hungerText, x, lineY, 0xffffffff, true);

            ResourceLocation fullHunger = ResourceLocation.fromNamespaceAndPath("minecraft", "hud/food_full");
            ResourceLocation halfHunger = ResourceLocation.fromNamespaceAndPath("minecraft", "hud/food_half");

            float fullHungers = hunger / 2f;
            boolean hasHalfHunger = (hunger % 2) != 0;

            var hungerWidth = textRenderer.width(hungerText) + 1;

            for (int i = 0; i < (int) fullHungers; i++) {
                //? if >1.21.5 {
                /*context.blitSprite(RenderPipelines.GUI_TEXTURED, fullHunger, textRenderer.lineHeight, textRenderer.lineHeight, 0, 0, x + hungerWidth, lineY, textRenderer.lineHeight, textRenderer.lineHeight);
                *///?} else if >1.21.1 {
                context.blitSprite(RenderType::guiTextured, fullHunger, textRenderer.lineHeight, textRenderer.lineHeight, 0, 0, x + hungerWidth, lineY, textRenderer.lineHeight, textRenderer.lineHeight);
                //?} else {
                /*context.blitSprite(fullHunger, x + hungerWidth, lineY, textRenderer.lineHeight, textRenderer.lineHeight);
                *///?}
                hungerWidth += textRenderer.lineHeight - 2;
            }

            if (hasHalfHunger) {
                //? if >1.21.5 {
                /*context.blitSprite(RenderPipelines.GUI_TEXTURED, halfHunger, textRenderer.lineHeight, textRenderer.lineHeight, 0, 0, x + hungerWidth, lineY, textRenderer.lineHeight, textRenderer.lineHeight);
                *///?} else if >1.21.1 {
                context.blitSprite(RenderType::guiTextured, halfHunger, textRenderer.lineHeight, textRenderer.lineHeight, 0, 0, x + hungerWidth, lineY, textRenderer.lineHeight, textRenderer.lineHeight);
                //?} else {
                /*context.blitSprite(halfHunger, x + hungerWidth, lineY, textRenderer.lineHeight, textRenderer.lineHeight);
                *///?}
            }

            lineY += textRenderer.lineHeight + 1;
        }

        if (config.foodAndDrinks.saturationTooltip) {
            context.drawString(textRenderer, saturationText, x + 2, lineY, 0xff00ffff, true);
            lineY += textRenderer.lineHeight + 1;
        }

        if (config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return;

        //? if >1.21.1 {
        for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
            if (!(entry instanceof ApplyStatusEffectsConsumeEffect applyEffectsConsumeEffect)) {
                continue;
            }

            for (MobEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                int c = statusEffect.getEffect().value().getColor() | 0xFF000000;
        //?} else {
        /*for (FoodProperties.PossibleEffect entry : foodComponent.effects()) {
            var statusEffect = entry.effect();
            int c = statusEffect.getEffect().value().getColor();
        *///?}
                int amplifier = statusEffect.getAmplifier();
                //? if >1.21.5 {
                /*ResourceLocation effectTexture = Gui.getMobEffectSprite(statusEffect.getEffect());
                *///?} else {
                TextureAtlasSprite effectTexture = Minecraft.getInstance().getMobEffectTextures().get(statusEffect.getEffect());
                //?}

                MutableComponent effectText = amplifier > 0 ? Component.translatable("potion.withAmplifier", Component.translatable(statusEffect.getDescriptionId()), Component.translatable("potion.potency." + amplifier)) : Component.translatable(statusEffect.getDescriptionId());

                //? if >1.21.1 {
                float probability = applyEffectsConsumeEffect.probability();
                //?} else {
                /*float probability = entry.probability();
                *///?}

                if (probability >= 1f) {
                    effectText = effectText
                            .append(" (").append(MobEffectUtil.formatDuration(statusEffect, 1.0f, 20)).append(")");
                } else {
                    effectText = effectText
                            .append(" (").append(MobEffectUtil.formatDuration(statusEffect, 1.0f, 20)).append(")")
                            .append(" [").append(Math.round(probability * 100) + "%").append("]");
                }

                if (config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS) {
                    //? if >1.21.5 {
                    /*context.blitSprite(RenderPipelines.GUI_TEXTURED, effectTexture, x, lineY - 1, textRenderer.lineHeight, textRenderer.lineHeight);
                    *///?} else if >1.21.1 {
                    context.blitSprite(RenderType::guiTextured, effectTexture, x, lineY - 1, textRenderer.lineHeight, textRenderer.lineHeight);
                    //?} else {
                    /*context.blit(x, lineY - 1, 0, textRenderer.lineHeight, textRenderer.lineHeight, effectTexture);
                    *///?}
                    context.drawString(textRenderer, effectText, x + textRenderer.lineHeight + 3, lineY, c, true);
                } else {
                    context.drawString(textRenderer, effectText, x, lineY, c, true);
                }

                lineY += textRenderer.lineHeight + 1;
            }
        //? if >1.21.1 {
        }
        //?}
    }
}
