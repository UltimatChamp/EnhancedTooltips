package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.*;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//? if <1.21.6 {
/*import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
*///?}

public class HeaderTooltipComponent implements EnhancedTooltipsTooltipComponent {
    private static final int TEXTURE_SIZE = 16;
    private static final int SPACING = 4;
    private final ItemStack stack;
    private final Component nameText;
    private final Component rarityName;
    private final EnhancedTooltipsConfig config;
    private final Component badgeText;
    private final int badgeColor;
    private final boolean hasBadge;

    public HeaderTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.nameText = TooltipHelper.getDisplayName(stack);
        this.rarityName = TooltipHelper.getRarityName(stack);
        this.config = EnhancedTooltipsConfig.load();

        Pair<@NotNull Component, @NotNull Integer> badge = BadgesUtils.getBadgeText(stack);
        this.badgeText = badge.left();
        this.badgeColor = badge.right();
        this.hasBadge = !this.badgeText.toFlatList().isEmpty();
    }

    @Override
    public int height() {
        return getTitleOffset();
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        int rarityWidth = 0;
        if (config.general.rarityTooltip) rarityWidth = textRenderer.width(this.rarityName);

        int badgeWidth = 0;
        if (config.general.itemBadges && hasBadge) badgeWidth = textRenderer.width(this.badgeText) + SPACING * 2;

        int titleWidth;
        if (config.general.rarityTooltip) {
            titleWidth = textRenderer.width(this.nameText) + badgeWidth;
        } else {
            titleWidth = Math.max(textRenderer.width(this.nameText), badgeWidth);
        }

        return Math.max(titleWidth, rarityWidth) + getTitleOffset() + (getTitleOffset() - TEXTURE_SIZE) / 2 + 2;
    }

    public int getTitleOffset() {
        return 26;
    }

    @Override
    //? if >1.21.5 {
    public void drawText(@NotNull GuiGraphicsExtractor context, @NotNull Font textRenderer, int x, int y) {
    //?} else {
    /*public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
    *///?}
        int startDrawX = x + getTitleOffset();
        int startDrawY = y;

        if (config.general.rarityTooltip)
            startDrawY += 2;
        else if (!config.general.itemBadges || !hasBadge)
            startDrawY += (int) ((getTitleOffset() - textRenderer.lineHeight) / 2f);

        //? if >1.21.5 {
        TooltipHelper.renderText(context, textRenderer, this.nameText, startDrawX, startDrawY, -1, true);
        //?} else {
        /*textRenderer.drawInBatch(this.nameText, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        *///?}

        if (config.general.rarityTooltip) {
            startDrawY += textRenderer.lineHeight + SPACING;
            //? if >1.21.5 {
            TooltipHelper.renderText(context, textRenderer, this.rarityName, startDrawX, startDrawY, -1, true);
            //?} else {
            /*textRenderer.drawInBatch(this.rarityName, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            *///?}
        }
    }

    @Override
    public void drawImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphicsExtractor context) {
        int startDrawX = x + (getTitleOffset() - TEXTURE_SIZE) / 2 - 1;
        int startDrawY = y + (getTitleOffset() - TEXTURE_SIZE) / 2 - 1;

        int sec = (int) (config.itemPreviewAnimation.time * 1000);

        float bounce = 0f;
        if (config.itemPreviewAnimation.enabled && sec > 0) {
            float time = (float) (System.currentTimeMillis() % sec) / sec;

            bounce = (float) Math.sin(time * Math.PI * 2) * (config.itemPreviewAnimation.magnitude * /*? if >1.21.5 {*/1/*?} else {*//*config.general.scaleFactor*//*?}*/);
        }

        //? if >1.21.11 {
        context.item(this.stack, startDrawX, (int) (startDrawY - bounce));
        //?} else {
        /*context.renderItem(this.stack, startDrawX, (int) (startDrawY - bounce));
        *///?}

        if (!config.general.itemBadges) return;
        if (!config.general.rarityTooltip) y += textRenderer.lineHeight + SPACING;

        if (hasBadge) drawBadge(textRenderer, this.badgeText, x, y, context, this.badgeColor);
    }

    private void drawBadge(Font textRenderer, Component text, int x, int y, GuiGraphicsExtractor context, int fillColor) {
        int textWidth = textRenderer.width(text);
        int textHeight = textRenderer.lineHeight;

        int textX = x + getTitleOffset() + (!config.general.rarityTooltip ? 4 : textRenderer.width(this.nameText) + SPACING + 2);
        int textY = y - textRenderer.lineHeight + SPACING * 2 + 2 + 1;

        context.fill(
                textX - SPACING,
                textY - SPACING / 2,
                textX + textWidth + SPACING,
                textY + textHeight,
                BadgesUtils.darkenColor(fillColor, 0.9f)
        );

        TooltipHelper.renderText(
                context,
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
