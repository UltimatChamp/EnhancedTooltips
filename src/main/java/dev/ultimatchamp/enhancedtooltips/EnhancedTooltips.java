package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.*;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.TooltipModule;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipComponentAPI;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipDrawerProvider;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnhancedTooltips implements ClientModInitializer {
    public static final String MOD_ID = "enhancedtooltips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        new TooltipModule().load();

        TooltipComponentAPI.EVENT.register((list, stack) -> {
            list.remove(0);
            list.add(0, new HeaderTooltipComponent(stack));

            list.add(1, new EffectsTooltipComponent(stack));

            if (stack.getItem() instanceof ArmorItem || stack.getItem() instanceof EntityBucketItem || stack.getItem() instanceof SpawnEggItem) {
                list.add(new ModelViewerComponent(stack));
            } else {
                list.add(new ColorBorderComponent(stack));
            }

            if (MinecraftClient.getInstance().options.advancedItemTooltips) {
                list.removeIf(component ->
                        component instanceof OrderedTextTooltipComponent orderedTextTooltipComponent &&
                        EnhancedTooltipsTextVisitor.get(orderedTextTooltipComponent.text).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage()));
            }
            list.add(new DurabilityTooltipComponent(stack));
        });

        TooltipDrawerProvider.setTooltipDrawerProvider(new EnhancedTooltipsDrawer());
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
