//? if fabric {
/*package dev.ultimatchamp.enhancedtooltips.loaders;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.util.CreativeModeTabCollector;
import dev.ultimatchamp.enhancedtooltips.util.ItemGroupsUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class EnhancedTooltipsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ServerWorldEvents.LOAD.register((server, world) -> {
            ItemGroupsUtils.tabs.putAll(CreativeModeTabCollector.collectTabs(world));
        });

        EnhancedTooltips.init();
    }
}
*///?}
