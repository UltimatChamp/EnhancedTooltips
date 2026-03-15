package dev.ultimatchamp.enhancedtooltips.component;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

//? if >1.21.5 {
/*import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
*///?} else {
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//?}

public record PotionEffectTooltipComponent(ItemStack stack) implements ClientTooltipComponent {
    @Override
    public int getHeight(/*? if >1.21.1 {*/@NotNull Font textRenderer/*?}*/) {
        int height = 0;

        var c = stack.get(DataComponents.POTION_CONTENTS);
        if (c != null) {
            List<Component> effects = new ArrayList<>();
            List<Pair<Holder<@NotNull Attribute>, AttributeModifier>> list = Lists.newArrayList();

            c.getAllEffects().forEach(i -> {
                effects.add(i.getEffect().value().getDisplayName());
                i.getEffect().value().createModifiers(i.getAmplifier(), (attribute, modifier) -> list.add(new Pair<>(attribute, modifier)));
            });
            height += effects.size() * (9 + 1);
            if (effects.isEmpty()) height += 9 + 1;

            if (!list.isEmpty()) height += 9 + 1;
            height += list.size() * (9 + 1);
        }

        return height;
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        int width = 0;

        var c = stack.get(DataComponents.POTION_CONTENTS);
        if (c != null) {
            List<Pair<Holder<@NotNull Attribute>, AttributeModifier>> list = Lists.newArrayList();
            boolean isEmpty = true;

            for (var effect : c.getAllEffects()) {
                isEmpty = false;

                width = Math.max(9 + 3 + textRenderer.width(getEffectText(effect, list::add)), width);
            }

            if (isEmpty)
                width = Math.max(textRenderer.width(Component.translatable("effect.none").withStyle(ChatFormatting.GRAY)), width);

            if (!list.isEmpty())
                width = Math.max(textRenderer.width(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE)), width);

            for (Pair<Holder<@NotNull Attribute>, AttributeModifier> pair : list) {
                Component modifierText = getModifierText(pair);
                if (modifierText != null)
                    width = Math.max(3 + textRenderer.width(modifierText), width);
            }
        }

        return width;
    }

    @Override
    //? if >1.21.1 {
    public void renderImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphics context) {
    //?} else {
    /*public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
    *///?}
        var c = stack.get(DataComponents.POTION_CONTENTS);
        if (c == null) return;

        int lineY = y - (textRenderer.lineHeight + 1);
        List<Pair<Holder<@NotNull Attribute>, AttributeModifier>> list = Lists.newArrayList();
        boolean isEmpty = true;

        for (var effect : c.getAllEffects()) {
            isEmpty = false;
            lineY += textRenderer.lineHeight + 1;

            //? if >1.21.5 {
            /*ResourceLocation effectTexture = Gui.getMobEffectSprite(effect.getEffect());
            *///?} else {
            TextureAtlasSprite effectTexture = Minecraft.getInstance().getMobEffectTextures().get(effect.getEffect());
            //?}

            //? if >1.21.5 {
            /*context.blitSprite(RenderPipelines.GUI_TEXTURED, effectTexture, x, lineY - 1, textRenderer.lineHeight, textRenderer.lineHeight);
            *///?} else if >1.21.1 {
            context.blitSprite(RenderType::guiTextured, effectTexture, x, lineY - 1, textRenderer.lineHeight, textRenderer.lineHeight);
            //?} else {
            /*context.blit(x, lineY - 1, 0, textRenderer.lineHeight, textRenderer.lineHeight, effectTexture);
            *///?}

            context.drawString(
                    textRenderer,
                    getEffectText(effect, list::add),
                    x + textRenderer.lineHeight + 3,
                    lineY,
                    effect.getEffect().value().getColor()/*? if >1.21.1 {*/ | 0xFF000000/*?}*/,
                    true
            );
        }

        if (isEmpty) {
            lineY += textRenderer.lineHeight + 1;
            context.drawString(
                    textRenderer,
                    Component.translatable("effect.none").withStyle(ChatFormatting.GRAY),
                    x,
                    lineY,
                    0xffffffff,
                    true
            );
        }

        if (!list.isEmpty()) {
            lineY += textRenderer.lineHeight + 1;
            context.drawString(
                    textRenderer,
                    Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE),
                    x,
                    lineY,
                    0xffffffff,
                    true
            );
        }

        for (Pair<Holder<@NotNull Attribute>, AttributeModifier> pair : list) {
            lineY += textRenderer.lineHeight + 1;

            Component modifierText = getModifierText(pair);
            if (modifierText == null) continue;

            context.drawString(
                    textRenderer,
                    modifierText,
                    x + 3,
                    lineY,
                    0xffffffff,
                    true
            );
        }
    }

    private Component getEffectText(MobEffectInstance effect, Consumer<Pair<Holder<@NotNull Attribute>, AttributeModifier>> list) {
        Holder<@NotNull MobEffect> registryEntry = effect.getEffect();
        int amplifier = effect.getAmplifier();
        registryEntry.value().createModifiers(amplifier, (attribute, modifier) -> list.accept(new Pair<>(attribute, modifier)));
        MutableComponent name = Component.translatable(effect.getEffect().value().getDescriptionId());
        MutableComponent effectText = amplifier > 0 ? Component.translatable("potion.withAmplifier", name, Component.translatable("potion.potency." + amplifier)) : name;

        if (!effect.endsWithin(20)) {
            //? if >1.21.4 {
            float durationMultiplier = stack.getOrDefault(DataComponents.POTION_DURATION_SCALE, 1f);
            //?} else {
            /*float durationMultiplier = stack.is(Items.LINGERING_POTION) ? 0.25f : 1;
            *///?}
            effectText = Component.translatable("potion.withDuration", effectText, MobEffectUtil.formatDuration(effect, durationMultiplier, 20));
        }

        return effectText;
    }

    private static Component getModifierText(Pair<Holder<@NotNull Attribute>, AttributeModifier> pair) {
        AttributeModifier modifier = pair.getSecond();
        double value;
        if (modifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && modifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            value = modifier.amount();
        else value = modifier.amount() * 100;

        Component modifierText;
        if (modifier.amount() > 0) {
            modifierText = Component.translatable("attribute.modifier.plus." + modifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(value), Component.translatable(pair.getFirst().value().getDescriptionId())).withStyle(ChatFormatting.BLUE);
        } else if (modifier.amount() < 0) {
            value *= -1;
            modifierText = Component.translatable("attribute.modifier.take." + modifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(value), Component.translatable(pair.getFirst().value().getDescriptionId())).withStyle(ChatFormatting.RED);
        } else modifierText = null;

        return modifierText;
    }
}
