package dev.ultimatchamp.enhancedtooltips.mixin;

import com.google.common.collect.Lists;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.PlayerInventoryAccessor;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipItemStackCache;
import dev.ultimatchamp.enhancedtooltips.util.BadgesUtils;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.RotationAxis;
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

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Unique private long enhancedTooltips$tiltStartTime = 0;
    @Unique private int enhancedTooltips$lastTiltSlot = -1;
    @Unique private float enhancedTooltips$tiltDirection = 0;
    @Unique private static final int enhancedTooltips$SPACING = 4;

    @Shadow public abstract TextRenderer getTextRenderer();
    @Shadow private ItemStack currentStack;
    @Shadow @Final private MinecraftClient client;
    @Shadow private int heldItemTooltipFade;

    @Inject(
            //? if fabric {
            method = "renderHeldItemTooltip",
            //?} else if neoforge {
            /*method = "renderSelectedItemName",
            *///?}
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"),
            cancellable = true
    )
    private void enhancedTooltips$renderHeldItemTooltipBackground(DrawContext context,/*? if neoforge {*/ /*int YShift,*//*?}*/ CallbackInfo ci) {
        EnhancedTooltipsConfig config = EnhancedTooltipsConfig.load();
        if (config.heldItemTooltip.mode == EnhancedTooltipsConfig.HeldItemTooltipMode.OFF) return;

        List<Text> tooltip;
        if (config.heldItemTooltip.mode == EnhancedTooltipsConfig.HeldItemTooltipMode.ON) {
            tooltip = Screen.getTooltipFromItem(client, currentStack);
            TooltipItemStackCache.saveItemStack(ItemStack.EMPTY);

            if (tooltip.isEmpty()) return;
        } else {
            tooltip = Lists.newArrayList(TooltipHelper.getDisplayName(currentStack));
        }

        TextRenderer textRenderer = this.getTextRenderer();

        int currentSlot = client.player != null ? ((PlayerInventoryAccessor) client.player.getInventory()).getSelectedSlot() : enhancedTooltips$lastTiltSlot;
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

        Pair<String, Integer> badgeText = BadgesUtils.getBadgeText(currentStack);
        if (config.general.itemBadges && !badgeText.getLeft().isEmpty()) {
            Text name = tooltip.getFirst().copy()
                .append(Text.literal(" (").withColor(-4539718))
                .append(Text.translatable(badgeText.getLeft()).withColor(badgeText.getRight()))
                .append(Text.literal(")").withColor(-4539718));
            tooltip.set(0, name);
        }

        if (config.general.rarityTooltip)
            tooltip.add(1, TooltipHelper.getRarityName(currentStack));

        if (config.heldItemTooltip.mode == EnhancedTooltipsConfig.HeldItemTooltipMode.ON)
            enhancedTooltips$addFoodTooltip(tooltip::add);

        if (config.heldItemTooltip.mode == EnhancedTooltipsConfig.HeldItemTooltipMode.ON && client.options.advancedItemTooltips) {
            tooltip.remove(Text.translatable("item.durability", currentStack.getMaxDamage() - currentStack.getDamage(), currentStack.getMaxDamage()));
            tooltip.remove(Text.literal(Registries.ITEM.getId(currentStack.getItem()).toString()).formatted(Formatting.DARK_GRAY));
            tooltip.remove(Text.translatable("item.components", currentStack.getComponents().size()).formatted(Formatting.DARK_GRAY));
        }

        if ((!config.durability.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) || config.durability.durabilityBar) && currentStack.isDamageable())
            tooltip.add(Text.translatable("enhancedtooltips.tooltip.durability").append(enhancedTooltips$getDurabilityText()));

        int maxWidth = context.getScaledWindowWidth() / 2;
        for (Text component : new ArrayList<>(tooltip)) {
            if (textRenderer.getWidth(component) > maxWidth) {
                List<Text> wrapped = new ArrayList<>();
                textRenderer.wrapLines(component, maxWidth).forEach(line -> wrapped.add(EnhancedTooltipsTextVisitor.get(line)));
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

            return textRenderer.getWidth(text) == 0;
        });

        if (cutOff.get() > 0)
            tooltip.add(Text.literal("(+" + cutOff.get() + " more...)").withColor(-4539718).styled(s -> s.withItalic(true)));

        int width = tooltip.stream().mapToInt(textRenderer::getWidth).max().orElse(0);
        int x = (context.getScaledWindowWidth() - width) / 2;

        int y = context.getScaledWindowHeight() - 59;
        y -= (textRenderer.fontHeight + enhancedTooltips$SPACING / 2) * tooltip.size() - enhancedTooltips$SPACING * 3 + enhancedTooltips$SPACING / 2;
        if (client.player.getArmor() > 0 && client.interactionManager.hasStatusBars()) y -= enhancedTooltips$SPACING * 2;

        int alpha = (int) ((float) this.heldItemTooltipFade * 256.0F / 10.0F);
        if (alpha > 255) {
            alpha = 255;
        }

        context.getMatrices().push();
        enhancedTooltips$drawTextWithBackground(textRenderer, tooltip, x, y, width, context, alpha);
        context.getMatrices().pop();

        ci.cancel();
    }

    @Unique
    public void enhancedTooltips$addFoodTooltip(Consumer<Text> list) {
        FoodComponent foodComponent = enhancedTooltips$getFoodComponent();
        if (foodComponent == null) return;

        int hunger = enhancedTooltips$getHunger();
        int saturation = enhancedTooltips$getSaturation();

        Text hungerText = Text.translatable("enhancedtooltips.tooltip.hunger").append(" " + hunger + " ").append(Text.translatable("effect.minecraft.hunger"));
        Text saturationText = Text.translatable("enhancedtooltips.tooltip.saturation", saturation).withColor(0xff00ffff);

        if (EnhancedTooltipsConfig.load().foodAndDrinks.hungerTooltip) list.accept(hungerText);
        if (EnhancedTooltipsConfig.load().foodAndDrinks.saturationTooltip) list.accept(saturationText);

        //? if >1.21.1 {
        ConsumableComponent consumableComponent = enhancedTooltips$getConsumableComponent();
        if (consumableComponent == null) return;

        List<ConsumeEffect> effects = consumableComponent.onConsumeEffects();

        for (ConsumeEffect entry : effects) {
            if (EnhancedTooltipsConfig.load().foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.OFF) break;

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
                MutableText effectText = Text.literal("◈ ")
                        .append(Text.translatable(statusEffect.getTranslationKey()))
                        .append(" (").append(StatusEffectUtil.getDurationText(statusEffect, 1.0f, 20)).append(")")
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
    public FoodComponent enhancedTooltips$getFoodComponent() {
        FoodComponent foodComponent;

        //? if >1.21.1 {
        foodComponent = currentStack.getItem().getComponents().get(DataComponentTypes.FOOD);
        //?} else {
        /*foodComponent = currentStack.getItem().getComponents().get(DataComponentTypes.FOOD);
        *///?}

        return foodComponent;
    }

    //? if >1.21.1 {
    @Unique
    public ConsumableComponent enhancedTooltips$getConsumableComponent() {
        return currentStack.get(DataComponentTypes.CONSUMABLE);
    }
    //?}

    @Unique
    public int enhancedTooltips$getHunger() {
        FoodComponent foodComponent = enhancedTooltips$getFoodComponent();
        int hunger = 0;

        //? if >1.21.1 {
        ConsumableComponent consumableComponent = enhancedTooltips$getConsumableComponent();
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
        FoodComponent foodComponent = enhancedTooltips$getFoodComponent();
        int saturation = 0;
        int hunger = enhancedTooltips$getHunger();

        if (foodComponent != null) {
            saturation = (int) ((foodComponent.saturation() / (hunger * 2.0)) * 100);
        }

        return saturation;
    }

    @Unique
    private Text enhancedTooltips$getDurabilityText() {
        int remaining = currentStack.getMaxDamage() - currentStack.getDamage();
        return switch (EnhancedTooltipsConfig.load().durability.durabilityTooltip) {
            case VALUE -> Text.literal(" ")
                .append(Text.literal(String.valueOf(remaining)).setStyle(Style.EMPTY.withColor(currentStack.getItemBarColor())))
                .append(Text.literal(" / ").setStyle(Style.EMPTY.withColor(-4539718)))
                .append(Text.literal(String.valueOf(currentStack.getMaxDamage())).setStyle(Style.EMPTY.withColor(0xFF00FF00)));
            case PERCENTAGE -> {
                int percent = remaining * 100 / currentStack.getMaxDamage();
                yield Text.literal(" " + percent + "%").setStyle(Style.EMPTY.withColor(currentStack.getItemBarColor()));
            }
            default -> {
                int filledCount = (remaining * 10) / currentStack.getMaxDamage();
                MutableText durabilityBar = Text.literal(" ");

                for (int i = 0; i < filledCount; i++) {
                    durabilityBar.append(Text.literal("=").setStyle(Style.EMPTY.withColor(currentStack.getItemBarColor())));
                }

                for (int i = filledCount; i < 10; i++) {
                    durabilityBar.append(Text.literal("=").setStyle(Style.EMPTY.withColor(-4539718)));
                }

                yield durabilityBar;
            }
        };
    }

    @Unique
    private void enhancedTooltips$drawTextWithBackground(TextRenderer textRenderer, List<Text> lines, int x, int y, int width, DrawContext context, int alpha) {
        int bgAlpha = (0x80 * alpha) / 255 << 24;
        float tilt = enhancedTooltips$getTilt();

        context.getMatrices().translate(x + width / 2f, y + (textRenderer.fontHeight * lines.size()) / 2f, 0);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(tilt));
        context.getMatrices().translate(-(x + width / 2f), -(y + (textRenderer.fontHeight * lines.size()) / 2f), 0);

        if (EnhancedTooltipsConfig.load().heldItemTooltip.showBackground) context.fill(
            x - enhancedTooltips$SPACING + 1,
            y - enhancedTooltips$SPACING / 2,
            x + width + enhancedTooltips$SPACING - 1,
            y + (textRenderer.fontHeight + enhancedTooltips$SPACING / 2) * lines.size() - enhancedTooltips$SPACING / 2,
            bgAlpha
        );

        int textY = y;
        for (Text line : lines) {
            int color = (line.getStyle().getColor() != null ? line.getStyle().getColor().getRgb() : 0xFFFFFF) | (alpha << 24);
            context.drawText(textRenderer, line.copy().withColor(color), (context.getScaledWindowWidth() - textRenderer.getWidth(line)) / 2, textY, color, true);
            textY += textRenderer.fontHeight + enhancedTooltips$SPACING / 2;
        }

        int frameAlpha = (0x70 * alpha) / 255 << 24;
        if (EnhancedTooltipsConfig.load().heldItemTooltip.showBackground)
            BadgesUtils.drawFrame(context, x - enhancedTooltips$SPACING, y - enhancedTooltips$SPACING / 2, width + enhancedTooltips$SPACING * 2, (textRenderer.fontHeight + enhancedTooltips$SPACING / 2) * lines.size() + enhancedTooltips$SPACING / 2, 400, frameAlpha);
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
