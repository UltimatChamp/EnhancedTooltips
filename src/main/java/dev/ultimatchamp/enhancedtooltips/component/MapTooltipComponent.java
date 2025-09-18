package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.util.MatricesUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

/*? if <1.21.6 {*//*import net.minecraft.client.util.math.MatrixStack;*//*?}*/

public record MapTooltipComponent(ItemStack stack) implements TooltipComponent {

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        return 128 + 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 128;
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        /*? if <1.21.6 {*//*VertexConsumerProvider vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();*//*?}*/

        //? if >1.21.1 {
        MapRenderer mapRenderer = MinecraftClient.getInstance().getMapRenderer();
        //?} else {
        /*MapRenderer mapRenderer = MinecraftClient.getInstance().gameRenderer.getMapRenderer();
        *///?}

        /*? if >1.21.1 {*/MapRenderState renderState = new MapRenderState();/*?}*/
        MapIdComponent mapId = stack.get(DataComponentTypes.MAP_ID);

        MapState mapState = FilledMapItem.getMapState(this.stack, MinecraftClient.getInstance().world);
        if (mapState == null) return;

        MatricesUtil matrices = new MatricesUtil(context.getMatrices());

        matrices.pushMatrix();
        matrices.trans(x, y, 0);
        matrices.scal(1, 1, 0);
        //? if >1.21.1 {
        mapRenderer.update(mapId, mapState, renderState);
        //?} else {
        /*mapRenderer.updateTexture(mapId, mapState);
        *///?}
        //? if >1.21.5 {
        context.drawMap(renderState);
        //?} else {
        /*mapRenderer.draw(/^? if >1.21.1 {^/renderState, /^?}^/context.getMatrices(), vertexConsumers, /^? if 1.21.1 {^//^mapId, mapState, ^//^?}^/false, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        *///?}
        matrices.popMatrix();
    }
}
