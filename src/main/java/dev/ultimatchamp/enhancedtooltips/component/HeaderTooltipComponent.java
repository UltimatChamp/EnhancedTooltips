package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.BadgesUtils;
import dev.ultimatchamp.enhancedtooltips.util.ItemGroupsUtils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.*;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Map;

public class HeaderTooltipComponent implements TooltipComponent {
    private static final int TEXTURE_SIZE = 20;
    private static final int ITEM_MODEL_SIZE = 16;
    private static final int SPACING = 4;
    private final ItemStack stack;
    private final OrderedText nameText;
    private final OrderedText rarityName;

    public HeaderTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.nameText = TooltipHelper.getDisplayName(stack).asOrderedText();
        this.rarityName = TooltipHelper.getRarityName(stack).asOrderedText();
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        return TEXTURE_SIZE + 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int badgeWidth = 0;

        //? if >1.20.6 {
        if (EnhancedTooltipsConfig.load().itemBadges) {
            for (var tab : ItemGroups.getGroups()) {
                if (tab.equals(ItemGroups.getSearchGroup())) continue;

                var identified = false;

                for (var displayStack : tab.getDisplayStacks()) {
                    if (stack.isOf(displayStack.getItem())) {
                        badgeWidth = textRenderer.getWidth(tab.getDisplayName()) + SPACING * 2;
                        identified = true;
                        break;
                    }
                }

                if (identified) break;
            }
        }
        //?}

        return Math.max(textRenderer.getWidth(this.nameText) + badgeWidth, textRenderer.getWidth(this.rarityName)) + SPACING + TEXTURE_SIZE;
    }

    public int getTitleOffset() {
        return SPACING + TEXTURE_SIZE;
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        float startDrawX = (float) x + getTitleOffset();
        float startDrawY = y + 1;
        textRenderer.draw(this.nameText, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);

        if (EnhancedTooltipsConfig.load().rarityTooltip) {
            startDrawY += textRenderer.fontHeight + SPACING;
            textRenderer.draw(this.rarityName, startDrawX, startDrawY, -1, true, matrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, 0xF000F0);
        }
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        int startDrawX = x + (TEXTURE_SIZE - ITEM_MODEL_SIZE) / 2;
        int startDrawY = y + (TEXTURE_SIZE - ITEM_MODEL_SIZE) / 2;

        context.drawItem(this.stack, startDrawX, startDrawY);

        //? if >1.20.6 {
        if (!EnhancedTooltipsConfig.load().itemBadges) return;

        String translation = "gamerule.category.misc";
        int fillColor = -6250336;

        for (Map.Entry<List<Item>, Pair<String, Integer>> entry : ItemGroupsUtils.getItemGroups().entrySet()) {
            if (entry.getKey().contains(stack.getItem())) {
                translation = entry.getValue().getLeft();
                fillColor = entry.getValue().getRight();
                break;
            }
        }

        drawBadge(textRenderer, Text.translatable(translation), x, y, context, fillColor);
        //?}
    }

    //? if >1.20.6 {
    private void drawBadge(TextRenderer textRenderer, Text text, int x, int y, DrawContext context, int fillColor) {
        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;

        int textX = x + getTitleOffset() + textRenderer.getWidth(this.nameText) + SPACING + 2;
        int textY = y - textRenderer.fontHeight + SPACING * 2 + 2;

        context.fill(
                textX - SPACING,
                textY - SPACING / 2,
                textX + textWidth + SPACING,
                textY + textHeight,
                fillColor
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
    //?}
}
