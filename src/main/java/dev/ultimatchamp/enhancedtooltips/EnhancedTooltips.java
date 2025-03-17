package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.*;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.api.TooltipComponentAPI;
import dev.ultimatchamp.enhancedtooltips.api.TooltipDrawerProvider;
import dev.ultimatchamp.enhancedtooltips.tooltip.EnhancedTooltipsDrawer;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipModule;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
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
            if (list.isEmpty()) return;

            list.removeFirst();
            list.add(0, new HeaderTooltipComponent(stack));

            list.add(1, new FoodTooltipComponent(stack));

            //? if >1.21.4 {
            if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ||
            //?} else {
            /*if (stack.getItem() instanceof ArmorItem ||
            *///?}
                stack.getItem() instanceof EntityBucketItem ||
                stack.getItem() instanceof SpawnEggItem
            ) {
                list.add(new ModelViewerTooltipComponent(stack));
            } else {
                list.add(new TooltipBorderColorComponent(stack));
            }

            if (stack.getItem() instanceof FilledMapItem) list.add(new MapTooltipComponent(stack));

            if (stack.getItem() instanceof DecorationItem decorationItem && decorationItem.entityType == EntityType.PAINTING) list.add(new PaintingTooltipComponent(stack));

            if (MinecraftClient.getInstance().options.advancedItemTooltips) {
                list.removeIf(component ->
                        component instanceof OrderedTextTooltipComponent orderedTextTooltipComponent &&
                        (!EnhancedTooltipsConfig.load().durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) || EnhancedTooltipsConfig.load().durabilityBar) &&
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
