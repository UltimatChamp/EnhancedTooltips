package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.util.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderTooltipComponent implements TooltipComponent {
    private static final int TEXTURE_SIZE = 20;
    private static final int ITEM_MODEL_SIZE = 16;
    private static final int SPACING = 4;
    private final ItemStack stack;
    private final OrderedText nameText;
    private final OrderedText rarityName;
    private final Map<String, String> mods = new HashMap<>();
    private final EnhancedTooltipsConfig config;

    public HeaderTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.nameText = TooltipHelper.getDisplayName(stack).asOrderedText();
        this.rarityName = TooltipHelper.getRarityName(stack).asOrderedText();
        this.config = EnhancedTooltipsConfig.load();

        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            if (modContainer.getMetadata().getId().equals("minecraft")) continue;
            this.mods.put(modContainer.getMetadata().getId(), modContainer.getMetadata().getName());
        }
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        return TEXTURE_SIZE + 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int badgeWidth = 0;

        if (config.itemBadges) {
            String badgeText = "gamerule.category.misc";

            for (Map.Entry<List<Item>, Pair<String, Integer>> entry : ItemGroupsUtils.getItemGroups().entrySet()) {
                if (entry.getKey().contains(stack.getItem())) {
                    badgeText = entry.getValue().getLeft();
                    badgeWidth = textRenderer.getWidth(Text.translatable(badgeText)) + SPACING * 2;
                    break;
                }
            }

            if (badgeText.equals("gamerule.category.misc")) {
                String namespace = Registries.ITEM.getId(stack.getItem()).getNamespace();
                badgeText = this.mods.getOrDefault(namespace, "gamerule.category.misc");
                badgeWidth = textRenderer.getWidth(Text.translatable(badgeText)) + SPACING * 2;
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

        if (config.rarityTooltip) {
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

        if (config.itemPreviewAnimation) {
            int sec = (int) (config.itemPreviewAnimationTime * 1000);
            float time = (float) (System.currentTimeMillis() % sec) / sec;

            float bounce = (float) Math.sin(time * Math.PI * 2) * (config.itemPreviewAnimationMagnitude * config.scaleFactor);

            MatrixStack matrixStack = new MatrixStack();
            matrixStack.translate(0, bounce, 0);

            context.getMatrices().push();
            context.getMatrices().multiplyPositionMatrix(matrixStack.peek().getPositionMatrix());
        }

        context.drawItem(this.stack, startDrawX, startDrawY);

        if (config.itemPreviewAnimation) context.getMatrices().pop();

        if (!config.itemBadges) return;

        String translation = "gamerule.category.misc";
        int fillColor = -6250336;

        for (Map.Entry<List<Item>, Pair<String, Integer>> entry : ItemGroupsUtils.getItemGroups().entrySet()) {
            if (entry.getKey().contains(stack.getItem())) {
                translation = entry.getValue().getLeft();
                fillColor = entry.getValue().getRight();
                break;
            }
        }

        if (translation.equals("gamerule.category.misc")) {
            String namespace = Registries.ITEM.getId(stack.getItem()).getNamespace();
            translation = this.mods.getOrDefault(namespace, "gamerule.category.misc");

            if (!translation.equals("gamerule.category.misc")) fillColor = 0xff0d5e7b;
        }

        drawBadge(textRenderer, Text.translatable(translation), x, y, context, fillColor);
    }

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
