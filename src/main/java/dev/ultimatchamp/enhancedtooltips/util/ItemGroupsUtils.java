package dev.ultimatchamp.enhancedtooltips.util;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemGroupsUtils {
    private static final Map<ResourceKey<@NotNull CreativeModeTab>, Integer> VANILLA_GROUP_COLORS = new LinkedHashMap<>();
    public static Map<CreativeModeTab, Collection<ItemStack>> tabs = new LinkedHashMap<>();
    private static Map<Item, Pair<@NotNull Component, @NotNull Integer>> itemBadgeCache = null;
    private static Set<String> itemGroupNameCache = null;
    private static Language itemGroupNameLanguage = null;
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

    public static void populateTabs(@NotNull Level world) {
        Map<CreativeModeTab, Collection<ItemStack>> merged = new LinkedHashMap<>(CreativeModeTabCollector.collectTabs(world));
        tabs.forEach(merged::putIfAbsent);

        tabs = merged;
        itemBadgeCache = null;
    }

    public static @NotNull Set<String> getItemGroupNames() {
        Language language = Language.getInstance();
        Set<String> cache = itemGroupNameCache;

        if (cache == null || itemGroupNameLanguage != language) {
            cache = new HashSet<>(ITEM_GROUP_KEYS.size());
            for (String key : ITEM_GROUP_KEYS) {
                cache.add(Component.translatable(key).getString());
            }

            itemGroupNameCache = cache;
            itemGroupNameLanguage = language;
        }

        return cache;
    }

    public static @Nullable Pair<@NotNull Component, @NotNull Integer> getItemBadge(Item item) {
        Map<Item, Pair<@NotNull Component, @NotNull Integer>> cache = itemBadgeCache;
        if (cache == null) {
            cache = buildItemBadgeCache();
            itemBadgeCache = cache;
        }
        return cache.get(item);
    }

    private static @NotNull Map<Item, Pair<@NotNull Component, @NotNull Integer>> buildItemBadgeCache() {
        Map<Item, Pair<@NotNull Component, @NotNull Integer>> cache = new HashMap<>();

        for (Map.Entry<CreativeModeTab, Collection<ItemStack>> entry : tabs.entrySet()) {
            CreativeModeTab group = entry.getKey();
            int fillColor;
            Component text;

            Optional<ResourceKey<@NotNull CreativeModeTab>> groupKeyOpt = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(group);

            if (groupKeyOpt.isPresent() && VANILLA_GROUP_COLORS.containsKey(groupKeyOpt.get())) {
                text = group.getDisplayName();
                fillColor = VANILLA_GROUP_COLORS.get(groupKeyOpt.get());
            } else {
                Identifier groupId = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(group);
                if (groupId != null) {
                    String namespace = groupId.getNamespace();
                    text = Component.literal(BadgesUtils.getMods().getOrDefault(namespace, ""));
                    fillColor = BadgesUtils.getColorFromModName(namespace);
                } else {
                    text = group.getDisplayName();
                    fillColor = BadgesUtils.getColorFromModName(text.getString());
                }
            }

            Pair<@NotNull Component, @NotNull Integer> badge = Pair.of(text, fillColor);
            for (ItemStack stack : entry.getValue()) {
                cache.putIfAbsent(stack.getItem(), badge);
            }
        }

        return cache;
    }
}
