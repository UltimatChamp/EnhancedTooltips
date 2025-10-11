//? if neoforge {
/*package dev.ultimatchamp.enhancedtooltips.loaders;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.CreativeModeTabCollector;
import dev.ultimatchamp.enhancedtooltips.util.ItemGroupsUtils;
import net.minecraft.client.MinecraftClient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

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

    private void collectTabsOnJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (MinecraftClient.getInstance().world != null)
            ItemGroupsUtils.tabs.putAll(CreativeModeTabCollector.collectTabs(MinecraftClient.getInstance().world));
    }
}
*///?}
