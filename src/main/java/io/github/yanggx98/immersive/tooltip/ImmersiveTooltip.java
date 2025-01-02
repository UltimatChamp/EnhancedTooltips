package io.github.yanggx98.immersive.tooltip;

import io.github.yanggx98.immersive.tooltip.component.ColorBorderComponent;
import io.github.yanggx98.immersive.tooltip.component.HeaderTooltipComponent;
import io.github.yanggx98.immersive.tooltip.component.ModelViewerComponent;
import io.github.yanggx98.immersive.tooltip.config.ConfigUtils;
import io.github.yanggx98.kaleido.render.tooltip.api.TooltipComparatorProvider;
import io.github.yanggx98.kaleido.render.tooltip.api.TooltipComponentAPI;
import io.github.yanggx98.kaleido.render.tooltip.api.TooltipDrawerProvider;
import me.grison.jtoml.impl.Toml;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ImmersiveTooltip implements ClientModInitializer {
    public static final String MOD_ID = "immersive-tooltip";
    public static boolean isRenderingArmorModel = true;

    @Override
    public void onInitializeClient() {
        TooltipComparatorProvider.setComparator(Comparator.comparingInt(ImmersiveTooltip::getSerialNumber));
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            // Special component
            FoodComponent foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
            ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
            if (foodComponent != null && consumableComponent != null) {
                Text hungerText = Text.translatable(identifier("tooltip.hunger").toTranslationKey(), foodComponent.nutrition());
                Text saturationText = Text.translatable(identifier("tooltip.saturation").toTranslationKey(), (int) (foodComponent.saturation() * 100))
                        .setStyle(Style.EMPTY.withFormatting(Formatting.AQUA)).setStyle(Style.EMPTY.withFormatting(Formatting.AQUA));
                lines.add(hungerText);
                lines.add(saturationText);
                for (ConsumeEffect entry : consumableComponent.onConsumeEffects()) {
                    if (!(entry instanceof ApplyEffectsConsumeEffect applyEffectsConsumeEffect)) {
                        continue;
                    }

                    for (StatusEffectInstance statusEffect : applyEffectsConsumeEffect.effects()) {
                        int c = statusEffect.getEffectType().value().getColor();
                        Text effectText = Text.empty().append("◈ ").append(Text.translatable(
                                        statusEffect.getTranslationKey()))
                                .append(" (").append(StatusEffectUtil.getDurationText(statusEffect, 1.0f, tooltipContext.getUpdateTickRate())).append(")")
                                .setStyle(Style.EMPTY.withColor(c));
                        lines.add(effectText);
                    }
                }
            }
            if (stack.isDamageable()) {
                Text durabilityText = Text.translatable("item.durability",
                        stack.getMaxDamage() - stack.getDamage(), stack.getMaxDamage());
                lines.add(durabilityText);
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
            } else {
                list.add(new ColorBorderComponent(0xff000000 | color));
            }

        });
        TooltipDrawerProvider.setTooltipDrawerProvider(new ImmersiveTooltipDrawer());

        ResourceManagerHelper resourceManagerHelper = ResourceManagerHelper.get(ResourceType.SERVER_DATA);
        resourceManagerHelper.registerReloadListener(BorderColorLoader.INSTANCE);

        try {
            Toml toml = ConfigUtils.initConfiguration(MOD_ID + "-client", (file, t) -> {
                Map<String, Boolean> map = new HashMap<>();
                map.put("enableRenderingArmorModel", true);
                String configStr = Toml.serialize("Rendering", map);
                ConfigUtils.write(file, configStr);
            });
            Map<String, Object> options = toml.getMap("Rendering");
            if (options != null) {
                isRenderingArmorModel = (Boolean) options.getOrDefault("enableRenderingArmorModel", true);
            } else {
                isRenderingArmorModel = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }


    private static int getSerialNumber(TooltipComponent component) {
        if (component instanceof TooltipComponent) {
            return 0;
        } else {
            return 1;
        }
    }
}
