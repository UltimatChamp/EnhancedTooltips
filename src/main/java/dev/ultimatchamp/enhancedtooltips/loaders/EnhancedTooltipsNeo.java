//? if neoforge {
/*package dev.ultimatchamp.enhancedtooltips.loaders;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.CreativeModeTabCollector;
import dev.ultimatchamp.enhancedtooltips.util.ItemGroupsUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(EnhancedTooltips.MOD_ID)
public final class EnhancedTooltipsNeo {
    public EnhancedTooltipsNeo(ModContainer modContainer, IEventBus modBus) {
        modBus.addListener(this::onClientSetup);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) ->
            EnhancedTooltipsConfig.createConfigScreen(parent)
        );

        modBus.addListener((ClientPlayerNetworkEvent.LoggingIn event) -> {
        ClientWorld world = MinecraftClient.getInstance().world;
            if (world != null) {
                ItemGroupsUtils.tabs.putAll(CreativeModeTabCollector.collectTabs(world));
            }
        });
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        EnhancedTooltips.init();
    }
}
*///?}
