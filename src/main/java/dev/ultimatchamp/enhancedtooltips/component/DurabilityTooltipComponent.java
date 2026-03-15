package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.BadgesUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DurabilityTooltipComponent implements ClientTooltipComponent {
    private static final int SPACING = 4;
    private static final int WIDTH = 80;
    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;

    public DurabilityTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
    }

    public boolean isDurabilityDisabled() {
        return !stack.isDamageableItem() || (config.durability.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) && !config.durability.durabilityBar);
    }

    private Component getDurabilityText() {
        int remaining = stack.getMaxDamage() - stack.getDamageValue();
        if (remaining <= 0) return Component.empty();
        return switch (config.durability.durabilityTooltip) {
            case VALUE -> config.durability.durabilityBar
                    ? Component.literal(" " + remaining + " / " + stack.getMaxDamage())
                    : Component.literal(" ")
                    .append(Component.literal(String.valueOf(remaining)).setStyle(Style.EMPTY.withColor(stack.getBarColor())))
                    .append(Component.literal(" / ").setStyle(Style.EMPTY.withColor(-4539718)))
                    .append(Component.literal(String.valueOf(stack.getMaxDamage())).setStyle(Style.EMPTY.withColor(0xFF00FF00)));
            case PERCENTAGE -> {
                Component percentageText = Component.literal(" " + (remaining * 100 / stack.getMaxDamage()) + "%");
                yield config.durability.durabilityBar ? percentageText : percentageText.toFlatList(Style.EMPTY.withColor(stack.getBarColor())).getFirst();
            }
            default -> Component.empty();
        };
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/@NotNull Font textRenderer/*?}*/) {
        if (isDurabilityDisabled()) return 0;
        return (config.durability.durabilityBar ? 18 : 17) - (config.general.removeAllSpacing ? (config.durability.durabilityBar ? SPACING : SPACING + 2) : 0);
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        if (isDurabilityDisabled()) return 0;

        int durabilityTextWidth = textRenderer.width(Component.translatable("enhancedtooltips.tooltip.durability"));
        if (config.durability.durabilityBar) return durabilityTextWidth + SPACING + WIDTH + 1;

        Component durability = getDurabilityText();
        return durabilityTextWidth + textRenderer.width(durability);
    }

    @Override
    //? if >1.21.1 {
    public void renderImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphics context) {
    //?} else {
    /*public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
    *///?}
        if (isDurabilityDisabled()) return;

        if (!config.general.removeAllSpacing)
            y += (config.durability.durabilityBar) ? SPACING : SPACING * 2;

        if (config.durability.durabilityBar) y += 2;
        int textHeight = textRenderer.lineHeight;
        int textY = config.durability.durabilityBar ? y - textHeight + SPACING * 2 + 2 : y;

        context.drawString(textRenderer, Component.translatable("enhancedtooltips.tooltip.durability"), x, textY, -4539718, true);

        x += textRenderer.width(Component.translatable("enhancedtooltips.tooltip.durability")) + SPACING;
        int remaining = stack.getMaxDamage() - stack.getDamageValue();

        if  (config.durability.durabilityBar && remaining > 0)
            context.fill(
                    x,
                    textY - SPACING / 2,
                    x + (remaining * WIDTH) / stack.getMaxDamage(),
                    textY + textHeight,
                    BadgesUtils.darkenColor(0xff000000 | stack.getBarColor(), 0.9f)
            );

        Component durabilityText = getDurabilityText();
        if  (!durabilityText.equals(Component.empty())) {
            int textX = config.durability.durabilityBar ? x + ((WIDTH - textRenderer.width(durabilityText)) / 2) : x - SPACING;
            context.drawString(textRenderer, durabilityText, textX, textY, 0xFFFFFFFF, true);
        }

        if (config.durability.durabilityBar)
            BadgesUtils.drawFrame(
                    context,
                    x,
                    textY - SPACING / 2,
                    WIDTH,
                    textHeight + SPACING,
                    400,
                    BadgesUtils.darkenColor(0xff000000 | stack.getBarColor(), 0.8f)
            );
    }
}
