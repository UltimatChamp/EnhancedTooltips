package dev.ultimatchamp.enhancedtooltips.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TooltipComponentAPI {
    public static final Event<TooltipComponentEvent> EVENT =
            EventFactory.createArrayBacked(TooltipComponentEvent.class, callbacks -> (components, stack) -> {
                for (TooltipComponentEvent callback : callbacks) {
                    callback.of(components, stack);
                }
            });

    public interface TooltipComponentEvent {
        void of(List<TooltipComponent> list, ItemStack stack);
    }
}
