package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.util.MatricesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;

//? if <26.1 {
/*import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
*///?}

//? if >1.21.1 {
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.state.MapRenderState;
//?} else {
/*import net.minecraft.client.gui.MapRenderer;
*///?}

public record MapTooltipComponent(ItemStack stack) implements EnhancedTooltipsTooltipComponent {
    @Override
    public int getHeight(/*? if >1.21.1 {*/@NotNull Font textRenderer/*?}*/) {
        return 128 + 2;
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        return 128;
    }

    @Override
    public void drawImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphicsExtractor context) {
        /*? if <1.21.6 {*//*MultiBufferSource.BufferSource vertexConsumers = Minecraft.getInstance().renderBuffers().bufferSource();*//*?}*/

        //? if >1.21.1 {
        MapRenderer mapRenderer = Minecraft.getInstance().getMapRenderer();
        //?} else {
        /*MapRenderer mapRenderer = Minecraft.getInstance().gameRenderer.getMapRenderer();
        *///?}

        /*? if >1.21.1 {*/MapRenderState renderState = new MapRenderState();/*?}*/
        MapId mapId = stack.get(DataComponents.MAP_ID);

        MapItemSavedData mapState = MapItem.getSavedData(this.stack, Minecraft.getInstance().level);
        if (mapState == null) return;

        MatricesUtil matrices = new MatricesUtil(context.pose());

        matrices.pushMatrix();
        matrices.trans(x, y, 0);
        matrices.scal(1, 1, 0);
        //? if >1.21.1 {
        mapRenderer.extractRenderState(mapId, mapState, renderState);
        //?} else {
        /*mapRenderer.update(mapId, mapState);
        *///?}
        //? if >1.21.11 {
        context.map(renderState);
        //?} else if >1.21.5 {
        /*context.submitMapRenderState(renderState);
        *///?} else {
        /*mapRenderer.render(/^? if >1.21.1 {^/renderState, /^?}^/context.pose(), vertexConsumers, /^? if 1.21.1 {^//^mapId, mapState, ^//^?}^/false, LightTexture.FULL_BRIGHT);
        *///?}
        matrices.popMatrix();
    }
}
