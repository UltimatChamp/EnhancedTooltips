package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

//? if >1.21.5 {
/*import net.minecraft.client.renderer.RenderPipelines;
*///?} else if >1.21.1 {
import net.minecraft.client.renderer.RenderType;
//?}

public class TooltipBackgroundComponent implements ClientTooltipComponent {
    protected static final int INNER_PADDING = 4;

    public void render(GuiGraphics context, int x, int y, int width, int height, int z, int page) throws Exception {
        int i = x - INNER_PADDING;
        int j = y - INNER_PADDING;
        int k = width + INNER_PADDING * 2;
        int l = height + INNER_PADDING * 2;

        int bgColor = EnhancedTooltipsConfig.load().background.backgroundColor.getRGB();
        if (EnhancedTooltipsConfig.load().background.backgroundMode == EnhancedTooltipsConfig.BackgroundMode.DEFAULT) {
            ResourceLocation renderId = ResourceLocation.withDefaultNamespace("tooltip/background");
            ResourceLocation checkId = ResourceLocation.withDefaultNamespace("textures/gui/sprites/" + renderId.getPath() + ".png");

            if (Minecraft.getInstance().getResourceManager().getResource(checkId).isPresent())
                renderBackgroundTexture(context, i, j, k, l, z, renderId);
            else renderBackground(context, i, j, k, l, z, new Color(0xF0100010, true).getRGB());
        } else renderBackground(context, i, j, k, l, z, bgColor);

        renderBorder(context, i, j + 1, k, l, z, page);
    }

    protected void renderBorder(GuiGraphics context, int x, int y, int width, int height, int z, int page) {
        if (EnhancedTooltipsConfig.load().border.borderColor == EnhancedTooltipsConfig.BorderColorMode.RARITY) {
            ResourceLocation renderId = ResourceLocation.withDefaultNamespace("tooltip/frame");
            ResourceLocation checkId = ResourceLocation.withDefaultNamespace("textures/gui/sprites/" + renderId.getPath() + ".png");

            if (Minecraft.getInstance().getResourceManager().getResource(checkId).isPresent()) {
                context.blitSprite(
                        //? if >1.21.5 {
                        /*RenderPipelines.GUI_TEXTURED,
                        *///?} else if >1.21.1 {
                        RenderType::guiTextured,
                         //?}
                        renderId,
                        x - 9,
                        y - 10,
                        width + 18,
                        height + 18
                );
                return;
            }
        }

        int startColor = EnhancedTooltipsConfig.BorderColor.COMMON.getColor().getRGB();
        int endColor = EnhancedTooltipsConfig.BorderColor.END_COLOR.getColor().getRGB();

        if (EnhancedTooltipsConfig.load().border.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM) {
            startColor = EnhancedTooltipsConfig.load().border.customBorderColors.common.getRGB();
            endColor = EnhancedTooltipsConfig.load().border.customBorderColors.endColor.getRGB();
        }

        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }

    protected void renderVerticalLine(GuiGraphics context, int x, int y, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height/*? if <1.21.6 {*/, z/*?}*/, startColor, endColor);
    }

    protected void renderHorizontalLine(GuiGraphics context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1/*? if <1.21.6 {*/, z/*?}*/, color);
    }

    protected void renderRectangle(GuiGraphics context, int x, int y, int width, int height, int z, int bgColor) {
        context.fill(x, y, x + width, y + height/*? if <1.21.6 {*/, z/*?}*/, bgColor);
    }

    protected void renderBackground(GuiGraphics context, int x, int y, int width, int height, int z, int bgColor) {
        renderHorizontalLine(context, x, y - 1, width, z, bgColor);
        renderHorizontalLine(context, x, y + height, width, z, bgColor);
        renderRectangle(context, x, y, width, height, z, bgColor);
        renderVerticalLine(context, x - 1, y, height, z, bgColor, bgColor);
        renderVerticalLine(context, x + width, y, height, z, bgColor, bgColor);
    }

    protected void renderBackgroundTexture(GuiGraphics context, int x, int y, int width, int height, int z, ResourceLocation id) {
        context.blitSprite(
                //? if >1.21.5 {
                /*RenderPipelines.GUI_TEXTURED,
                *///?} else if >1.21.1 {
                RenderType::guiTextured,
                //?}
                id,
                x - 9,
                y - 9,
                width + 18,
                height + 18
        );
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/@NotNull Font textRenderer/*?}*/) {
        return 0;
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        return 0;
    }
}
