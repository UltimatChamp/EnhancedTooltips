package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.BadgesUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class DurabilityTooltipComponent implements TooltipComponent {
    private static final int SPACING = 4;
    private static final int WIDTH = 80;
    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;

    public DurabilityTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
    }

    public boolean isDurabilityDisabled() {
        return !stack.isDamageable() || (config.durability.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) && !config.durability.durabilityBar);
    }

    private Text getDurabilityText() {
        int remaining = stack.getMaxDamage() - stack.getDamage();
        if (remaining <= 0) return Text.empty();
        return switch (config.durability.durabilityTooltip) {
            case VALUE -> config.durability.durabilityBar
                    ? Text.literal(" " + remaining + " / " + stack.getMaxDamage())
                    : Text.literal(" ")
                    .append(Text.literal(String.valueOf(remaining)).setStyle(Style.EMPTY.withColor(stack.getItemBarColor())))
                    .append(Text.literal(" / ").setStyle(Style.EMPTY.withColor(-4539718)))
                    .append(Text.literal(String.valueOf(stack.getMaxDamage())).setStyle(Style.EMPTY.withColor(0xFF00FF00)));
            case PERCENTAGE -> {
                Text percentageText = Text.literal(" " + (remaining * 100 / stack.getMaxDamage()) + "%");
                yield config.durability.durabilityBar ? percentageText : percentageText.getWithStyle(Style.EMPTY.withColor(stack.getItemBarColor())).getFirst();
            }
            default -> Text.empty();
        };
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        if (isDurabilityDisabled()) return 0;
        return config.durability.durabilityBar ? 18 : 17;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (isDurabilityDisabled()) return 0;

        int durabilityTextWidth = textRenderer.getWidth(Text.translatable("enhancedtooltips.tooltip.durability"));
        if (config.durability.durabilityBar) return durabilityTextWidth + SPACING + WIDTH + 1;

        Text durability = getDurabilityText();
        return durabilityTextWidth + textRenderer.getWidth(durability);
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        if (isDurabilityDisabled()) return;

        y += (config.durability.durabilityBar) ? SPACING : SPACING * 2;

        if (config.durability.durabilityBar) y += 2;
        int textHeight = textRenderer.fontHeight;
        int textY = config.durability.durabilityBar ? y - textHeight + SPACING * 2 + 2 : y;

        context.drawText(textRenderer, Text.translatable("enhancedtooltips.tooltip.durability"), x, textY, -4539718, true);

        x += textRenderer.getWidth(Text.translatable("enhancedtooltips.tooltip.durability")) + SPACING;
        int remaining = stack.getMaxDamage() - stack.getDamage();

        if  (config.durability.durabilityBar && remaining > 0)
            context.fill(
                    x,
                    textY - SPACING / 2,
                    x + (remaining * WIDTH) / stack.getMaxDamage(),
                    textY + textHeight,
                    BadgesUtils.darkenColor(0xff000000 | stack.getItemBarColor(), 0.9f)
            );

        Text durabilityText = getDurabilityText();
        if  (!durabilityText.equals(Text.empty())) {
            int textX = config.durability.durabilityBar ? x + ((WIDTH - textRenderer.getWidth(durabilityText)) / 2) : x - SPACING;
            context.drawText(textRenderer, durabilityText, textX, textY, 0xFFFFFFFF, true);
        }

        if  (config.durability.durabilityBar)
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
