package dev.ultimatchamp.enhancedtooltips.mixin;

import com.google.common.collect.Lists;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.InventoryAccessor;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipItemStackCache;
import dev.ultimatchamp.enhancedtooltips.util.BadgesUtils;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import dev.ultimatchamp.enhancedtooltips.util.MatricesUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/*? if <1.21.6 {*/import com.mojang.math.Axis;/*?}*/

//? if >1.21.1 {
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.effect.MobEffectInstance;
//?}

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Unique private long enhancedTooltips$tiltStartTime = 0;
    @Unique private int enhancedTooltips$lastTiltSlot = -1;
    @Unique private float enhancedTooltips$tiltDirection = 0;
    @Unique private static final int enhancedTooltips$SPACING = 4;

    @Shadow public abstract Font getFont();
    @Shadow private ItemStack lastToolHighlight;
    @Shadow @Final private Minecraft minecraft;
    @Shadow private int toolHighlightTimer;

    @Inject(
            method = "renderSelectedItemName(Lnet/minecraft/client/gui/GuiGraphics;" /*? if neoforge {*/+ "I"/*?}*/ + ")V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawStringWithBackdrop(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIII)" + /*? if >1.21.5 {*//*"V"*//*?} else {*/"I"/*?}*/),
            cancellable = true
    )
    private void enhancedTooltips$renderHeldItemTooltipBackground(GuiGraphics context,/*? if neoforge {*/ int yShift,/*?}*/ CallbackInfo ci) {
        EnhancedTooltipsConfig config = EnhancedTooltipsConfig.load();
        if (config.heldItemTooltip.mode == EnhancedTooltipsConfig.HeldItemTooltipMode.OFF) return;

        List<Component> tooltip;
        if (config.heldItemTooltip.mode == EnhancedTooltipsConfig.HeldItemTooltipMode.ON) {
            tooltip = Screen.getTooltipFromItem(minecraft, lastToolHighlight);
            TooltipItemStackCache.saveItemStack(ItemStack.EMPTY);

            if (tooltip.isEmpty()) return;
        } else {
            tooltip = Lists.newArrayList(TooltipHelper.getDisplayName(lastToolHighlight));
        }

        Font textRenderer = this.getFont();

        int currentSlot = minecraft.player != null ? ((InventoryAccessor) minecraft.player.getInventory()).getSelected() : enhancedTooltips$lastTiltSlot;
        if (currentSlot != enhancedTooltips$lastTiltSlot) {
            int delta = currentSlot - enhancedTooltips$lastTiltSlot;
            if (enhancedTooltips$lastTiltSlot == 8 && currentSlot == 0) {
                delta = 1;
            } else if (enhancedTooltips$lastTiltSlot == 0 && currentSlot == 8) {
                delta = -1;
            }

            enhancedTooltips$tiltDirection = Math.signum(delta);
            enhancedTooltips$tiltStartTime = System.currentTimeMillis();
            enhancedTooltips$lastTiltSlot = currentSlot;
        }

        Tuple<@NotNull Component, @NotNull Integer> badgeText = BadgesUtils.getBadgeText(lastToolHighlight);
        if (config.general.itemBadges && !badgeText.getA().toFlatList().isEmpty()) {
            MutableComponent name = tooltip.getFirst().copy();
            Component badge = badgeText.getA().copy().withColor(badgeText.getB());

            if (!config.heldItemTooltip.hideItemName) {
                Component combined = name
                        .append(Component.literal(" (").withColor(-4539718))
                        .append(badge)
                        .append(Component.literal(")").withColor(-4539718));
                tooltip.set(0, combined);
            } else tooltip.set(0, badge);
        } else if (config.heldItemTooltip.hideItemName) tooltip.removeFirst();

        tooltip.removeIf(c -> c.getString().equals(
                Component.translatable(
                        "item.sophisticatedcore.storage.tooltip.press_for_contents",
                        Component.translatable("item.sophisticatedcore.storage.tooltip.shift")
                ).getString())
        );

        if (config.general.rarityTooltip)
            tooltip.add(Math.min(1, tooltip.size()), TooltipHelper.getRarityName(lastToolHighlight));

        if (config.heldItemTooltip.mode == EnhancedTooltipsConfig.HeldItemTooltipMode.ON)
            enhancedTooltips$addFoodTooltip(tooltip::add);

        if (minecraft.options.advancedItemTooltips) {
            tooltip.remove(Component.translatable("item.durability", lastToolHighlight.getMaxDamage() - lastToolHighlight.getDamageValue(), lastToolHighlight.getMaxDamage()));
            tooltip.remove(Component.literal(BuiltInRegistries.ITEM.getKey(lastToolHighlight.getItem()).toString()).withStyle(ChatFormatting.DARK_GRAY));
            tooltip.remove(Component.translatable("item.components", lastToolHighlight.getComponents().size()).withStyle(ChatFormatting.DARK_GRAY));
        }

        if ((!config.durability.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) || config.durability.durabilityBar) && lastToolHighlight.isDamageableItem())
            tooltip.add(Component.translatable("enhancedtooltips.tooltip.durability").append(enhancedTooltips$getDurabilityText()));

        float scale = config.heldItemTooltip.scaleFactor;
        float maxWidth = context.guiWidth() / (2 * scale);
        for (Component component : new ArrayList<>(tooltip)) {
            if (textRenderer.width(component) > maxWidth) {
                List<Component> wrapped = new ArrayList<>();
                textRenderer.split(component, (int) maxWidth).forEach(line -> wrapped.add(EnhancedTooltipsTextVisitor.get(line)));
                tooltip.addAll(tooltip.indexOf(component), wrapped);
                tooltip.remove(component);
            }
        }

        AtomicInteger cutOff = new AtomicInteger();
        tooltip.removeIf(text -> {
            if (tooltip.indexOf(text) > config.heldItemTooltip.maxLines) {
                cutOff.incrementAndGet();
                return true;
            }

            return textRenderer.width(text) == 0;
        });

        if (cutOff.get() > 0)
            tooltip.add(Component.literal("(+" + cutOff.get() + " more...)").withColor(-4539718).withStyle(s -> s.withItalic(true)));

        int width = tooltip.stream().mapToInt(textRenderer::width).max().orElse(0);
        float x = (context.guiWidth() - width * scale) / 2;
        /*? if fabric {*//*int yShift = 0;*//*?}*/
        float y = context.guiHeight() - Math.max(yShift, 59);
        y -= (textRenderer.lineHeight + enhancedTooltips$SPACING / 2f) * tooltip.size() * scale - enhancedTooltips$SPACING * 3 + enhancedTooltips$SPACING / 2f;
        if (minecraft.player.getArmorValue() > 0 &&
                minecraft.gameMode != null &&
                minecraft.gameMode.canHurtPlayer()
        ) y -= enhancedTooltips$SPACING * 2;

        float alpha = this.toolHighlightTimer * 256 / 10f;
        if (alpha > 255) {
            alpha = 255;
        }

        MatricesUtil matrices = new MatricesUtil(context.pose());

        if (!tooltip.isEmpty()) {
            matrices.pushMatrix();
            enhancedTooltips$drawTextWithBackground(textRenderer, tooltip, (int) x, (int) y, width, context, (int) alpha, scale);
            matrices.popMatrix();
        }

        ci.cancel();
    }

    @Unique
    public void enhancedTooltips$addFoodTooltip(Consumer<Component> list) {
        FoodProperties foodComponent = enhancedTooltips$getFoodComponent();
        if (foodComponent == null) return;

        int hunger = enhancedTooltips$getHunger();
        int saturation = enhancedTooltips$getSaturation();

        Component hungerText = Component.translatable("enhancedtooltips.tooltip.hunger").append(" " + hunger + " ").append(Component.translatable("effect.minecraft.hunger"));
        Component saturationText = Component.translatable("enhancedtooltips.tooltip.saturation", saturation).withColor(0xff00ffff);

        if (EnhancedTooltipsConfig.load().foodAndDrinks.hungerTooltip) list.accept(hungerText);
        if (EnhancedTooltipsConfig.load().foodAndDrinks.saturationTooltip) list.accept(saturationText);

        //? if >1.21.1 {
        Consumable consumableComponent = enhancedTooltips$getConsumableComponent();
        if (consumableComponent == null) return;

        List<ConsumeEffect> effects = consumableComponent.onConsumeEffects();

        for (ConsumeEffect entry : effects) {
            if (EnhancedTooltipsConfig.load().foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) break;

            if (!(entry instanceof ApplyStatusEffectsConsumeEffect applyEffectsConsumeEffect)) {
                continue;
            }

            for (MobEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                int c = statusEffect.getEffect().value().getColor();
        //?} else {
        /*for (FoodProperties.PossibleEffect entry : foodComponent.effects()) {
            var statusEffect = entry.effect();
            int c = statusEffect.getEffect().value().getColor();
        *///?}
                int amplifier = statusEffect.getAmplifier();
                MutableComponent name = amplifier > 0 ? Component.translatable("potion.withAmplifier", Component.translatable(statusEffect.getDescriptionId()), Component.translatable("potion.potency." + amplifier)) : Component.translatable(statusEffect.getDescriptionId());

                MutableComponent effectText = Component.literal("◈ ")
                        .append(name)
                        .append(" (").append(MobEffectUtil.formatDuration(statusEffect, 1.0f, 20)).append(")")
                        .withColor(c);

                //? if >1.21.1 {
                float probability = applyEffectsConsumeEffect.probability();
                //?} else {
                /*float probability = entry.probability();
                *///?}

                if (!(probability >= 1f)) {
                    effectText = effectText.append(" [").append(Math.round(probability * 100) + "%").append("]");
                }

                list.accept(effectText);
            }
        //? if >1.21.1 {
        }
        //?}
    }

    @Unique
    public FoodProperties enhancedTooltips$getFoodComponent() {
        return lastToolHighlight.getItem().components().get(DataComponents.FOOD);
    }

    //? if >1.21.1 {
    @Unique
    public Consumable enhancedTooltips$getConsumableComponent() {
        return lastToolHighlight.get(DataComponents.CONSUMABLE);
    }
    //?}

    @Unique
    public int enhancedTooltips$getHunger() {
        FoodProperties foodComponent = enhancedTooltips$getFoodComponent();
        int hunger = 0;

        //? if >1.21.1 {
        Consumable consumableComponent = enhancedTooltips$getConsumableComponent();
        if (foodComponent != null && consumableComponent != null) {
            hunger = foodComponent.nutrition();
        //?} else {
        /*if (foodComponent != null) {
            hunger = foodComponent.nutrition();
        *///?}
        }

        return hunger;
    }

    @Unique
    public int enhancedTooltips$getSaturation() {
        FoodProperties foodComponent = enhancedTooltips$getFoodComponent();
        int saturation = 0;
        int hunger = enhancedTooltips$getHunger();

        if (foodComponent != null) {
            saturation = (int) ((foodComponent.saturation() / (hunger * 2.0)) * 100);
        }

        return saturation;
    }

    @Unique
    private Component enhancedTooltips$getDurabilityText() {
        int remaining = lastToolHighlight.getMaxDamage() - lastToolHighlight.getDamageValue();
        if (remaining <= 0) return Component.empty();
        return switch (EnhancedTooltipsConfig.load().durability.durabilityTooltip) {
            case VALUE -> Component.literal(" ")
                    .append(Component.literal(String.valueOf(remaining)).setStyle(Style.EMPTY.withColor(lastToolHighlight.getBarColor())))
                    .append(Component.literal(" / ").setStyle(Style.EMPTY.withColor(-4539718)))
                    .append(Component.literal(String.valueOf(lastToolHighlight.getMaxDamage())).setStyle(Style.EMPTY.withColor(0xFF00FF00)));
            case PERCENTAGE -> {
                int percent = remaining * 100 / lastToolHighlight.getMaxDamage();
                yield Component.literal(" " + percent + "%").setStyle(Style.EMPTY.withColor(lastToolHighlight.getBarColor()));
            }
            default -> {
                int filledCount = (remaining * 10) / lastToolHighlight.getMaxDamage();
                MutableComponent durabilityBar = Component.literal(" ");

                for (int i = 0; i < filledCount; i++) {
                    durabilityBar.append(Component.literal("=").setStyle(Style.EMPTY.withColor(lastToolHighlight.getBarColor())));
                }

                for (int i = filledCount; i < 10; i++) {
                    durabilityBar.append(Component.literal("=").setStyle(Style.EMPTY.withColor(-4539718)));
                }

                yield durabilityBar;
            }
        };
    }

    @Unique
    private void enhancedTooltips$drawTextWithBackground(Font textRenderer, List<Component> lines, int x, int y, int width, GuiGraphics context, int alpha, float scale) {
        int bgAlpha = (0x80 * alpha) / 255 << 24;
        float tilt = enhancedTooltips$getTilt();

        MatricesUtil matrices = new MatricesUtil(context.pose());

        matrices.pushMatrix();
        matrices.trans(x + width * scale / 2f, y + (textRenderer.lineHeight * lines.size() * scale) / 2f, 0);
        matrices.scal(scale, scale, 0);

        //? if >1.21.5 {
        /*context.pose().rotate((float) Math.toRadians(tilt));
        *///?} else {
        context.pose().mulPose(Axis.ZP.rotationDegrees(tilt));
        //?}

        matrices.trans(-(x / scale + width / 2f), -(y / scale + (textRenderer.lineHeight * lines.size()) / 2f), 0);

        if (EnhancedTooltipsConfig.load().heldItemTooltip.showBackground) context.fill(
                (int) (x / scale - enhancedTooltips$SPACING + 1),
                (int) (y / scale - enhancedTooltips$SPACING / 2f),
                (int) (x / scale + width + enhancedTooltips$SPACING - 1),
                (int) (y / scale + (textRenderer.lineHeight + enhancedTooltips$SPACING / 2f) * lines.size() - enhancedTooltips$SPACING / 2f),
                bgAlpha
        );

        int textY = (int) (y / scale);
        for (Component line : lines) {
            int color = (line.getStyle().getColor() != null ? line.getStyle().getColor().getValue() : 0xFFFFFF) | (alpha << 24);
            context.drawString(textRenderer, line.copy().withColor(color),
                    (int) ((context.guiWidth() / scale - textRenderer.width(line)) / 2), textY, color, true
            );
            textY += textRenderer.lineHeight + enhancedTooltips$SPACING / 2;
        }

        int frameAlpha = (0x70 * alpha) / 255 << 24;
        if (EnhancedTooltipsConfig.load().heldItemTooltip.showBackground)
            BadgesUtils.drawFrame(context, (int) (x / scale - enhancedTooltips$SPACING),
                    (int) (y / scale - enhancedTooltips$SPACING / 2f), width + enhancedTooltips$SPACING * 2,
                    (textRenderer.lineHeight + enhancedTooltips$SPACING / 2) * lines.size() + enhancedTooltips$SPACING / 2,
                    400, frameAlpha
            );

        matrices.popMatrix();
    }

    @Unique
    private float enhancedTooltips$getTilt() {
        if (!EnhancedTooltipsConfig.load().heldItemTooltip.tiltAnimation || enhancedTooltips$tiltDirection == 0f) return 0f;

        long elapsed = System.currentTimeMillis() - enhancedTooltips$tiltStartTime;
        float duration = EnhancedTooltipsConfig.load().heldItemTooltip.tiltDuration;
        if (elapsed > duration) return 0f;

        float eased = (float) Math.pow(1f - (elapsed / duration), EnhancedTooltipsConfig.load().heldItemTooltip.tiltEasing);
        return enhancedTooltips$tiltDirection * EnhancedTooltipsConfig.load().heldItemTooltip.tiltMagnitude * eased;
    }
}
