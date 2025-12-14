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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
            List<Text> effects = new ArrayList<>();
            List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> list = Lists.newArrayList();

            c.getEffects().forEach(i -> {
                effects.add(i.getEffectType().value().getName());
                i.getEffectType().value().forEachAttributeModifier(i.getAmplifier(), (attribute, modifier) -> list.add(new Pair<>(attribute, modifier)));
            });
            height += effects.size() * (9 + 1);
            if (effects.isEmpty()) height += 9 + 1;

            if (!list.isEmpty()) height += 9 + 1;
            height += list.size() * (9 + 1);
        }

        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int width = 0;

        var c = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (c != null) {
            List<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> list = Lists.newArrayList();
            boolean isEmpty = true;

            for (var effect : c.getEffects()) {
                isEmpty = false;

                width = Math.max(9 + 3 + textRenderer.getWidth(getEffectText(effect, list::add)), width);
            }

            if (isEmpty)
                width = Math.max(textRenderer.getWidth(Text.translatable("effect.none").formatted(Formatting.GRAY)), width);

            if (!list.isEmpty())
                width = Math.max(textRenderer.getWidth(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE)), width);

            for (Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : list) {
                Text modifierText = getModifierText(pair);
                if (modifierText != null)
                    width = Math.max(3 + textRenderer.getWidth(modifierText), width);
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
        boolean isEmpty = true;

        for (var effect : c.getEffects()) {
            isEmpty = false;
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

            context.drawText(
                    textRenderer,
                    getEffectText(effect, list::add),
                    x + textRenderer.fontHeight + 3,
                    lineY,
                    0xffffffff,
                    true
            );
        }

        if (isEmpty) {
            lineY += textRenderer.fontHeight + 1;
            context.drawText(
                    textRenderer,
                    Text.translatable("effect.none").formatted(Formatting.GRAY),
                    x,
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
                    x,
                    lineY,
                    0xffffffff,
                    true
            );
        }

        for (Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair : list) {
            lineY += textRenderer.fontHeight + 1;

            Text modifierText = getModifierText(pair);
            if (modifierText == null) continue;

            context.drawText(
                    textRenderer,
                    modifierText,
                    x + 3,
                    lineY,
                    0xffffffff,
                    true
            );
        }
    }

    private Text getEffectText(StatusEffectInstance effect, Consumer<Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier>> list) {
        RegistryEntry<StatusEffect> registryEntry = effect.getEffectType();
        int amplifier = effect.getAmplifier();
        registryEntry.value().forEachAttributeModifier(amplifier, (attribute, modifier) -> list.accept(new Pair<>(attribute, modifier)));
        MutableText name = Text.translatable(effect.getEffectType().value().getTranslationKey());
        MutableText effectText = amplifier > 0 ? Text.translatable("potion.withAmplifier", name, Text.translatable("potion.potency." + amplifier)) : name;

        if (!effect.isDurationBelow(20)) {
            //? if >1.21.4 {
            float durationMultiplier = stack.getOrDefault(DataComponentTypes.POTION_DURATION_SCALE, 1f);
            //?} else {
            /*float durationMultiplier = stack.isOf(Items.LINGERING_POTION) ? 0.25f : 1;
            *///?}
            effectText = Text.translatable("potion.withDuration", effectText, StatusEffectUtil.getDurationText(effect, durationMultiplier, 20));
        }

        return effectText;
    }

    private static Text getModifierText(Pair<RegistryEntry<EntityAttribute>, EntityAttributeModifier> pair) {
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
        } else modifierText = null;

        return modifierText;
    }
}
