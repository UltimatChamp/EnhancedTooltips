package dev.ultimatchamp.enhancedtooltips.util;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemGroupsUtils {
    private static final Map<RegistryKey<ItemGroup>, Integer> VANILLA_GROUP_COLORS = new LinkedHashMap<>();
    public static Map<ItemGroup, Collection<ItemStack>> tabs = new LinkedHashMap<>();

    static {
        VANILLA_GROUP_COLORS.put(ItemGroups.COMBAT, 0xfff94144);
        VANILLA_GROUP_COLORS.put(ItemGroups.TOOLS, 0xff9b59b6);
        VANILLA_GROUP_COLORS.put(ItemGroups.SPAWN_EGGS, 0xffa29bfe);
        VANILLA_GROUP_COLORS.put(ItemGroups.OPERATOR, 0xff9c89b8);
        VANILLA_GROUP_COLORS.put(ItemGroups.FOOD_AND_DRINK, 0xff61b748);
        VANILLA_GROUP_COLORS.put(ItemGroups.REDSTONE, 0xffff6b6b);
        VANILLA_GROUP_COLORS.put(ItemGroups.INGREDIENTS, 0xffff6347);
        VANILLA_GROUP_COLORS.put(ItemGroups.COLORED_BLOCKS, 0xff42a5f5);
        VANILLA_GROUP_COLORS.put(ItemGroups.FUNCTIONAL, 0xff2a9d8f);
        VANILLA_GROUP_COLORS.put(ItemGroups.NATURAL, 0xff66bb6a);
        VANILLA_GROUP_COLORS.put(ItemGroups.BUILDING_BLOCKS, 0xfff2c94c);
    }

    public static @NotNull Map<Collection<Item>, Pair<Text, Integer>> getItemGroups() {
        Map<Collection<Item>, Pair<Text, Integer>> resultGroups = new LinkedHashMap<>();
        Map<ItemGroup, Collection<ItemStack>> collectedGroups = ItemGroupsUtils.tabs;

        for (Map.Entry<ItemGroup, Collection<ItemStack>> entry : collectedGroups.entrySet()) {
            ItemGroup group = entry.getKey();
            int fillColor;
            Text text;

            Optional<RegistryKey<ItemGroup>> groupKeyOpt = Registries.ITEM_GROUP.getKey(group);

            if (groupKeyOpt.isPresent() && VANILLA_GROUP_COLORS.containsKey(groupKeyOpt.get())) {
                text = group.getDisplayName();
                fillColor = VANILLA_GROUP_COLORS.get(groupKeyOpt.get());
            } else {
                Identifier groupId = Registries.ITEM_GROUP.getId(group);
                if (groupId != null) {
                    String namespace = groupId.getNamespace();
                    text = Text.literal(BadgesUtils.getMods().getOrDefault(namespace, ""));
                    fillColor = BadgesUtils.getColorFromModName(namespace);
                } else {
                    text = group.getDisplayName();
                    fillColor = BadgesUtils.getColorFromModName(text.getString());
                }
            }

            Collection<Item> items = entry.getValue().stream()
                    .map(ItemStack::getItem)
                    .collect(Collectors.toList());

            resultGroups.put(items, new Pair<>(text, fillColor));
        }
        return resultGroups;
    }
}
