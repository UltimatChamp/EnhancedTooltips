package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.ColorBorderComponent;
import dev.ultimatchamp.enhancedtooltips.component.HeaderTooltipComponent;
import dev.ultimatchamp.enhancedtooltips.component.ModelViewerComponent;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.TooltipModule;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipComparatorProvider;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipComponentAPI;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipDrawerProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

//? if >1.21.1 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
//?} else if >1.20.4 {
/*import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
*///?} else {
/*import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
*///?}

public class EnhancedTooltips implements ClientModInitializer {
    public static final String MOD_ID = "enhancedtooltips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final String MARK_KEY = "kaleido_tooltip_mark";

    @Override
    public void onInitializeClient() {
        new TooltipModule().load();

        TooltipComparatorProvider.setComparator(Comparator.comparingInt(EnhancedTooltips::getSerialNumber));
        //? if >1.20.4 {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
        //?} else {
        /*ItemTooltipCallback.EVENT.register((stack, tooltipContext, lines) -> {
        *///?}
            // Special component
            //? if >1.21.1 {
            ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
            FoodComponent foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
            if (foodComponent != null && consumableComponent != null) {
                Text hungerText = Text.translatable(identifier("tooltip.hunger").toTranslationKey(), foodComponent.nutrition());
                Text saturationText = Text.translatable(identifier("tooltip.saturation").toTranslationKey(), (int) (foodComponent.saturation() * 100))
            //?} else if >1.20.4 {
            /*FoodComponent foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
            if (foodComponent != null) {
                Text hungerText = Text.translatable(identifier("tooltip.hunger").toTranslationKey(), foodComponent.nutrition());
                Text saturationText = Text.translatable(identifier("tooltip.saturation").toTranslationKey(), (int) (foodComponent.saturation() * 100))
            *///?} else {
            /*FoodComponent foodComponent = stack.getItem().getFoodComponent();
            if (foodComponent != null) {
                Text hungerText = Text.translatable(identifier("tooltip.hunger").toTranslationKey(), foodComponent.getHunger());
                Text saturationText = Text.translatable(identifier("tooltip.saturation").toTranslationKey(), (int) (foodComponent.getSaturationModifier() * 100))
            *///?}
                        .setStyle(Style.EMPTY.withFormatting(Formatting.AQUA)).setStyle(Style.EMPTY.withFormatting(Formatting.AQUA));
                lines.add(hungerText);
                lines.add(saturationText);

                //? if >1.21.1 {
                for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
                    if (!(entry instanceof ApplyEffectsConsumeEffect applyEffectsConsumeEffect)) {
                        continue;
                    }

                    for (StatusEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                        int c = statusEffect.getEffectType().value().getColor();
                //?} else if >1.20.4 {
                /*for (FoodComponent.StatusEffectEntry entry: foodComponent.effects()) {
                    var statusEffect = entry.effect();
                    int c = statusEffect.getEffectType().value().getColor();
                *///?} else {
                /*for (Pair<StatusEffectInstance, Float> effect : foodComponent.getStatusEffects()) {
                    var statusEffect = effect.getFirst();
                    int c = statusEffect.getEffectType().getColor();
                *///?}
                        if (!EnhancedTooltipsConfig.load().effectTooltip) break;

                        Text effectText = Text.empty().append("◈ ").append(Text.translatable(
                                        statusEffect.getTranslationKey()))
                                //? if >1.20.4 {
                                .append(" (").append(StatusEffectUtil.getDurationText(statusEffect, 1.0f, tooltipContext.getUpdateTickRate())).append(")")
                                //?} else if >1.20.2 {
                                /*.append(" (").append(StatusEffectUtil.getDurationText(statusEffect, 1.0f, 20)).append(")")
                                *///?} else {
                                /*.append(" (").append(StatusEffectUtil.getDurationText(statusEffect, 1.0f)).append(")")
                                *///?}
                                .setStyle(Style.EMPTY.withColor(c));
                        lines.add(effectText);
                    }
                }
            //? if >1.21.1 {
            }
            //?}

            if (stack.isDamageable()) {
                var damaged = stack.getMaxDamage() - stack.getDamage();
                Text durabilityText = Text.empty();

                if (EnhancedTooltipsConfig.load().durabilityTooltip == EnhancedTooltipsConfig.DurabilityTooltipMode.VALUE) {
                    durabilityText = Text.translatable("enhancedtooltips.tooltip.durability")
                            .append(Text.literal(" " + damaged + " / " + stack.getMaxDamage())
                                    .setStyle(Style.EMPTY.withColor(stack.getItemBarColor()))
                            );
                } else if (EnhancedTooltipsConfig.load().durabilityTooltip == EnhancedTooltipsConfig.DurabilityTooltipMode.PERCENTAGE) {
                    durabilityText = Text.translatable("enhancedtooltips.tooltip.durability")
                            .append(Text.literal(" " + damaged * 100 / stack.getMaxDamage() + "%")
                                    .setStyle(Style.EMPTY.withColor(stack.getItemBarColor()))
                            );
                }

                if (!durabilityText.equals(Text.empty())) lines.add(durabilityText);
            }
        });

        TooltipComponentAPI.EVENT.register((list, itemStack) -> {
            list.remove(0);
            list.add(0, new HeaderTooltipComponent(itemStack));
            // Background component
            int color = TooltipHelper.borderColorProvider.getItemBorderColor(itemStack);
            if (itemStack.getItem() instanceof ArmorItem) {
                list.add(new ModelViewerComponent(itemStack, 0xff000000 | color));
            } else if (itemStack.getItem() instanceof EntityBucketItem) {
                list.add(new ModelViewerComponent(itemStack, 0xff000000 | color));
            } else if (itemStack.getItem() instanceof SpawnEggItem) {
                list.add(new ModelViewerComponent(itemStack, 0xff000000 | color));
            } else {
                list.add(new ColorBorderComponent(0xff000000 | color));
            }
        });

        TooltipDrawerProvider.setTooltipDrawerProvider(new EnhancedTooltipsDrawer());

        ResourceManagerHelper resourceManagerHelper = ResourceManagerHelper.get(ResourceType.SERVER_DATA);
        resourceManagerHelper.registerReloadListener(BorderColorLoader.INSTANCE);
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }

    private static int getSerialNumber(TooltipComponent component) {
        if (component != null) {
            return 0;
        } else {
            return 1;
        }
    }
}
