package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.BadgesUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class DurabilityTooltipComponent implements TooltipComponent {
    private static final int SPACING = 4;
    private static final int WIDTH = 80;
    private final ItemStack stack;

    public DurabilityTooltipComponent(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        if (!stack.isDamageable() || EnhancedTooltipsConfig.load().durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF)) return 0;
        return 14;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (!stack.isDamageable() || EnhancedTooltipsConfig.load().durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF)) return 0;
        return textRenderer.getWidth(Text.translatable("enhancedtooltips.tooltip.durability")) + SPACING + WIDTH + 1;
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        if (!stack.isDamageable() || EnhancedTooltipsConfig.load().durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF)) return;

        y += 2;

        int textHeight = textRenderer.fontHeight;
        int textY = y - textRenderer.fontHeight + SPACING * 2 + 2;

        context.drawText(
                textRenderer,
                Text.translatable("enhancedtooltips.tooltip.durability"),
                x,
                textY,
                0xffffffff,
                true
        );

        x += textRenderer.getWidth(Text.translatable("enhancedtooltips.tooltip.durability")) + SPACING;

        var damaged = stack.getMaxDamage() - stack.getDamage();

        context.fill(
                x,
                textY - SPACING / 2,
                x + (damaged * WIDTH) / stack.getMaxDamage(),
                textY + textHeight,
                0xff000000 | stack.getItemBarColor()
        );

        Text durabilityText = Text.empty();
        if (EnhancedTooltipsConfig.load().durabilityTooltip == EnhancedTooltipsConfig.DurabilityTooltipMode.VALUE) {
            durabilityText = Text.literal(" " + damaged + " / " + stack.getMaxDamage());
        } else if (EnhancedTooltipsConfig.load().durabilityTooltip == EnhancedTooltipsConfig.DurabilityTooltipMode.PERCENTAGE) {
            durabilityText = Text.literal(" " + damaged * 100 / stack.getMaxDamage() + "%");
        }

        int textX = x + ((WIDTH - textRenderer.getWidth(durabilityText)) / 2);

        context.drawText(
                textRenderer,
                durabilityText,
                textX,
                textY,
                0xffffffff,
                true
        );

        BadgesUtils.drawFrame(
                context,
                x,
                textY - SPACING / 2,
                WIDTH,
                textHeight + SPACING,
                400,
                BadgesUtils.darkenColor(0xff000000 | stack.getItemBarColor(), 0.8f)
        );
    }
}
