package dev.ultimatchamp.enhancedtooltips.compat;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.ClientBackpackContentsTooltip;

import java.util.List;

public class SophisticatedBackpacksCompat {
    public static boolean containsBackpackTooltip(List<ClientTooltipComponent> components) {
        for (ClientTooltipComponent component : components) {
            if (component instanceof ClientBackpackContentsTooltip)
                return true;
        }

        return false;
    }
}
