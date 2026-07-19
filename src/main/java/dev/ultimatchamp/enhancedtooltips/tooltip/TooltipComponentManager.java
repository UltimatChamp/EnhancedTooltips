package dev.ultimatchamp.enhancedtooltips.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TooltipComponentManager {
    private static final List<Entry> CALLBACKS = new ArrayList<>();

    public static void register(TooltipComponentEvent callback) {
        register(0, callback);
    }

    public static void register(int priority, TooltipComponentEvent callback) {
        CALLBACKS.add(new Entry(priority, callback));
        CALLBACKS.sort(Comparator.comparingInt(Entry::priority));
    }

    public static void invoke(List<ClientTooltipComponent> components, ItemStack stack) {
        for (Entry entry : CALLBACKS) {
            entry.callback().of(components, stack);
        }
    }

    private record Entry(int priority, TooltipComponentEvent callback) {}

    public interface TooltipComponentEvent {
        void of(List<ClientTooltipComponent> list, ItemStack stack);
    }
}
