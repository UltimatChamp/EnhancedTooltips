package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.texture.Sprite;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

//? if >1.21.1 {
import net.minecraft.client.render.RenderLayer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
//?} else {
/*import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
*///?}

public class FoodTooltipComponent implements TooltipComponent {
    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;

    public FoodTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
    }

    //? if >1.21.1 {
    public ConsumableComponent getConsumableComponent() {
        return this.stack.get(DataComponentTypes.CONSUMABLE);
    }
    //?}

    public FoodComponent getFoodComponent() {
        FoodComponent foodComponent;

        //? if >1.21.1 {
        foodComponent = this.stack.getItem().getComponents().get(DataComponentTypes.FOOD);
        //?} else {
        /*foodComponent = this.stack.getItem().getComponents().get(DataComponentTypes.FOOD);
        *///?}

        return foodComponent;
    }

    public int getHunger() {
        FoodComponent foodComponent = getFoodComponent();
        int hunger = 0;

        //? if >1.21.1 {
        ConsumableComponent consumableComponent = getConsumableComponent();
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
        FoodComponent foodComponent = getFoodComponent();
        int saturation = 0;
        int hunger = getHunger();

        if (foodComponent != null) {
            saturation = (int) ((foodComponent.saturation() / (hunger * 2.0)) * 100);
        }

        return saturation;
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        int height = 0;

        FoodComponent foodComponent = getFoodComponent();
        //? if >1.21.1 {
        ConsumableComponent consumableComponent = getConsumableComponent();
        if (foodComponent != null && consumableComponent != null) {
        //?} else {
        /*if (foodComponent != null) {
        *///?}
            if (config.hungerTooltip) height += 9 + 1;
            if (config.saturationTooltip) height += 9 + 1;

            if (config.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return height;

            //? if >1.21.1 {
            for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
                if (!(entry instanceof ApplyEffectsConsumeEffect applyEffectsConsumeEffect)) {
                    continue;
                }

                for (StatusEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                    height += textRenderer.fontHeight + 1;
                }
            //?} else {
            /*for (FoodComponent.StatusEffectEntry entry : foodComponent.effects()) {
                height += 9 + 1;
            *///?}
            }
        }

        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        var foodWidth = 0;
        var effectsWidth = 0;

        FoodComponent foodComponent = getFoodComponent();

        int hunger = getHunger();

        int hungerLine = 0;
        if (config.hungerTooltip) hungerLine = (int) (textRenderer.getWidth(Text.translatable(EnhancedTooltips.identifier("tooltip.hunger").toTranslationKey())) + 1 + ((textRenderer.fontHeight - 2) * (hunger / 2f)));

        int saturationLine = 0;
        if (config.saturationTooltip) saturationLine = textRenderer.getWidth(Text.translatable(EnhancedTooltips.identifier("tooltip.saturation").toTranslationKey(), "100%")) - textRenderer.fontHeight;

        foodWidth = Math.max(hungerLine, saturationLine);

        if (config.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return foodWidth;

        if (foodComponent == null) return 0;

        //? if >1.21.1 {
        ConsumableComponent consumableComponent = getConsumableComponent();

        if (consumableComponent == null) return 0;

        for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
            if (!(entry instanceof ApplyEffectsConsumeEffect applyEffectsConsumeEffect)) {
                continue;
            }

            for (StatusEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                //? if >1.21.1 {
                float probability = applyEffectsConsumeEffect.probability();
                //?} else {
                /*float probability = entry.probability();
                *///?}

                effectsWidth = Math.max(effectsWidth, textRenderer.getWidth(Text.translatable(statusEffect.getTranslationKey()).append(" (99:99)")) + (probability >= 1f ? 0 : textRenderer.getWidth(" [100%]")));
            }
        //?} else {
        /*for (FoodComponent.StatusEffectEntry entry : foodComponent.effects()) {
            effectsWidth = Math.max(effectsWidth, textRenderer.getWidth(Text.translatable(entry.effect().getTranslationKey()).append(" (99:99)")));
        *///?}
        }

        if (effectsWidth != 0 && config.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS) effectsWidth += textRenderer.fontHeight + 2;

        return Math.max(foodWidth, effectsWidth);
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        FoodComponent foodComponent = getFoodComponent();
        int hunger = getHunger();
        int saturation = getSaturation();

        //? if >1.21.1 {
        ConsumableComponent consumableComponent = getConsumableComponent();
        if (consumableComponent == null) return;
        //?}

        if (foodComponent == null) return;

        Text hungerText = Text.translatable(EnhancedTooltips.identifier("tooltip.hunger").toTranslationKey());
        Text saturationText = Text.translatable(EnhancedTooltips.identifier("tooltip.saturation").toTranslationKey(), saturation);

        var lineY = y;

        if (config.hungerTooltip) {
            context.drawText(textRenderer, hungerText, x, lineY, 0xffffffff, true);

            Identifier fullHunger = Identifier.of("minecraft", "hud/food_full");
            Identifier halfHunger = Identifier.of("minecraft", "hud/food_half");

            float fullHungers = hunger / 2f;
            boolean hasHalfHunger = (hunger % 2) != 0;

            var hungerWidth = textRenderer.getWidth(hungerText) + 1;

            for (int i = 0; i < (int) fullHungers; i++) {
                //? if >1.21.1 {
                context.drawGuiTexture(RenderLayer::getGuiTextured, fullHunger, textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                //?} else {
                /*context.drawGuiTexture(fullHunger, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                *///?}
                hungerWidth += textRenderer.fontHeight - 2;
            }

            if (hasHalfHunger) {
                //? if >1.21.1 {
                context.drawGuiTexture(RenderLayer::getGuiTextured, halfHunger, textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                //?} else {
                /*context.drawGuiTexture(halfHunger, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                *///?}
            }

            lineY += textRenderer.fontHeight + 1;
        }

        if (config.saturationTooltip) {
            textRenderer.draw(saturationText, (float) (x + 0.75), lineY, 0xff00ffff, true, context.getMatrices().peek().getPositionMatrix(), context.vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            lineY += textRenderer.fontHeight + 1;
        }

        if (config.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return;

        //? if >1.21.1 {
        for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
            if (!(entry instanceof ApplyEffectsConsumeEffect applyEffectsConsumeEffect)) {
                continue;
            }

            for (StatusEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                int c = statusEffect.getEffectType().value().getColor();
        //?} else {
        /*for (FoodComponent.StatusEffectEntry entry : foodComponent.effects()) {
            var statusEffect = entry.effect();
            int c = statusEffect.getEffectType().value().getColor();
        *///?}
                Sprite effectTexture = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect.getEffectType());

                Text effectText;

                //? if >1.21.1 {
                float probability = applyEffectsConsumeEffect.probability();
                //?} else {
                /*float probability = entry.probability();
                *///?}

                if (probability >= 1f) {
                    effectText = Text.translatable(statusEffect.getTranslationKey())
                            .append(" (").append(StatusEffectUtil.getDurationText(statusEffect, 1.0f, 20)).append(")");
                } else {
                    effectText = Text.translatable(statusEffect.getTranslationKey())
                            .append(" (").append(StatusEffectUtil.getDurationText(statusEffect, 1.0f, 20)).append(")")
                            .append(" [").append(Math.round(probability * 100) + "%").append("]");

                }

                if (config.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS) {
                    //? if >1.21.1 {
                    context.drawSpriteStretched(RenderLayer::getGuiTextured, effectTexture, x - 1, lineY - 1, textRenderer.fontHeight, textRenderer.fontHeight);
                    //?} else {
                    /*context.drawSprite(x - 2, lineY, 0, textRenderer.fontHeight, textRenderer.fontHeight, effectTexture);
                    *///?}
                    context.drawText(textRenderer, effectText, x + textRenderer.fontHeight + 2, lineY, c, true);
                } else {
                    context.drawText(textRenderer, effectText, x, lineY, c, true);
                }

                lineY += textRenderer.fontHeight + 1;
            }
        //? if >1.21.1 {
        }
        //?}
    }
}
