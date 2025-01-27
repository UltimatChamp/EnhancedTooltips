package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.item.*;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
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

        if (EnhancedTooltipsConfig.load().itemBadges) {
            for (var tab : ItemGroups.getGroups()) {
                if (tab.equals(ItemGroups.getSearchGroup())) continue;

                var identified = false;

                for (var displayStack : tab.getDisplayStacks()) {
                    if (stack.isOf(displayStack.getItem())) {
                        badgeWidth = textRenderer.getWidth(tab.getDisplayName()) + SPACING * 3 - 2;
                        identified = true;
                        break;
                    }
                }

                if (identified) break;
            }
        }

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

        if (!EnhancedTooltipsConfig.load().itemBadges) return;

        for (var tab : ItemGroups.getGroups()) {
            if (tab.equals(ItemGroups.getSearchGroup())) continue;

            var identified = false;

            for (var displayStack : tab.getDisplayStacks()) {
                if (stack.isOf(displayStack.getItem())) {
                    if (tab.contains(Items.NETHERITE_PICKAXE.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xff9b59b6, 0xff8e44ad); // Tools & Utilities
                    } else if (tab.contains(Items.NETHERITE_SWORD.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xfff94144, 0xffd72638); // Combat
                    } else if (tab.contains(Items.REPEATER.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xffff6b6b, 0xffe63939); // Redstone Blocks
                    } else if (tab.contains(Items.OAK_SIGN.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xff2a9d8f, 0xff207567); // Functional Blocks
                    } else if (tab.contains(Items.COOKED_BEEF.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xff61b748, 0xff7ad85b); // Food and Drinks
                    } else if (tab.contains(Items.GOLD_INGOT.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xffff6347, 0xffffa07a); // Ingredients
                    } else if (tab.contains(Items.ALLAY_SPAWN_EGG.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xffa29bfe, 0xff817dc4); // Spawn Eggs
                    } else if (tab.contains(Items.COMMAND_BLOCK.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xff9c89b8, 0xff7a6395); // Operator Utilities
                    } else if (tab.contains(Items.GRASS_BLOCK.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xff66bb6a, 0xffa5d6a7); // Natural Blocks
                    } else if (tab.contains(Items.BLUE_WOOL.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xff42a5f5, 0xff90caf9); // Colored Blocks
                    } else if (tab.contains(Items.IRON_BARS.getDefaultStack())) {
                        drawBadge(textRenderer, tab.getDisplayName(), x, y, context, 0xfff2c94c, 0xffe0a800); // Building Blocks
                    }

                    identified = true;
                    break;
                }
            }

            if (identified) break;
        }
    }

    private void drawBadge(TextRenderer textRenderer, Text text, int x, int y, DrawContext context, int fillColor, int borderColor) {
        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;

        int textX = x + getTitleOffset() + textRenderer.getWidth(this.nameText) + SPACING * 2;
        int textY = y - textRenderer.fontHeight + SPACING * 2 + 2;

        context.fill(
                textX - SPACING - 2,
                textY - SPACING / 2,
                textX + textWidth + SPACING,
                textY + textHeight,
                fillColor
        );

        context.drawText(
                textRenderer,
                text,
                textX - 2,
                textY,
                0xffffffff,
                true
        );

        drawFrame(
                context,
                textX - SPACING - 2,
                textY - SPACING / 2,
                textWidth + SPACING * 2,
                textHeight + SPACING,
                400,
                borderColor
        );
    }

    private static void drawFrame(DrawContext context, int x, int y, int width, int height, int z, int color) {
        renderVerticalLine(context, x, y, height - 2, z, color);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1, width - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1 + height - 1, width - 2, z, color);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }
}
