package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

//? if >1.21.5 {
import net.minecraft.client.gl.RenderPipelines;
//?} else if <1.21.5 {
/*import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.painting.PaintingEntity;
*///?}

public class PaintingTooltipComponent implements TooltipComponent {
    private final PaintingVariant variant;
    private final int width;
    private final int height;

    public PaintingTooltipComponent(ItemStack stack) {
        //? if >1.21.4 {
        RegistryEntry<PaintingVariant> variant = stack.getOrDefault(DataComponentTypes.PAINTING_VARIANT, null);
        if (variant != null) {
            this.variant = variant.value();
            this.width = variant.value().width() * 25;
            this.height = variant.value().height() * 25;
        //?} else {
        /*NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, null);
        PaintingEntity painting = EntityType.PAINTING.create(MinecraftClient.getInstance().world/^? if >1.21.1 {^/, SpawnReason.SPAWN_ITEM_USE/^?}^/);
        if (nbtComponent != null && painting != null) {
            painting.readNbt(nbtComponent.copyNbt());

            this.variant = painting.getVariant().value();
            this.width = variant.width() * 25;
            this.height = variant.height() * 25;
        *///?}
        } else {
            this.variant = null;
            this.width = 0;
            this.height = 0;
        }
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        return this.height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.width;
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        if (this.variant == null) return;
        //? if >1.21.8 {
        SpriteAtlasTexture paintingAtlas = (SpriteAtlasTexture) MinecraftClient.getInstance()
                .getTextureManager()
                .getTexture(Identifier.ofVanilla("textures/atlas/paintings.png"));
        Sprite sprite = paintingAtlas.getSprite(variant.assetId());
        //?} else {
        /*var paintingManager = MinecraftClient.getInstance().getPaintingManager();
        Sprite sprite = paintingManager.getPaintingSprite(this.variant);
        *///?}
        //? if >1.21.5 {
        context.drawSpriteStretched(RenderPipelines.GUI_TEXTURED, sprite, x, y, this.width, this.height);
        //?} else if >1.21.1 {
        /*context.drawSpriteStretched(RenderLayer::getGuiTextured, sprite, x, y, this.width, this.height);
        *///?} else {
        /*context.drawSprite(x, y, 0, this.width, this.height, sprite);
        *///?}
    }
}
