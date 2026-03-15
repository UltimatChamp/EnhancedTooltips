package dev.ultimatchamp.enhancedtooltips.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class TooltipComponentManager {
    private static final List<TooltipComponentEvent> CALLBACKS = new ArrayList<>();

    public static void register(TooltipComponentEvent callback) {
        CALLBACKS.add(callback);
    }

    public static void invoke(List<ClientTooltipComponent> components, ItemStack stack) {
        for (TooltipComponentEvent callback : CALLBACKS) {
            callback.of(components, stack);
        }
    }

    public interface TooltipComponentEvent {
        void of(List<ClientTooltipComponent> list, ItemStack stack);
    }
}
