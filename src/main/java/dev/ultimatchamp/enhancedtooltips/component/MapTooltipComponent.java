package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

/*? if >1.21.1 {*/import net.minecraft.client.render.MapRenderState;/*?}*/

public class MapTooltipComponent implements TooltipComponent {
    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;

    public MapTooltipComponent(ItemStack stack) {
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
    }

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        if (!config.mapTooltip) return 0;
        return 128 + 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        if (!config.mapTooltip) return 0;
        return 128;
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        if (!config.mapTooltip) return;
        VertexConsumerProvider vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        //? if >1.21.1 {
        MapRenderer mapRenderer = MinecraftClient.getInstance().getMapRenderer();
        //?} else {
        /*MapRenderer mapRenderer = MinecraftClient.getInstance().gameRenderer.getMapRenderer();
        *///?}

        /*? if >1.21.1 {*/MapRenderState renderState = new MapRenderState();/*?}*/
        MapIdComponent mapId = stack.get(DataComponentTypes.MAP_ID);

        MapState mapState = FilledMapItem.getMapState(this.stack, MinecraftClient.getInstance().world);
        if (mapState == null) return;

        MatrixStack matrices = context.getMatrices();

        matrices.push();
        matrices.translate(x, y, 0);
        matrices.scale(1, 1, 0);
        //? if >1.21.1 {
        mapRenderer.update(mapId, mapState, renderState);
        //?} else {
        /*mapRenderer.updateTexture(mapId, mapState);
        *///?}
        mapRenderer.draw(/*? if >1.21.1 {*/renderState, /*?}*/matrices, vertexConsumers, /*? if 1.21.1 {*//*mapId, mapState, *//*?}*/false, LightmapTextureManager.MAX_LIGHT_COORDINATE);
        matrices.pop();
    }
}
