package dev.ultimatchamp.enhancedtooltips.component;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

//? if >1.21.5 {
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
//?} else {
/*import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
*///?}

public record PotionEffectTooltipComponent(ItemStack stack) implements TooltipComponent {
    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        int height = 0;

        var c = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (c != null) {
            List<Text> list = new ArrayList<>();
            c.getEffects().forEach(i -> list.add(i.getEffectType().value().getName()));
            height = list.size() * (9 + 1);
        }

        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int width = 0;

        var c = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (c != null) {
            List<Text> list = new ArrayList<>();
            c.getEffects().forEach(i -> list.add(i.getEffectType().value().getName()));
            for (Text effect : list) {
                width += 9 + 3 + textRenderer.getWidth(effect);
            }
        }

        return width;
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        var c = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (c == null) return;

        int lineY = y - (textRenderer.fontHeight + 1);
        List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> list = Lists.newArrayList();

        for (var effect : c.getEffects()) {
            lineY += textRenderer.fontHeight + 1;

            //? if >1.21.5 {
            Identifier effectTexture = InGameHud.getEffectTexture(effect.getEffectType());
            //?} else {
            /*Sprite effectTexture = MinecraftClient.getInstance().getStatusEffectSpriteManager().getSprite(effect.getEffectType());
            *///?}

            if (effectTexture == null) return;

            //? if >1.21.5 {
            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, effectTexture, x, lineY - 1, textRenderer.fontHeight, textRenderer.fontHeight);
             //?} else if >1.21.1 {
            /*context.drawSpriteStretched(RenderLayer::getGuiTextured, effectTexture, x, lineY - 1, textRenderer.fontHeight, textRenderer.fontHeight);
             *///?} else {
            /*context.drawSprite(x, lineY - 1, 0, textRenderer.fontHeight, textRenderer.fontHeight, effectTexture);
            *///?}

            RegistryEntry<StatusEffect> registryEntry = effect.getEffectType();
            int amplifier = effect.getAmplifier();
            registryEntry.value().forEachAttributeModifier(amplifier, (attribute, modifier) -> list.add(new Pair<>(attribute, modifier)));
            MutableText name = Text.translatable(effect.getEffectType().value().getTranslationKey());
            MutableText effectText = amplifier > 0 ? Text.translatable("potion.withAmplifier", name, Text.translatable("potion.potency." + amplifier)) : name;
            if (!effect.isDurationBelow(20))
                effectText = Text.translatable("potion.withDuration", effectText, StatusEffectUtil.getDurationText(effect, 1, 20));

            context.drawText(
                    textRenderer,
                    effectText,
                    x + textRenderer.fontHeight + 3,
                    lineY,
                    0xffffffff,
                    true
            );
        }

        if (c.getEffects() != null) {
            lineY += textRenderer.fontHeight + 1;
            context.drawText(
                    textRenderer,
                    Text.translatable("effect.none").formatted(Formatting.GRAY),
                    x + textRenderer.fontHeight + 3,
                    lineY,
                    0xffffffff,
                    true
            );
        }

        if (!list.isEmpty()) {
            lineY += textRenderer.fontHeight + 1;
            context.drawText(
                    textRenderer,
                    Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE),
                    x + textRenderer.fontHeight + 3,
                    lineY,
                    0xffffffff,
                    true
            );
        }

        for(Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : list) {
            lineY += textRenderer.fontHeight + 1;

            EntityAttributeModifier modifier = pair.getSecond();
            double value;
            if (modifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE && modifier.operation() != EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                value = modifier.value();
            else value = modifier.value() * 100;

            Text modifierText;
            if (modifier.value() > 0) {
                modifierText = Text.translatable("attribute.modifier.plus." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(value), Text.translatable(pair.getFirst().value().getTranslationKey())).formatted(Formatting.BLUE);
            } else if (modifier.value() < 0) {
                value *= -1;
                modifierText = Text.translatable("attribute.modifier.take." + modifier.operation().getId(), AttributeModifiersComponent.DECIMAL_FORMAT.format(value), Text.translatable(pair.getFirst().value().getTranslationKey())).formatted(Formatting.RED);
            } else continue;

            context.drawText(
                    textRenderer,
                    modifierText,
                    x + textRenderer.fontHeight + 3,
                    lineY,
                    0xffffffff,
                    true
            );
        }
    }
}
