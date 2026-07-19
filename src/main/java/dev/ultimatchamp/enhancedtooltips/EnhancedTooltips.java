//~ entity_types
package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.compat.EMFCompat;
import dev.ultimatchamp.enhancedtooltips.compat.SophisticatedBackpacksCompat;
import dev.ultimatchamp.enhancedtooltips.component.*;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.HangingEntityItemTypeAccessor;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.ClientTextTooltipAccessor;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipComponentManager;
import dev.ultimatchamp.enhancedtooltips.util.BadgesUtils;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import dev.ultimatchamp.enhancedtooltips.util.ItemGroupsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*? if >1.21.4 {*/import net.minecraft.world.entity.EquipmentSlot;/*?}*/

public class EnhancedTooltips {
    public static final String MOD_ID = "enhancedtooltips";
    public static final String MOD_NAME = "EnhancedTooltips";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        EnhancedTooltipsConfig.load();

        if (BadgesUtils.getMods().containsKey("entity_model_features"))
            EMFCompat.registerVanillaModelCondition();

        TooltipComponentManager.register((list, stack) -> {
            if (list.isEmpty()) return;

            List<ClientTooltipComponent> advanced = new ArrayList<>();
            if (Minecraft.getInstance().options.advancedItemTooltips) {
                String damageString = (stack.getMaxDamage() - stack.getDamageValue()) + " / " + stack.getMaxDamage();
                String idString = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
                String componentsString = Component.translatable("item.components", stack.getComponents().size()).getString();
                boolean durabilityShown = !EnhancedTooltipsConfig.load().durability.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) || EnhancedTooltipsConfig.load().durability.durabilityBar;

                list.removeIf(component -> {
                    if (!(component instanceof ClientTextTooltipAccessor c)) return false;
                    String componentText = EnhancedTooltipsTextVisitor.getString(c.getText());

                    if (componentText.contains(damageString)) {
                        if (!durabilityShown) advanced.add(component);
                        return true;
                    }

                    if (componentText.contains(idString) || componentText.contains(componentsString)) {
                        advanced.add(component);
                        return true;
                    }

                    return false;
                });
            }

            if (stack.isEmpty()) {
                list.add(new TooltipBackgroundComponent());
                return;
            }

            boolean keepVanillaName = BadgesUtils.getMods().containsKey("sophisticatedcore")
                    && SophisticatedBackpacksCompat.containsBackpackTooltip(list);
            if (!keepVanillaName) {
                String hoverName = stack.getHoverName().getString();
                list.removeIf(c -> c instanceof ClientTextTooltipAccessor component &&
                        EnhancedTooltipsTextVisitor.getString(component.getText()).equals(hoverName));
            }
            list.addFirst(new HeaderTooltipComponent(stack));

            if (EnhancedTooltipsConfig.load().general.itemBadges) {
                Set<String> groupNames = ItemGroupsUtils.getItemGroupNames();
                list.removeIf(component -> component instanceof ClientTextTooltipAccessor textComponent &&
                        groupNames.contains(EnhancedTooltipsTextVisitor.getString(textComponent.getText())));
            }

            if (FoodTooltipComponent.getFoodComponent(stack) != null)
                list.add(1, new FoodTooltipComponent(stack));
            if (stack.get(DataComponents.POTION_CONTENTS) != null &&
                EnhancedTooltipsConfig.load().foodAndDrinks.effectsTooltip == EnhancedTooltipsConfig.EffectsTooltipMode.WITH_ICONS)
                list.add(1, new PotionEffectTooltipComponent(stack));

            //? if >1.21.4 {
            if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR ||
                ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.ANIMAL_ARMOR ||
            //?} else {
            /*if (stack.getItem() instanceof ArmorItem ||
                  stack.getItem() instanceof AnimalArmorItem ||
            *///?}
                    stack.getItem() instanceof MobBucketItem ||
                    stack.getItem() instanceof SpawnEggItem ||
                    (stack.getItem() instanceof SmithingTemplateItem && stack.getItem().toString().contains("armor_trim"))
            ) {
                list.add(new ModelViewerTooltipComponent(stack));
            } else {
                list.add(new TooltipBorderColorComponent(stack));
            }

            if (stack.getItem() instanceof MapItem && EnhancedTooltipsConfig.load().mapTooltip.enabled)
                list.add(new MapTooltipComponent(stack));

            if (stack.getItem() instanceof HangingEntityItemTypeAccessor decorationItem &&
                decorationItem.get() == EntityTypes.PAINTING &&
                EnhancedTooltipsConfig.load().paintingTooltip.enabled
            ) list.add(new PaintingTooltipComponent(stack));

            if (
                //? if >1.21.4 {
                stack.get(DataComponents.PROVIDES_BANNER_PATTERNS) != null &&
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

            if (stack.isDamageableItem())
                list.add(new DurabilityTooltipComponent(stack));

            list.addAll(advanced);
        });
    }
}
