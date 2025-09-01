/*
 * This file is licensed under the MIT License, part of Roughly Enough Items.
 * Copyright (c) 2018, 2019, 2020, 2021, 2022, 2023 shedaniel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.ultimatchamp.enhancedtooltips.util;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.ClientWorldAccessor;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.ItemGroupAccessor;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;

//? if fabric {
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
//?} else {
/*import net.neoforged.neoforge.event.EventHooks;
*///?}

public class CreativeModeTabCollector {
    public static Map<ItemGroup, Collection<ItemStack>> collectTabs(@NotNull ClientWorld world) {
        Map<ItemGroup, Collection<ItemStack>> map = new LinkedHashMap<>();
        FeatureSet featureFlags = FeatureFlags.FEATURE_MANAGER.getFeatureSet();
        ItemGroup.DisplayContext parameters = new ItemGroup.DisplayContext(featureFlags, true, ((ClientWorldAccessor) world).getNetworkHandler().getRegistryManager());

        for (ItemGroup group : ItemGroups.getGroups()) {
            if (group.getType() != ItemGroup.Type.HOTBAR && group.getType() != ItemGroup.Type.INVENTORY) {
                try {
                    ItemGroup.EntriesImpl builder = new ItemGroup.EntriesImpl(group, featureFlags);
                    RegistryKey<ItemGroup> resourceKey = Registries.ITEM_GROUP
                            .getKey(group)
                            .orElseThrow(() -> new IllegalStateException("Unregistered creative tab: " + group));

                    //? if fabric {
                    ((ItemGroupAccessor) group).getEntryCollector().accept(parameters, builder);
                    map.put(group, postFabricEvents(group, parameters, resourceKey, builder.parentTabStacks));
                    //?} else {
                    /*EventHooks.onCreativeModeTabBuildContents(group, resourceKey, ((ItemGroupAccessor) group).getEntryCollector(), parameters, (stack, visibility) -> {
                        if (visibility == ItemGroup.StackVisibility.SEARCH_TAB_ONLY) return;
                        builder.add(stack, visibility);
                    });
                    map.put(group, builder.parentTabStacks);
                    *///?}
                } catch (Throwable throwable) {
                    EnhancedTooltips.LOGGER.error("Failed to collect creative tab: {}", group, throwable);
                }
            }
        }

        return map;
    }

    //? if fabric {
    @SuppressWarnings("UnstableApiUsage")
    private static Collection<ItemStack> postFabricEvents(ItemGroup group, ItemGroup.DisplayContext context, RegistryKey<ItemGroup> resourceKey, Collection<ItemStack> tabContents) {
        try {
            // Sorry!
            FabricItemGroupEntries entries = new FabricItemGroupEntries(context, new LinkedList<>(tabContents), new LinkedList<>());
            ItemGroupEvents.modifyEntriesEvent(resourceKey).invoker().modifyEntries(entries);
            ItemGroupEvents.MODIFY_ENTRIES_ALL.invoker().modifyEntries(group, entries);
            return entries.getDisplayStacks();
        } catch (Throwable throwable) {
            EnhancedTooltips.LOGGER.error("Failed to collect fabric's creative group: {}", group, throwable);
            return tabContents;
        }
    }
    //?}
}
