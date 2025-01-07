package dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api;

import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.Comparator;

public class TooltipComparatorProvider {
    private static final Comparator<TooltipComponent> DEFAULT = (o1, o2) -> 0;

    static Comparator<TooltipComponent> COMPARATOR = null;

    public static void setComparator(Comparator<TooltipComponent> comparable) {
        COMPARATOR = comparable;
    }

    public static Comparator<TooltipComponent> getComparator() {
        return COMPARATOR == null ? DEFAULT : COMPARATOR;
    }
}
