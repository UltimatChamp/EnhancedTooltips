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
        return !stack.isDamageable() || (config.durabilityTooltip.equals(EnhancedTooltipsConfig.DurabilityTooltipMode.OFF) && !config.durabilityBar);
    }

    private Text getDurabilityText() {
        int damaged = stack.getMaxDamage() - stack.getDamage();
        return switch (config.durabilityTooltip) {
            case VALUE -> config.durabilityBar
                    ? Text.literal(" " + damaged + " / " + stack.getMaxDamage())
                    : Text.literal(" ")
                    .append(Text.literal(String.valueOf(damaged)).setStyle(Style.EMPTY.withColor(stack.getItemBarColor())))
                    .append(Text.literal(" / ").setStyle(Style.EMPTY.withColor(-8355712)))
                    .append(Text.literal(String.valueOf(stack.getMaxDamage())).setStyle(Style.EMPTY.withColor(0xFF00FF00)));
            case PERCENTAGE -> {
                Text percentageText = Text.literal(" " + (damaged * 100 / stack.getMaxDamage()) + "%");
                yield config.durabilityBar ? percentageText : percentageText.getWithStyle(Style.EMPTY.withColor(stack.getItemBarColor())).get(0);
            }
            default -> Text.empty();
        };
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        if (isDurabilityDisabled()) return 0;
        return config.durabilityBar ? 18 : 17;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (isDurabilityDisabled()) return 0;

        int durabilityTextWidth = textRenderer.getWidth(Text.translatable("enhancedtooltips.tooltip.durability"));
        if (config.durabilityBar) return durabilityTextWidth + SPACING + WIDTH + 1;

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

        y += (config.durabilityBar) ? SPACING : SPACING * 2;

        if (config.durabilityBar) y += 2;
        int textHeight = textRenderer.fontHeight;
        int textY = config.durabilityBar ? y - textHeight + SPACING * 2 + 2 : y;

        context.drawText(textRenderer, Text.translatable("enhancedtooltips.tooltip.durability"), x, textY, -8355712, true);

        x += textRenderer.getWidth(Text.translatable("enhancedtooltips.tooltip.durability")) + SPACING;
        int damaged = stack.getMaxDamage() - stack.getDamage();

        if  (config.durabilityBar)
            context.fill(
                    x,
                    textY - SPACING / 2,
                    x + (damaged * WIDTH) / stack.getMaxDamage(),
                    textY + textHeight,
                    BadgesUtils.darkenColor(0xff000000 | stack.getItemBarColor(), 0.9f)
            );

        Text durabilityText = getDurabilityText();
        if  (!durabilityText.equals(Text.empty())) {
            int textX = config.durabilityBar ? x + ((WIDTH - textRenderer.getWidth(durabilityText)) / 2) : x - SPACING;
            context.drawText(textRenderer, durabilityText, textX, textY, 0xFFFFFFFF, true);
        }

        if  (config.durabilityBar)
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
