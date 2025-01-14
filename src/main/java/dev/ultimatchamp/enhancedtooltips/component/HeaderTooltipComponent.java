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

//? if >1.21.1 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
//?} else if >1.20.4 {
/*import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
*///?} else {
/*import net.minecraft.item.FoodComponent;
*///?}

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
            if (this.stack.getItem() instanceof ArmorItem || this.stack.getItem() instanceof
                    //? if >1.20.4 {
                    AnimalArmorItem) {
                    //?} else {
                    /*HorseArmorItem) {
                    *///?}
                badgeWidth = textRenderer.getWidth(Text.translatable("attribute.name.armor")) + SPACING * 3;
            } else if (this.stack.isDamageable()) {
                badgeWidth = textRenderer.getWidth(Text.translatable("itemGroup.tools")) + SPACING * 3;
            } else if (this.stack.getItem() instanceof EntityBucketItem) {
                badgeWidth = textRenderer.getWidth(Text.translatable("item.minecraft.bucket")) + SPACING * 3;
            } else if (this.stack.getItem() instanceof SpawnEggItem) {
                badgeWidth = textRenderer.getWidth(Text.translatable("itemGroup.spawnEggs")) + SPACING * 3;
            } else if (this.stack.getItem() instanceof BlockItem) {
                badgeWidth = textRenderer.getWidth(Text.translatable("soundCategory.block")) + SPACING * 3;
            } else {
                //? if >1.21.1 {
                ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
                FoodComponent foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
                if ((foodComponent != null && consumableComponent != null) || stack.getItem() instanceof PotionItem) {
                //?} else if >1.20.4 {
                /*FoodComponent foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
                if (foodComponent != null) {
                *///?} else {
                /*FoodComponent foodComponent = stack.getItem().getFoodComponent();
                if (foodComponent != null) {
                *///?}
                    badgeWidth = textRenderer.getWidth(Text.translatable("itemGroup.foodAndDrink")) + SPACING * 3;
                }
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

        drawFrame(context, x, y + 1, TEXTURE_SIZE, TEXTURE_SIZE, 400, 0xffd6d6d6, 0xffd6d6d6);
        context.drawItem(this.stack, startDrawX, startDrawY);

        if (!EnhancedTooltipsConfig.load().itemBadges) return;

        if (this.stack.getItem() instanceof ArmorItem || this.stack.getItem() instanceof
                //? if >1.20.4 {
                AnimalArmorItem) {
                //?} else {
                /*HorseArmorItem) {
                *///?}
            drawBadge(textRenderer, Text.translatable("attribute.name.armor"), x, y, context, 0xff00ffff, 0xffc8ffff, 0xff6ac7be);
        } else if (this.stack.isDamageable()) {
            drawBadge(textRenderer, Text.translatable("itemGroup.tools"), x, y, context, 0xff57b88c, 0xff6acda4, 0xff4e866f);
        } else if (this.stack.getItem() instanceof EntityBucketItem) {
            drawBadge(textRenderer, Text.translatable("item.minecraft.bucket"), x, y, context, 0xff14564b, 0xff3d8075, 0xff367166);
        } else if (this.stack.getItem() instanceof SpawnEggItem) {
            drawBadge(textRenderer, Text.translatable("itemGroup.spawnEggs"), x, y, context, 0xff70589b, 0xffa994cf, 0xffa994ff);
        } else if (this.stack.getItem() instanceof BlockItem) {
            drawBadge(textRenderer, Text.translatable("soundCategory.block"), x, y, context, 0xff396425, 0xff496c67, 0xff38502b);
        } else {
            //? if >1.21.1 {
            ConsumableComponent consumableComponent = stack.get(DataComponentTypes.CONSUMABLE);
            FoodComponent foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
            if ((foodComponent != null && consumableComponent != null) || stack.getItem() instanceof PotionItem) {
            //?} else if >1.20.4 {
            /*FoodComponent foodComponent = stack.getItem().getComponents().get(DataComponentTypes.FOOD);
            if (foodComponent != null) {
            *///?} else {
            /*FoodComponent foodComponent = stack.getItem().getFoodComponent();
            if (foodComponent != null) {
            *///?}
                drawBadge(textRenderer, Text.translatable("itemGroup.foodAndDrink"), x, y, context, 0xff61b748, 0xff7ad85b, 0xff8ab85b);
            }
        }
    }

    private void drawBadge(TextRenderer textRenderer, Text text, int x, int y, DrawContext context, int fillColor, int startColor, int endColor) {
        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;

        int textX = x + getTitleOffset() + textRenderer.getWidth(this.nameText) + SPACING * 2;
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

        drawFrame(
                context,
                textX - SPACING,
                textY - SPACING / 2,
                textWidth + SPACING * 2,
                textHeight + SPACING,
                400,
                startColor,
                endColor
        );
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
