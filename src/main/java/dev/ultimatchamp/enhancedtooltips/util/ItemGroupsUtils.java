package dev.ultimatchamp.enhancedtooltips.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ItemGroupsUtils {
    private static final Map<ResourceKey<@NotNull CreativeModeTab>, Integer> VANILLA_GROUP_COLORS = new LinkedHashMap<>();
    public static Map<CreativeModeTab, Collection<ItemStack>> tabs = new LinkedHashMap<>();
    public static final List<String> ITEM_GROUP_KEYS = List.of(
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

    static {
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.COMBAT, 0xfff94144);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.TOOLS_AND_UTILITIES, 0xff9b59b6);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.SPAWN_EGGS, 0xffa29bfe);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.OP_BLOCKS, 0xff9c89b8);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.FOOD_AND_DRINKS, 0xff61b748);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.REDSTONE_BLOCKS, 0xffff6b6b);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.INGREDIENTS, 0xffff6347);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.COLORED_BLOCKS, 0xff42a5f5);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.FUNCTIONAL_BLOCKS, 0xff2a9d8f);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.NATURAL_BLOCKS, 0xff66bb6a);
        VANILLA_GROUP_COLORS.put(CreativeModeTabs.BUILDING_BLOCKS, 0xfff2c94c);
    }

    public static @NotNull Map<Collection<Item>, Tuple<@NotNull Component, @NotNull Integer>> getItemGroups() {
        Map<Collection<Item>, Tuple<@NotNull Component, @NotNull Integer>> resultGroups = new LinkedHashMap<>();
        Map<CreativeModeTab, Collection<ItemStack>> collectedGroups = ItemGroupsUtils.tabs;

        for (Map.Entry<CreativeModeTab, Collection<ItemStack>> entry : collectedGroups.entrySet()) {
            CreativeModeTab group = entry.getKey();
            int fillColor;
            Component text;

            Optional<ResourceKey<@NotNull CreativeModeTab>> groupKeyOpt = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group);

            if (groupKeyOpt.isPresent() && VANILLA_GROUP_COLORS.containsKey(groupKeyOpt.get())) {
                text = group.getDisplayName();
                fillColor = VANILLA_GROUP_COLORS.get(groupKeyOpt.get());
            } else {
                ResourceLocation groupId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(group);
                if (groupId != null) {
                    String namespace = groupId.getNamespace();
                    text = Component.literal(BadgesUtils.getMods().getOrDefault(namespace, ""));
                    fillColor = BadgesUtils.getColorFromModName(namespace);
                } else {
                    text = group.getDisplayName();
                    fillColor = BadgesUtils.getColorFromModName(text.getString());
                }
            }

            Collection<Item> items = entry.getValue().stream()
                    .map(ItemStack::getItem)
                    .collect(Collectors.toList());

            resultGroups.put(items, new Tuple<>(text, fillColor));
        }
        return resultGroups;
    }
}
