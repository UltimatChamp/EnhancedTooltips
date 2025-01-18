package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.ColorBorderComponent;
import dev.ultimatchamp.enhancedtooltips.component.EffectsTooltipComponent;
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
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

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
            if (stack.isDamageable() && !tooltipType.isAdvanced()) {
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
            list.add(1, new EffectsTooltipComponent(itemStack));
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
