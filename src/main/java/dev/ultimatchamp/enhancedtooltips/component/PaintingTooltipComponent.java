package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

//? if >1.21.5 {
import net.minecraft.client.renderer.RenderPipelines;
//?} else if <1.21.5 {
/*import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
*///?}
//? if <1.21.6 {
/*import net.minecraft.client.renderer.RenderType;
*///?}

//? if >1.21.1 && <1.21.5 {
/*import net.minecraft.world.entity.EntitySpawnReason;
*///?}

//? if >1.21.10 {
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
//?} else {
/*import net.minecraft.world.entity.decoration.PaintingVariant;
*///?}

public class PaintingTooltipComponent implements EnhancedTooltipsTooltipComponent {
    private final PaintingVariant variant;
    private final int width;
    private final int height;

    public PaintingTooltipComponent(ItemStack stack) {
        //? if >1.21.4 {
        Holder<@NotNull PaintingVariant> variant = stack.get(DataComponents.PAINTING_VARIANT);
        if (variant != null) {
            this.variant = variant.value();
            this.width = variant.value().width() * 25;
            this.height = variant.value().height() * 25;
        //?} else {
        /*CustomData nbtComponent = stack.getOrDefault(DataComponents.ENTITY_DATA, null);
        Painting painting = EntityType.PAINTING.create(Minecraft.getInstance().level/^? if >1.21.1 {^/, EntitySpawnReason.SPAWN_ITEM_USE/^?}^/);
        if (painting != null) {
            painting.load(nbtComponent.copyTag());

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
    public int height() {
        return this.height;
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        return this.width;
    }

    @Override
    public void drawImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphicsExtractor context) {
        if (this.variant == null) return;
        //? if >1.21.8 {
        TextureAtlas paintingAtlas = (TextureAtlas) Minecraft.getInstance()
                .getTextureManager()
                .getTexture(Identifier.withDefaultNamespace("textures/atlas/paintings.png"));
        TextureAtlasSprite sprite = paintingAtlas.getSprite(variant.assetId());
        //?} else {
        /*var paintingManager = Minecraft.getInstance().getPaintingTextures();
        TextureAtlasSprite sprite = paintingManager.get(this.variant);
        *///?}
        //? if >1.21.5 {
        context.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, this.width, this.height);
        //?} else if >1.21.1 {
        /*context.blitSprite(RenderType::guiTextured, sprite, x, y, this.width, this.height);
        *///?} else {
        /*context.blit(x, y, 0, this.width, this.height, sprite);
        *///?}
    }
}
