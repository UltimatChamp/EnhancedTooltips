package io.github.yanggx98.immersive.tooltip.component;

import io.github.yanggx98.immersive.tooltip.TooltipHelper;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;

public class HeaderTooltipComponent implements TooltipComponent {
    private static final int TEXTURE_SIZE = 24;
    private static final int ITEM_MODEL_SIZE = 16;
    private static final int SPACING = 4;
    private final ItemStack stack;
    private final OrderedText nameText;
    private final OrderedText rarityName;

    public HeaderTooltipComponent(ItemStack stack) {
        this.stack = stack;
        nameText = TooltipHelper.getDisplayName(stack).asOrderedText();
        rarityName = TooltipHelper.getRarityName(stack).asOrderedText();
    }

    @Override
    public int getHeight(TextRenderer textRenderer) {
        return TEXTURE_SIZE + 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return Math.max(textRenderer.getWidth(nameText), textRenderer.getWidth(rarityName)) + SPACING + TEXTURE_SIZE;
    }

    public int getTitleOffset() {
        return SPACING + TEXTURE_SIZE;
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        float startDrawX = (float) x + getTitleOffset();
        float startDrawY = y;
        textRenderer.draw(nameText, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        startDrawY = y + textRenderer.fontHeight + SPACING;
        textRenderer.draw(rarityName, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
        int startDrawX = x + (TEXTURE_SIZE - ITEM_MODEL_SIZE) / 2;
        int startDrawY = y + (TEXTURE_SIZE - ITEM_MODEL_SIZE) / 2;

        drawFrame(context, x, y + 1, TEXTURE_SIZE, TEXTURE_SIZE, 400, 0xffd6d6d6, 0xffd6d6d6);
        context.drawItem(this.stack, startDrawX, startDrawY);
    }

    private static void drawFrame(DrawContext context, int x, int y, int width, int height, int z, int startColor, int endColor) {
        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x + 1, y - 1, width - 2, z, startColor);
        renderHorizontalLine(context, x + 1, y - 1 + height - 1, width - 2, z, endColor);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }
}
