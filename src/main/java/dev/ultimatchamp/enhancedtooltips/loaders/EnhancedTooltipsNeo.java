//? if neoforge {
/*package dev.ultimatchamp.enhancedtooltips.loaders;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.CreativeModeTabCollector;
import dev.ultimatchamp.enhancedtooltips.util.ItemGroupsUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(EnhancedTooltips.MOD_ID)
public final class EnhancedTooltipsNeo {
    public EnhancedTooltipsNeo(ModContainer modContainer, IEventBus modBus) {
        modBus.addListener(this::onClientSetup);
        NeoForge.EVENT_BUS.addListener(this::collectTabsOnJoin);

        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (client, parent) -> EnhancedTooltipsConfig.createConfigScreen(parent));
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        EnhancedTooltips.init();
    }

    private void collectTabsOnJoin(ClientPlayerNetworkEvent.LoggingIn event) {
        Minecraft.getInstance().execute(() ->
                ItemGroupsUtils.tabs.putAll(CreativeModeTabCollector.collectTabs(event.getPlayer().level()))
        );
    }
}
*///?}
