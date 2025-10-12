package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.*;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.DecorationItemEntityTypeAccessor;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.OrderedTextTooltipComponentAccessor;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipComponentManager;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/*? if >1.21.4 {*/import net.minecraft.entity.EquipmentSlot;/*?}*/

public class EnhancedTooltips {
    public static final String MOD_ID = "enhancedtooltips";
    public static final String MOD_NAME = "EnhancedTooltips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        EnhancedTooltipsConfig.load();

        TooltipComponentManager.register((list, stack) -> {
            if (list.isEmpty()) return;

            List<TooltipComponent> copy = new ArrayList<>(list);
            List<TooltipComponent> advanced = new ArrayList<>();
            if (MinecraftClient.getInstance().options.advancedItemTooltips) {
                list.removeIf(component ->
                        component instanceof OrderedTextTooltipComponentAccessor c &&
                         (!EnhancedTooltipsConfig.load().durability.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) || EnhancedTooltipsConfig.load().durability.durabilityBar) &&
                         EnhancedTooltipsTextVisitor.get(c.getText()).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage()));
                copy.forEach(component -> {
                    if (!(component instanceof OrderedTextTooltipComponentAccessor c)) return;
                    if (
                            EnhancedTooltipsTextVisitor.get(c.getText()).getString().contains((stack.getMaxDamage() - stack.getDamage()) + " / " + stack.getMaxDamage()) ||
                            EnhancedTooltipsTextVisitor.get(c.getText()).getString().contains(Text.literal(Registries.ITEM.getId(stack.getItem()).toString()).formatted(Formatting.DARK_GRAY).getString()) ||
                            EnhancedTooltipsTextVisitor.get(c.getText()).getString().contains(Text.translatable("item.components", stack.getComponents().size()).formatted(Formatting.DARK_GRAY).getString())
                    ) {
                        advanced.add(component);
                        list.remove(component);
                    }
                });
            }

            if (stack.isEmpty()) {
                list.add(new TooltipBackgroundComponent());
                return;
            }

            list.removeFirst();
            list.addFirst(new HeaderTooltipComponent(stack));

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

            if (FoodTooltipComponent.getFoodComponent(stack) != null)
                list.add(1, new FoodTooltipComponent(stack));
            if (stack.get(DataComponentTypes.POTION_CONTENTS) != null &&
                EnhancedTooltipsConfig.load().foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS)
                list.add(1, new PotionEffectTooltipComponent(stack));

            //? if >1.21.4 {
            if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ||
                ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.ANIMAL_ARMOR ||
            //?} else {
            /*if (stack.getItem() instanceof ArmorItem ||
                  stack.getItem() instanceof AnimalArmorItem ||
            *///?}
                stack.getItem() instanceof EntityBucketItem ||
                stack.getItem() instanceof SpawnEggItem ||
                (stack.getItem() instanceof SmithingTemplateItem && Registries.ITEM.getId(stack.getItem()).toString().contains("armor_trim"))
            ) {
                list.add(new ModelViewerTooltipComponent(stack));
            } else {
                list.add(new TooltipBorderColorComponent(stack));
            }

            if (stack.getItem() instanceof FilledMapItem && EnhancedTooltipsConfig.load().mapTooltip.enabled)
                list.add(new MapTooltipComponent(stack));

            if (stack.getItem() instanceof DecorationItemEntityTypeAccessor decorationItem &&
                decorationItem.get() == EntityType.PAINTING &&
                EnhancedTooltipsConfig.load().paintingTooltip.enabled
            ) list.add(new PaintingTooltipComponent(stack));

            if (
                //? if >1.21.4 {
                stack.get(DataComponentTypes.PROVIDES_BANNER_PATTERNS) != null &&
                //?} else {
                /*stack.getItem() instanceof BannerPatternItem &&
                *///?}
                EnhancedTooltipsConfig.load().bannerPatternTooltip.enabled
            )
                list.add(new BannerPatternTooltipComponent(stack));

            //? if >1.21.4 {
            if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR &&
            //?} else {
            /*if (stack.getItem() instanceof ArmorItem &&
            *///?}
                EnhancedTooltipsConfig.load().armorIconTooltip.enabled
            )
                list.add(new ArmorTooltipComponent(stack));

            if (stack.isDamageable())
                list.add(new DurabilityTooltipComponent(stack));

            list.addAll(advanced);
        });
    }
}
