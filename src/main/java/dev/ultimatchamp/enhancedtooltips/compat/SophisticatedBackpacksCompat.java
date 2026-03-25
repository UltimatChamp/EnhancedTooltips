package dev.ultimatchamp.enhancedtooltips.compat;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

/*? if <26.1 {*//*import net.p3pp3rf1y.sophisticatedbackpacks.client.render.ClientBackpackContentsTooltip;*//*?}*/

public class SophisticatedBackpacksCompat {
    public static boolean containsBackpackTooltip(List<ClientTooltipComponent> components) {
        //? if <26.1 {
        /*for (ClientTooltipComponent component : components) {
            if (component instanceof ClientBackpackContentsTooltip)
                return true;
        }
        *///?}

        return false;
    }
}
