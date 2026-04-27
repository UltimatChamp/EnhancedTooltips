package dev.ultimatchamp.enhancedtooltips.compat;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

//? if !fabric || <26.1 {
/*import net.p3pp3rf1y.sophisticatedcore.client.render.ClientStorageContentsTooltipBase;
*///?}

public class SophisticatedBackpacksCompat {
    public static boolean containsBackpackTooltip(List<ClientTooltipComponent> components) {
        //? if !fabric || <26.1 {
        /*for (ClientTooltipComponent component : components) {
            if (component instanceof ClientStorageContentsTooltipBase)
                return true;
        }
        *///?}

        return false;
    }
}
