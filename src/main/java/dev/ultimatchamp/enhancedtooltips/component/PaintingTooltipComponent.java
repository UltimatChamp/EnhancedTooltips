package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.PaintingManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;

public class PaintingTooltipComponent implements TooltipComponent {
    private final PaintingVariant variant;
    private final int width;
    private final int height;
    private final EnhancedTooltipsConfig config;

    public PaintingTooltipComponent(ItemStack stack) {
        this.config = EnhancedTooltipsConfig.load();

        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, null);
        PaintingEntity painting = EntityType.PAINTING.create(MinecraftClient.getInstance().world/*? if >1.21.1 {*/, SpawnReason.SPAWN_ITEM_USE/*?}*/);
        if (nbtComponent != null && painting != null) {
            painting.readNbt(nbtComponent.copyNbt());

            this.variant = painting.getVariant().value();
            this.width = variant.width() * 25;
            this.height = variant.height() * 25;
        } else {
            this.variant = null;
            this.width = 0;
            this.height = 0;
        }
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        if (!config.paintingTooltip) return 0;
        return this.height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (!config.paintingTooltip) return 0;
        return this.width;
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        if (this.variant == null || !config.paintingTooltip) return;
        PaintingManager paintingManager = MinecraftClient.getInstance().getPaintingManager();
        Sprite sprite = paintingManager.getPaintingSprite(this.variant);
        //? if >1.21.1 {
        context.drawSpriteStretched(RenderLayer::getGuiTextured, sprite, x, y, this.width, this.height);
        //?} else {
        /*context.drawSprite(x, y, 0, this.width, this.height, sprite);
        *///?}
    }
}
