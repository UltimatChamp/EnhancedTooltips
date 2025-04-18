package dev.ultimatchamp.enhancedtooltips.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
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
    @Unique private long tiltStartTime = 0;
    @Unique private int lastTiltSlot = -1;
    @Unique private float tiltDirection = 0;
    @Unique private static final int SPACING = 4;

    @Shadow public abstract TextRenderer getTextRenderer();
    @Shadow private ItemStack currentStack;
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)I"), cancellable = true)
    private void enhancedTooltips$renderHeldItemTooltipBackground(DrawContext context, CallbackInfo ci, @Local(ordinal = 2) int y, @Local(ordinal = 3) int alpha) {
        EnhancedTooltipsConfig config = EnhancedTooltipsConfig.load();
        if (!config.heldItemTooltip.enabled) return;

        List<Text> tooltip = Screen.getTooltipFromItem(client, currentStack);
        TooltipItemStackCache.saveItemStack(ItemStack.EMPTY);

        if (tooltip.isEmpty()) return;

        TextRenderer textRenderer = this.getTextRenderer();

        int currentSlot = client.player != null ? client.player.getInventory().selectedSlot : lastTiltSlot;
        if (currentSlot != lastTiltSlot) {
            int delta = currentSlot - lastTiltSlot;
            if (lastTiltSlot == 8 && currentSlot == 0) {
                delta = 1;
            } else if (lastTiltSlot == 0 && currentSlot == 8) {
                delta = -1;
            }

            tiltDirection = Math.signum(delta);
            tiltStartTime = System.currentTimeMillis();
            lastTiltSlot = currentSlot;
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

        addFoodTooltip(tooltip::add);

        if (client.options.advancedItemTooltips) {
            tooltip.remove(Text.translatable("item.durability", currentStack.getMaxDamage() - currentStack.getDamage(), currentStack.getMaxDamage()));
            tooltip.remove(Text.literal(Registries.ITEM.getId(currentStack.getItem()).toString()).formatted(Formatting.DARK_GRAY));
            tooltip.remove(Text.translatable("item.components", currentStack.getComponents().size()).formatted(Formatting.DARK_GRAY));
        }

        if ((!config.durability.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) || config.durability.durabilityBar) && currentStack.isDamageable())
            tooltip.add(Text.translatable("enhancedtooltips.tooltip.durability").append(getDurabilityText()));

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

        y -= (textRenderer.fontHeight + SPACING / 2) * tooltip.size() - SPACING * 3 + SPACING / 2;
        if (client.player.getArmor() > 0 && client.interactionManager.hasStatusBars()) y -= SPACING * 2;

        context.getMatrices().push();
        drawTextWithBackground(textRenderer, tooltip, x, y, width, context, alpha);
        context.getMatrices().pop();

        ci.cancel();
    }

    @Unique
    public void addFoodTooltip(Consumer<Text> list) {
        FoodComponent foodComponent = getFoodComponent();
        if (foodComponent == null) return;

        int hunger = getHunger();
        int saturation = getSaturation();

        Text hungerText = Text.translatable("enhancedtooltips.tooltip.hunger").append(" " + hunger + " ").append(Text.translatable("effect.minecraft.hunger"));
        Text saturationText = Text.translatable("enhancedtooltips.tooltip.saturation", saturation).withColor(0xff00ffff);

        if (EnhancedTooltipsConfig.load().foodAndDrinks.hungerTooltip) list.accept(hungerText);
        if (EnhancedTooltipsConfig.load().foodAndDrinks.saturationTooltip) list.accept(saturationText);

        //? if >1.21.1 {
        ConsumableComponent consumableComponent = getConsumableComponent();
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
    public FoodComponent getFoodComponent() {
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
    public ConsumableComponent getConsumableComponent() {
        return currentStack.get(DataComponentTypes.CONSUMABLE);
    }
    //?}

    @Unique
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

    @Unique
    public int getSaturation() {
        FoodComponent foodComponent = getFoodComponent();
        int saturation = 0;
        int hunger = getHunger();

        if (foodComponent != null) {
            saturation = (int) ((foodComponent.saturation() / (hunger * 2.0)) * 100);
        }

        return saturation;
    }

    @Unique
    private Text getDurabilityText() {
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
    private void drawTextWithBackground(TextRenderer textRenderer, List<Text> lines, int x, int y, int width, DrawContext context, int alpha) {
        int bgAlpha = (0x80 * alpha) / 255 << 24;
        float tilt = getTilt();

        context.getMatrices().translate(x + width / 2f, y + (textRenderer.fontHeight * lines.size()) / 2f, 0);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(tilt));
        context.getMatrices().translate(-(x + width / 2f), -(y + (textRenderer.fontHeight * lines.size()) / 2f), 0);

        if (EnhancedTooltipsConfig.load().heldItemTooltip.showBackground) context.fill(
            x - SPACING + 1,
            y - SPACING / 2,
            x + width + SPACING - 1,
            y + (textRenderer.fontHeight + SPACING / 2) * lines.size() - SPACING / 2,
            bgAlpha
        );

        int textY = y;
        for (Text line : lines) {
            int color = (line.getStyle().getColor() != null ? line.getStyle().getColor().getRgb() : 0xFFFFFF) | (alpha << 24);
            context.drawText(textRenderer, line.copy().withColor(color), (context.getScaledWindowWidth() - textRenderer.getWidth(line)) / 2, textY, color, true);
            textY += textRenderer.fontHeight + SPACING / 2;
        }

        int frameAlpha = (0x70 * alpha) / 255 << 24;
        if (EnhancedTooltipsConfig.load().heldItemTooltip.showBackground)
            BadgesUtils.drawFrame(context, x - SPACING, y - SPACING / 2, width + SPACING * 2, (textRenderer.fontHeight + SPACING / 2) * lines.size() + SPACING / 2, 400, frameAlpha);
    }

    @Unique
    private float getTilt() {
        if (!EnhancedTooltipsConfig.load().heldItemTooltip.tiltAnimation || tiltDirection == 0f) return 0f;

        long elapsed = System.currentTimeMillis() - tiltStartTime;
        float duration = 300f;
        if (elapsed > duration) return 0f;

        float eased = (float) Math.pow(1f - (elapsed / duration), 2);
        return tiltDirection * 10f * eased;
    }
}
