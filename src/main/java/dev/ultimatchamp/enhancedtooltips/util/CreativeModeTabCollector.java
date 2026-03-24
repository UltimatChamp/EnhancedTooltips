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
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.CreativeModeTabAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

//? if fabric {
//? if >1.21.11 {
import net.fabricmc.fabric.api.creativetab.v1.*;
//?} else {
/*import net.fabricmc.fabric.api.itemgroup.v1.*;
*///?}
//?} else {
/*import net.neoforged.neoforge.event.EventHooks;
*///?}

public class CreativeModeTabCollector {
    public static Map<CreativeModeTab, Collection<ItemStack>> collectTabs(@NotNull Level world) {
        Map<CreativeModeTab, Collection<ItemStack>> map = new LinkedHashMap<>();
        FeatureFlagSet featureFlags = FeatureFlags.REGISTRY.allFlags();
        CreativeModeTab.ItemDisplayParameters parameters = new CreativeModeTab.ItemDisplayParameters(featureFlags, true, world.registryAccess());

        for (CreativeModeTab group : CreativeModeTabs.allTabs()) {
            if (group.getType() != CreativeModeTab.Type.HOTBAR && group.getType() != CreativeModeTab.Type.INVENTORY) {
                try {
                    CreativeModeTab.ItemDisplayBuilder builder = new CreativeModeTab.ItemDisplayBuilder(group, featureFlags);
                    ResourceKey<@NotNull CreativeModeTab> resourceKey = BuiltInRegistries.CREATIVE_MODE_TAB
                            .getResourceKey(group)
                            .orElseThrow(() -> new IllegalStateException("Unregistered creative tab: " + group));

                    //? if fabric {
                    ((CreativeModeTabAccessor) group).getDisplayItemsGenerator().accept(parameters, builder);
                    map.put(group, postFabricEvents(group, parameters, resourceKey, builder.tabContents));
                    //?} else {
                    /*EventHooks.onCreativeModeTabBuildContents(group, resourceKey, ((CreativeModeTabAccessor) group).getDisplayItemsGenerator(), parameters, (stack, visibility) -> {
                        if (visibility == CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY) return;
                        builder.accept(stack, visibility);
                    });
                    map.put(group, builder.tabContents);
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
    private static Collection<ItemStack> postFabricEvents(CreativeModeTab group, CreativeModeTab.ItemDisplayParameters context, ResourceKey<@NotNull CreativeModeTab> resourceKey, Collection<ItemStack> tabContents) {
        try {
            // Sorry!
            var entries =
                    //? if >1.21.11 {
                    new FabricCreativeModeTabOutput(
                    //?} else {
                    /*new FabricItemGroupEntries(
                    *///?}
                            context, new LinkedList<>(tabContents), new LinkedList<>());
            //? if >1.21.11 {
            CreativeModeTabEvents.modifyOutputEvent(resourceKey).invoker().modifyOutput(entries);
            CreativeModeTabEvents.MODIFY_OUTPUT_ALL.invoker().modifyOutput(group, entries);
            //?} else {
            /*ItemGroupEvents.modifyEntriesEvent(resourceKey).invoker().modifyEntries(entries);
            ItemGroupEvents.MODIFY_ENTRIES_ALL.invoker().modifyEntries(group, entries);
            *///?}
            return entries.getDisplayStacks();
        } catch (Throwable throwable) {
            EnhancedTooltips.LOGGER.error("Failed to collect fabric's creative group: {}", group, throwable);
            return tabContents;
        }
    }
    //?}
}
