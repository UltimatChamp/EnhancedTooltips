package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.*;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.TooltipModule;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipComparatorProvider;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipComponentAPI;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipDrawerProvider;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.SpawnEggItem;
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

        TooltipComponentAPI.EVENT.register((list, stack) -> {
            list.remove(0);
            list.add(0, new HeaderTooltipComponent(stack));

            list.add(1, new EffectsTooltipComponent(stack));

            int color = TooltipHelper.borderColorProvider.getItemBorderColor(stack);
            if (stack.getItem() instanceof ArmorItem || stack.getItem() instanceof EntityBucketItem || stack.getItem() instanceof SpawnEggItem) {
                list.add(new ModelViewerComponent(stack, 0xff000000 | color));
            } else {
                list.add(new ColorBorderComponent(0xff000000 | color));
            }

            if (MinecraftClient.getInstance().options.advancedItemTooltips) {
                for (TooltipComponent component : list) {
                    if (component instanceof OrderedTextTooltipComponent orderedTextTooltipComponent) {
                        if (EnhancedTooltipsTextVisitor.get(orderedTextTooltipComponent.text).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " +  stack.getMaxDamage())) {
                            list.remove(component);
                            break;
                        }
                    }
                }
                list.add(list.size() - 3, new DurabilityTooltipComponent(stack));
            } else {
                list.add(new DurabilityTooltipComponent(stack));
            }
        });

        TooltipDrawerProvider.setTooltipDrawerProvider(new EnhancedTooltipsDrawer());
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
