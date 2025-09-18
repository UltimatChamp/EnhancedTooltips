package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.*;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

//? if <1.21.6 {
/*import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;
*///?}

public class HeaderTooltipComponent implements TooltipComponent {
    private static final int TEXTURE_SIZE = 16;
    private static final int SPACING = 4;
    private final ItemStack stack;
    private final Text nameText;
    private final Text rarityName;
    private final EnhancedTooltipsConfig config;

    public HeaderTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.nameText = TooltipHelper.getDisplayName(stack);
        this.rarityName = TooltipHelper.getRarityName(stack);
        this.config = EnhancedTooltipsConfig.load();
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        return getTitleOffset();
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int rarityWidth = 0;
        if (config.general.rarityTooltip) rarityWidth = textRenderer.getWidth(this.rarityName);

        int badgeWidth = 0;
        Text badgeText = BadgesUtils.getBadgeText(stack).getLeft();
        if (config.general.itemBadges && !badgeText.withoutStyle().isEmpty()) badgeWidth = textRenderer.getWidth(badgeText) + SPACING * 2;

        int titleWidth;
        if (config.general.rarityTooltip) {
            titleWidth = textRenderer.getWidth(this.nameText) + badgeWidth;
        } else {
            titleWidth = Math.max(textRenderer.getWidth(this.nameText), badgeWidth);
        }

        return Math.max(titleWidth, rarityWidth) + getTitleOffset() + (getTitleOffset() - TEXTURE_SIZE) / 2 + 2;
    }

    public int getTitleOffset() {
        return 26;
    }

    @Override
    //? if >1.21.5 {
    public void drawText(DrawContext context, TextRenderer textRenderer, int x, int y) {
    //?} else {
    /*public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
    *///?}
        int startDrawX = x + getTitleOffset();
        int startDrawY = y;

        if (config.general.rarityTooltip)
            startDrawY += 2;
        else if (!config.general.itemBadges || BadgesUtils.getBadgeText(stack).getLeft().withoutStyle().isEmpty())
            startDrawY += (int) ((getTitleOffset() - textRenderer.fontHeight) / 2f);

        //? if >1.21.5 {
        context.drawText(textRenderer, this.nameText, startDrawX, startDrawY, -1, true);
        //?} else {
        /*textRenderer.draw(this.nameText, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        *///?}

        if (config.general.rarityTooltip) {
            startDrawY += textRenderer.fontHeight + SPACING;
            //? if >1.21.5 {
            context.drawText(textRenderer, this.rarityName, startDrawX, startDrawY, -1, true);
            //?} else {
            /*textRenderer.draw(this.rarityName, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
            *///?}
        }
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        int startDrawX = x + (getTitleOffset() - TEXTURE_SIZE) / 2 - 1;
        int startDrawY = y + (getTitleOffset() - TEXTURE_SIZE) / 2 - 1;

        float bounce = 0f;
        if (config.itemPreviewAnimation.enabled) {
            int sec = (int) (config.itemPreviewAnimation.time * 1000);
            float time = (float) (System.currentTimeMillis() % sec) / sec;

            bounce = (float) Math.sin(time * Math.PI * 2) * (config.itemPreviewAnimation.magnitude * /*? if >1.21.5 {*/1/*?} else {*//*config.general.scaleFactor*//*?}*/);
        }

        context.drawItem(this.stack, startDrawX, (int) (startDrawY - bounce));

        if (!config.general.itemBadges) return;
        if (!config.general.rarityTooltip) y += textRenderer.fontHeight + SPACING;

        Pair<Text, Integer> badgeText = BadgesUtils.getBadgeText(stack);
        if (!badgeText.getLeft().withoutStyle().isEmpty()) drawBadge(textRenderer, badgeText.getLeft(), x, y, context, badgeText.getRight());
    }

    private void drawBadge(TextRenderer textRenderer, Text text, int x, int y, DrawContext context, int fillColor) {
        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;

        int textX = x + getTitleOffset() + (!config.general.rarityTooltip ? 4 : textRenderer.getWidth(this.nameText) + SPACING + 2);
        int textY = y - textRenderer.fontHeight + SPACING * 2 + 2 + 1;

        context.fill(
                textX - SPACING,
                textY - SPACING / 2,
                textX + textWidth + SPACING,
                textY + textHeight,
                BadgesUtils.darkenColor(fillColor, 0.9f)
        );

        context.drawText(
                textRenderer,
                text,
                textX,
                textY,
                0xffffffff,
                true
        );

        BadgesUtils.drawFrame(
                context,
                textX - SPACING,
                textY - SPACING / 2,
                textWidth + SPACING * 2,
                textHeight + SPACING,
                400,
                BadgesUtils.darkenColor(fillColor, 0.8f)
        );
    }
}
