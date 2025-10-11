package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

//? if >1.21.5 {
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.hud.InGameHud;
//?} else {
/*import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.render.RenderLayer;
*///?}

//? if >1.21.1 {
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

    public static FoodComponent getFoodComponent(ItemStack stack) {
        FoodComponent foodComponent;

        //? if >1.21.1 {
        foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
        //?} else {
        /*foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
        *///?}

        return foodComponent;
    }

    public int getHunger() {
        FoodComponent foodComponent = getFoodComponent(this.stack);
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
        FoodComponent foodComponent = getFoodComponent(this.stack);
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

        FoodComponent foodComponent = getFoodComponent(this.stack);
        //? if >1.21.1 {
        ConsumableComponent consumableComponent = getConsumableComponent();
        if (foodComponent != null && consumableComponent != null) {
        //?} else {
        /*if (foodComponent != null) {
        *///?}
            if (config.foodAndDrinks.hungerTooltip) height += 9 + 1;
            if (config.foodAndDrinks.saturationTooltip) height += 9 + 1;

            if (config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return height;

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

        FoodComponent foodComponent = getFoodComponent(this.stack);

        int hunger = getHunger();

        int hungerLine = 0;
        if (config.foodAndDrinks.hungerTooltip) hungerLine = (int) (textRenderer.getWidth(Text.translatable("enhancedtooltips.tooltip.hunger")) + 1 + ((textRenderer.fontHeight - 2) * (hunger / 2f)));

        int saturationLine = 0;
        if (config.foodAndDrinks.saturationTooltip) saturationLine = textRenderer.getWidth(Text.translatable("enhancedtooltips.tooltip.saturation"));

        foodWidth = Math.max(hungerLine, saturationLine);

        if (config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return foodWidth;

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
                var text = Text.translatable(statusEffect.getTranslationKey()).append(" (99:99)");
                if (probability < 1f) text.append(" [100%]");
                effectsWidth = Math.max(effectsWidth, textRenderer.getWidth(text));
            }
        //?} else {
        /*for (FoodComponent.StatusEffectEntry entry : foodComponent.effects()) {
            effectsWidth = Math.max(effectsWidth, textRenderer.getWidth(Text.translatable(entry.effect().getTranslationKey()).append(" (99:99)")));
        *///?}
        }

        if (effectsWidth != 0 && config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS) effectsWidth += textRenderer.fontHeight + 3;

        return Math.max(foodWidth, effectsWidth);
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        FoodComponent foodComponent = getFoodComponent(this.stack);
        int hunger = getHunger();
        int saturation = getSaturation();

        //? if >1.21.1 {
        ConsumableComponent consumableComponent = getConsumableComponent();
        if (consumableComponent == null) return;
        //?}

        if (foodComponent == null) return;

        Text hungerText = Text.translatable("enhancedtooltips.tooltip.hunger");
        Text saturationText = Text.translatable("enhancedtooltips.tooltip.saturation", saturation);

        var lineY = y;

        if (config.foodAndDrinks.hungerTooltip) {
            context.drawText(textRenderer, hungerText, x, lineY, 0xffffffff, true);

            Identifier fullHunger = Identifier.of("minecraft", "hud/food_full");
            Identifier halfHunger = Identifier.of("minecraft", "hud/food_half");

            float fullHungers = hunger / 2f;
            boolean hasHalfHunger = (hunger % 2) != 0;

            var hungerWidth = textRenderer.getWidth(hungerText) + 1;

            for (int i = 0; i < (int) fullHungers; i++) {
                //? if >1.21.5 {
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, fullHunger, textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                //?} else if >1.21.1 {
                /*context.drawGuiTexture(RenderLayer::getGuiTextured, fullHunger, textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                *///?} else {
                /*context.drawGuiTexture(fullHunger, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                *///?}
                hungerWidth += textRenderer.fontHeight - 2;
            }

            if (hasHalfHunger) {
                //? if >1.21.5 {
                context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, halfHunger, textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                //?} else if >1.21.1 {
                /*context.drawGuiTexture(RenderLayer::getGuiTextured, halfHunger, textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                *///?} else {
                /*context.drawGuiTexture(halfHunger, x + hungerWidth, lineY, textRenderer.fontHeight, textRenderer.fontHeight);
                *///?}
            }

            lineY += textRenderer.fontHeight + 1;
        }

        if (config.foodAndDrinks.saturationTooltip) {
            context.drawText(textRenderer, saturationText, x + 2, lineY, 0xff00ffff, true);
            lineY += textRenderer.fontHeight + 1;
        }

        if (config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) return;

        //? if >1.21.1 {
        for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
            if (!(entry instanceof ApplyEffectsConsumeEffect applyEffectsConsumeEffect)) {
                continue;
            }

            for (StatusEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                int c = statusEffect.getEffectType().value().getColor() | 0xFF000000;
        //?} else {
        /*for (FoodComponent.StatusEffectEntry entry : foodComponent.effects()) {
            var statusEffect = entry.effect();
            int c = statusEffect.getEffectType().value().getColor();
        *///?}
                //? if >1.21.5 {
                Identifier effectTexture = InGameHud.getEffectTexture(statusEffect.getEffectType());
                //?} else {
                /*Sprite effectTexture = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(statusEffect.getEffectType());
                *///?}

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

                if (config.foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS) {
                    //? if >1.21.5 {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, effectTexture, x, lineY - 1, textRenderer.fontHeight, textRenderer.fontHeight);
                    //?} else if >1.21.1 {
                    /*context.drawSpriteStretched(RenderLayer::getGuiTextured, effectTexture, x, lineY - 1, textRenderer.fontHeight, textRenderer.fontHeight);
                    *///?} else {
                    /*context.drawSprite(x, lineY - 1, 0, textRenderer.fontHeight, textRenderer.fontHeight, effectTexture);
                    *///?}
                    context.drawText(textRenderer, effectText, x + textRenderer.fontHeight + 3, lineY, c, true);
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
