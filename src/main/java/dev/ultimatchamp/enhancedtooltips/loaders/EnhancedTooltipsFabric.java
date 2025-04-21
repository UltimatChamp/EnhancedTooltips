//? if fabric {
package dev.ultimatchamp.enhancedtooltips.loaders;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import net.fabricmc.api.ClientModInitializer;

public class EnhancedTooltipsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EnhancedTooltips.init();
    }
}
//?}
