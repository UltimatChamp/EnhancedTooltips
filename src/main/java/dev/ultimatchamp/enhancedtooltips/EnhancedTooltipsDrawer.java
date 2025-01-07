package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.TooltipBackgroundComponent;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipDrawerProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.List;

public class EnhancedTooltipsDrawer implements TooltipDrawerProvider.ITooltipDrawer {
    private static final int EDGE_SPACING = 32;
    private static final int PAGE_SPACING = 12;

    private static int getLimitMaxHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight() - EDGE_SPACING * 2;
    }

    @Override
    public void drawTooltip(DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner) {
        MatrixStack matrices = context.getMatrices();
        List<TooltipPage> pageList = new ArrayList<>();
        TooltipBackgroundComponent backgroundComponent = getBackgroundComponent(components);
        if (components.isEmpty()) {
            return;
        }
        if (backgroundComponent != null) {
            components.remove(backgroundComponent);
        }

        int pageWidth = 0;
        int pageHeight = -2;
        int maxHeight = getLimitMaxHeight();
        int totalWidth = 0;

        int spacing = components.size() > 1 ? 4 : 0;
        pageHeight += spacing;

        TooltipPage page = new TooltipPage();

        for (int j = 0; j < components.size(); j++) {
            TooltipComponent tooltipComponent = components.get(j);
            int width = tooltipComponent.getWidth(textRenderer);
            if (width > pageWidth) {
                pageWidth = width;
            }
            pageHeight += tooltipComponent.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);
            if (pageHeight > maxHeight) {
                pageList.add(page);
                totalWidth += page.width;
                page = new TooltipPage();
                page.components.add(tooltipComponent);
                page.height = tooltipComponent.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);
                pageHeight = tooltipComponent.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);
                if (j == components.size() - 1) {
                    page.width = tooltipComponent.getWidth(textRenderer);
                    pageList.add(page);
                    totalWidth += page.width;
                }
            } else if (j == components.size() - 1) {
                page.height = pageHeight;
                page.width = pageWidth;
                page.components.add(tooltipComponent);
                pageList.add(page);
                totalWidth += page.width;
            } else {
                page.height = pageHeight;
                page.width = pageWidth;
                page.components.add(tooltipComponent);
            }
        }

        Vector2ic vector2ic = positioner.getPosition(context.getScaledWindowWidth(), context.getScaledWindowHeight(), x, y, totalWidth, pageList.get(0).height);
        int n = vector2ic.x();
        int o = vector2ic.y();
        for (TooltipPage tooltipPage : pageList) {
            tooltipPage.x = n;
            tooltipPage.y = (pageList.size() > 1) ? o - EDGE_SPACING : o - 6;
            n += tooltipPage.width + PAGE_SPACING;
        }

        matrices.push();

        for (TooltipPage p : pageList) {
            if (backgroundComponent == null) {
            //? if >1.21.1 {
                context.draw(vertexConsumerProvider -> TooltipBackgroundRenderer.render(context, p.x, p.y, p.width, p.height, 400, Identifier.ofVanilla("tooltip/background")));
            } else {
                context.draw(vertexConsumerProvider -> {
            //?} else {
                /*context.draw(() -> TooltipBackgroundRenderer.render(context, p.x, p.y, p.width, p.height, 400/^? if >1.21.1 {^/, Identifier.ofVanilla("tooltip/background")/^?}^/));
            } else {
                context.draw(() -> {
            *///?}
                    try {
                        backgroundComponent.render(context, p.x, p.y, p.width, p.height, 400, pageList.indexOf(p));
                    } catch (Exception e) {
                        EnhancedTooltips.LOGGER.error("[{}]", EnhancedTooltips.MOD_ID, e);
                    }
                });
            }
        }

        matrices.translate(0.0f, 0.0f, 400.0f);

        for (TooltipPage p : pageList) {
            int cx = p.x;
            int cy = p.y;

            for (TooltipComponent component : p.components) {
                try {
                    component.drawText(textRenderer, cx, cy, matrices.peek().getPositionMatrix(), context.vertexConsumers);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                component.drawItems(textRenderer, cx, cy, /*? if >1.21.1 {*/pageWidth, pageHeight,/*?}*/ context);
                cy += component.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);
                if (p == pageList.get(0) && component == p.components.get(0)) {
                    cy += spacing;
                }
            }
        }

        matrices.pop();
    }

    @Nullable
    private TooltipBackgroundComponent getBackgroundComponent(List<TooltipComponent> components) {
        for (TooltipComponent component : components) {
            if (component instanceof TooltipBackgroundComponent) {
                return (TooltipBackgroundComponent) component;
            }
        }
        return null;
    }

    private static class TooltipPage {
        private int x;
        private int y;
        private int width;
        private int height;
        private final List<TooltipComponent> components;

        private TooltipPage() {
            this(0, 0, 0, 0, new ArrayList<>());
        }

        private TooltipPage(int x, int y, int width, int height, List<TooltipComponent> components) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.components = components;
        }
    }
}
