package dev.ultimatchamp.enhancedtooltips.tooltip;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TooltipComponentManager {
    private static final List<TooltipComponentEvent> CALLBACKS = new ArrayList<>();

    public static void register(TooltipComponentEvent callback) {
        CALLBACKS.add(callback);
    }

    public static void invoke(List<TooltipComponent> components, ItemStack stack) {
        for (TooltipComponentEvent callback : CALLBACKS) {
            callback.of(components, stack);
        }
    }

    public interface TooltipComponentEvent {
        void of(List<TooltipComponent> list, ItemStack stack);
    }
}
