package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//? if >1.21.10 {
import net.minecraft.client.model.object.banner.BannerFlagModel;
//?} else if >1.21.8 {
/*import net.minecraft.client.model.BannerFlagModel;
*///?}

//? if <1.21.6 {
/*import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
*///?}
/*? if <1.21.5 {*//*import net.minecraft.world.item.BannerPatternItem;*//*?}*/
//? if <1.21.6 {
/*import net.minecraft.client.resources.model.ModelBakery;
*///?}

public record BannerPatternTooltipComponent(ItemStack stack) implements EnhancedTooltipsTooltipComponent {
    private TagKey<@NotNull BannerPattern> getBannerPatternComponent() {
        //? if >1.21.11 {
        return stack.get(DataComponents.PROVIDES_BANNER_PATTERNS).unwrapKey().get();
        //?} else if >1.21.4 {
        /*return stack.get(DataComponents.PROVIDES_BANNER_PATTERNS);
        *///?} else {
        /*return ((BannerPatternItem) stack.getItem()).getBannerPattern();
        *///?}
    }

    @Override
    public int height() {
        return 45;
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        return 20;
    }

    @Override
    public void drawImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphicsExtractor context) {
        var c = getBannerPatternComponent();
        var world = Minecraft.getInstance().level;

        if (world == null) return;

        //? if >1.21.1 {
        world.registryAccess().lookup(Registries.BANNER_PATTERN).flatMap(registry -> registry.getRandomElementOf(c, RandomSource.create())).ifPresent(entry -> {
        //?} else {
        /*world.registryAccess().registry(Registries.BANNER_PATTERN).flatMap(registry -> registry.getRandomElementOf(c, RandomSource.create())).ifPresent(entry -> {
        *///?}
            var patterns = new BannerPatternLayers(List.of(new BannerPatternLayers.Layer(entry, DyeColor.WHITE)));

            //? if >1.21.8 {
            var modelPart = new BannerFlagModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG));
            //?} else if >1.21.3 {
            /*var modelPart = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG).getChild("flag");
            *///?} else {
            /*var modelPart = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag");
            *///?}

            //? if >1.21.11 {
            context.bannerPattern(modelPart, DyeColor.GRAY, patterns, x, y, x + 20, y + 40);
            //?} else if >1.21.5 {
            /*context.submitBannerPatternRenderState(modelPart, DyeColor.GRAY, patterns, x, y, x + 20, y + 40);
            *///?} else {
            /*Lighting.setupForEntityInInventory();
            context.pose().pushPose();;
            context.pose().translate(x, y + 56 /^? if <1.21.5 {^//^- 10^//^?}^/, 0);
            context.pose().scale(24, 24, 1);
            context.pose().translate(0.5, -0.5, 0.5);
            float f = 0.6666667f;
            context.pose().scale(f, f, -f);
            modelPart.xRot = 0;
            /^? if <1.21.5 {^//^modelPart.xRot = -32;^//^?}^/
            BannerRenderer.renderPatterns(context.pose(), Minecraft.getInstance().renderBuffers().bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, modelPart, ModelBakery.BANNER_BASE, true, DyeColor.GRAY, patterns);
            context.pose().popPose();
            context.flush();
            Lighting.setupForFlatItems();
            *///?}
        });
    }
}
