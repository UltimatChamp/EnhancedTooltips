package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

//? if <1.21.6 {
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
//?}

public class HeaderTooltipComponent implements ClientTooltipComponent {
    private static final int TEXTURE_SIZE = 16;
    private static final int SPACING = 4;
    private final ItemStack stack;
    private final Component nameText;
    private final Component rarityName;
    private final EnhancedTooltipsConfig config;

    public HeaderTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.nameText = TooltipHelper.getDisplayName(stack);
        this.rarityName = TooltipHelper.getRarityName(stack);
        this.config = EnhancedTooltipsConfig.load();
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/@NotNull Font textRenderer/*?}*/) {
        return getTitleOffset();
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        int rarityWidth = 0;
        if (config.general.rarityTooltip) rarityWidth = textRenderer.width(this.rarityName);

        int badgeWidth = 0;
        Component badgeText = BadgesUtils.getBadgeText(stack).getA();
        if (config.general.itemBadges && !badgeText.toFlatList().isEmpty()) badgeWidth = textRenderer.width(badgeText) + SPACING * 2;

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
    /*public void renderText(@NotNull GuiGraphics context, @NotNull Font textRenderer, int x, int y) {
    *///?} else {
    public void renderText(Font textRenderer, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource vertexConsumers) {
    //?}
        int startDrawX = x + getTitleOffset();
        int startDrawY = y;

        if (config.general.rarityTooltip)
            startDrawY += 2;
        else if (!config.general.itemBadges || BadgesUtils.getBadgeText(stack).getA().toFlatList().isEmpty())
            startDrawY += (int) ((getTitleOffset() - textRenderer.lineHeight) / 2f);

        //? if >1.21.5 {
        /*context.drawString(textRenderer, this.nameText, startDrawX, startDrawY, -1, true);
        *///?} else {
        textRenderer.drawInBatch(this.nameText, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
        //?}

        if (config.general.rarityTooltip) {
            startDrawY += textRenderer.lineHeight + SPACING;
            //? if >1.21.5 {
            /*context.drawString(textRenderer, this.rarityName, startDrawX, startDrawY, -1, true);
            *///?} else {
            textRenderer.drawInBatch(this.rarityName, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            //?}
        }
    }

    @Override
    //? if >1.21.1 {
    public void renderImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphics context) {
    //?} else {
    /*public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
    *///?}
        int startDrawX = x + (getTitleOffset() - TEXTURE_SIZE) / 2 - 1;
        int startDrawY = y + (getTitleOffset() - TEXTURE_SIZE) / 2 - 1;

        float bounce = 0f;
        if (config.itemPreviewAnimation.enabled) {
            int sec = (int) (config.itemPreviewAnimation.time * 1000);
            float time = (float) (System.currentTimeMillis() % sec) / sec;

            bounce = (float) Math.sin(time * Math.PI * 2) * (config.itemPreviewAnimation.magnitude * /*? if >1.21.5 {*//*1*//*?} else {*/config.general.scaleFactor/*?}*/);
        }

        context.renderItem(this.stack, startDrawX, (int) (startDrawY - bounce));

        if (!config.general.itemBadges) return;
        if (!config.general.rarityTooltip) y += textRenderer.lineHeight + SPACING;

        Tuple<@NotNull Component, @NotNull Integer> badgeText = BadgesUtils.getBadgeText(stack);
        if (!badgeText.getA().toFlatList().isEmpty()) drawBadge(textRenderer, badgeText.getA(), x, y, context, badgeText.getB());
    }

    private void drawBadge(Font textRenderer, Component text, int x, int y, GuiGraphics context, int fillColor) {
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

        context.drawString(
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
