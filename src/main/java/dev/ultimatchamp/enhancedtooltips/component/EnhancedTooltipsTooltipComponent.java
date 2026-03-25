package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.NotNull;

public interface EnhancedTooltipsTooltipComponent extends ClientTooltipComponent {
    default int height() {
        return 0;
    }

    @Override
    default int getHeight(/*? if >1.21.1 {*/@NotNull Font font/*?}*/) {
        return height();
    }

    //? if >1.21.5 {
    default void drawText(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y) {
        //? if >1.21.11 {
        ClientTooltipComponent.super.extractText(graphics, font, x, y);
        //?} else {
        /*ClientTooltipComponent.super.renderText(graphics, font, x, y);
        *///?}
    }
    //?}

    //? if >1.21.5 {
    @Override
    //? if >1.21.11 {
    default void extractText(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y) {
    //?} else {
    /*default void renderText(@NotNull GuiGraphicsExtractor graphics, @NotNull Font font, int x, int y) {
    *///?}
        drawText(graphics, font, x, y);
    }
    //?}

    default void drawImage(Font font, int x, int y, int width, int height, GuiGraphicsExtractor graphics) {
        //? if >1.21.11 {
        ClientTooltipComponent.super.extractImage(font, x, y, width, height, graphics);
        //?} else if >1.21.1 {
        /*ClientTooltipComponent.super.renderImage(font, x, y, width, height, graphics);
        *///?} else {
        /*ClientTooltipComponent.super.renderImage(font, x, y, graphics);
        *///?}
    }

    @Override
    //? if >1.21.11 {
    default void extractImage(@NotNull Font font, int x, int y, int width, int height, @NotNull GuiGraphicsExtractor graphics) {
        drawImage(font, x, y, width, height, graphics);
    //?} else if >1.21.1 {
    /*default void renderImage(@NotNull Font font, int x, int y, int width, int height, @NotNull GuiGraphicsExtractor graphics) {
        drawImage(font, x, y, width, height, graphics);
    *///?} else {
    /*default void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphicsExtractor graphics) {
        drawImage(font, x, y, 0, 0, graphics);
    *///?}
    }
}
