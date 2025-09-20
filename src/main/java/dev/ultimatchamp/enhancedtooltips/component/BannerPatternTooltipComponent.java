package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.random.Random;

import java.util.List;

/*? if >1.21.8 {*/import net.minecraft.client.render.block.entity.model.BannerFlagBlockModel;/*?}*/
//? if <1.21.6 {
/*import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
*///?}
/*? if <1.21.5 {*//*import net.minecraft.item.BannerPatternItem;*//*?}*/
//? if <1.21.6 && >1.21.1 {
/*import net.minecraft.client.render.model.ModelBaker;
*///?} else if 1.21.1 {
/*import net.minecraft.client.render.model.ModelLoader;
*///?}

public record BannerPatternTooltipComponent(ItemStack stack) implements TooltipComponent {
    private TagKey<BannerPattern> getBannerPatternComponent() {
        //? if >1.21.4 {
        return stack.get(DataComponentTypes.PROVIDES_BANNER_PATTERNS);
        //?} else {
        /*return ((BannerPatternItem) stack.getItem()).getPattern();
        *///?}
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        return 45;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 20;
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        var c = getBannerPatternComponent();
        var world = MinecraftClient.getInstance().world;

        if (c == null || world == null) return;

        world.getRegistryManager().getOptional(RegistryKeys.BANNER_PATTERN).flatMap(registry -> registry.getRandomEntry(c, Random.create())).ifPresent(entry -> {
            var patterns = new BannerPatternsComponent(List.of(new BannerPatternsComponent.Layer(entry, DyeColor.WHITE)));

            //? if >1.21.8 {
            var modelPart = new BannerFlagBlockModel(MinecraftClient.getInstance().getLoadedEntityModels().getModelPart(EntityModelLayers.STANDING_BANNER_FLAG));
            //?} else if >1.21.3 {
            /*var modelPart = MinecraftClient.getInstance().getLoadedEntityModels().getModelPart(EntityModelLayers.STANDING_BANNER_FLAG);
            if (modelPart == null) return;
            *///?} else {
            /*var modelPart = MinecraftClient.getInstance().getEntityModelLoader().getModelPart(EntityModelLayers.BANNER);
            if (modelPart == null) return;
            *///?}

            //? if >1.21.5 {
            context.addBannerResult(modelPart, DyeColor.GRAY, patterns, x, y, x + 20, y + 40);
            //?} else {
            /*MatrixStack matrixStack = new MatrixStack();
            matrixStack.push();
            matrixStack.translate((float) x + 0.5, (float) (y + 16), 0);
            matrixStack.scale(6, -6, 1);
            matrixStack.translate(0.5, 0.5, 0);
            matrixStack.translate(0.5, 0.5, 0.5);
            float f = 0.6666667f;
            matrixStack.scale(f, f, f);
            modelPart.pitch = 0;
            /^? if <1.21.5 {^//^modelPart.pivotY = -32;^//^?}^/
            BannerPatternsComponent bannerPatternsComponent = (new BannerPatternsComponent.Builder()).add(entry, DyeColor.WHITE).build();
            BannerBlockEntityRenderer.renderCanvas(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV, modelPart, /^? if >1.21.1 {^/ModelBaker/^?} else {^//^ModelLoader^//^?}^/.BANNER_BASE, true, DyeColor.GRAY, bannerPatternsComponent);
            matrixStack.pop();
            context.draw();
            *///?}
        });
    }
}
