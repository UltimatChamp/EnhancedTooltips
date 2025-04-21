package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.*;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.DecorationItemEntityTypeAccessor;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.OrderedTextTooltipComponentAccessor;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipComponentManager;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*? if >1.21.4 {*/import net.minecraft.entity.EquipmentSlot;/*?}*/

import java.util.List;

public class EnhancedTooltips {
    public static final String MOD_ID = "enhancedtooltips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        EnhancedTooltipsConfig.load();

        TooltipComponentManager.register((list, stack) -> {
            if (list.isEmpty()) return;

            if (stack.isEmpty()) {
                list.add(new TooltipBackgroundComponent());
                return;
            }

            list.removeFirst();
            list.add(0, new HeaderTooltipComponent(stack));

            list.add(1, new FoodTooltipComponent(stack));

            if (EnhancedTooltipsConfig.load().general.itemBadges) {
                List<String> itemGroups = List.of(
                        "itemGroup.combat",
                        "itemGroup.tools",
                        "itemGroup.spawnEggs",
                        "itemGroup.op",
                        "itemGroup.foodAndDrink",
                        "itemGroup.redstone",
                        "itemGroup.ingredients",
                        "itemGroup.coloredBlocks",
                        "itemGroup.functional",
                        "itemGroup.natural",
                        "itemGroup.buildingBlocks"
                );

                list.removeIf(component -> {
                    if (component instanceof OrderedTextTooltipComponentAccessor textComponent) {
                        Text text = EnhancedTooltipsTextVisitor.get(textComponent.getText());
                        for (String group : itemGroups) {
                            if (text.getString().equals(Text.translatable(group).getString())) {
                                return true;
                            }
                        }
                    }

                    return false;
                });
            }

            //? if >1.21.4 {
            if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ||
                ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.ANIMAL_ARMOR ||
            //?} else {
            /*if (stack.getItem() instanceof ArmorItem ||
                  stack.getItem() instanceof AnimalArmorItem ||
            *///?}
                stack.getItem() instanceof EntityBucketItem ||
                stack.getItem() instanceof SpawnEggItem
            ) {
                list.add(new ModelViewerTooltipComponent(stack));
            } else {
                list.add(new TooltipBorderColorComponent(stack));
            }

            if (stack.getItem() instanceof FilledMapItem) list.add(new MapTooltipComponent(stack));

            if (stack.getItem() instanceof DecorationItemEntityTypeAccessor decorationItem &&
                decorationItem.get() == EntityType.PAINTING
            ) list.add(new PaintingTooltipComponent(stack));

            if (MinecraftClient.getInstance().options.advancedItemTooltips) {
                list.removeIf(component ->
                        component instanceof OrderedTextTooltipComponentAccessor orderedTextTooltipComponent &&
                        (!EnhancedTooltipsConfig.load().durability.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) || EnhancedTooltipsConfig.load().durability.durabilityBar) &&
                        EnhancedTooltipsTextVisitor.get(orderedTextTooltipComponent.getText()).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage()));
            }
            list.add(new DurabilityTooltipComponent(stack));
        });
    }
}
