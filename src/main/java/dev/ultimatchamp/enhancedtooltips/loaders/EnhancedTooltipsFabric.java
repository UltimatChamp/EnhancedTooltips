//? if fabric {
package dev.ultimatchamp.enhancedtooltips.loaders;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.util.CreativeModeTabCollector;
import dev.ultimatchamp.enhancedtooltips.util.ItemGroupsUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class EnhancedTooltipsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.level != null) {
                client.execute(() ->
                        ItemGroupsUtils.tabs.putAll(CreativeModeTabCollector.collectTabs(client.level))
                );
            }
        });

        EnhancedTooltips.init();
    }
}
//?}
