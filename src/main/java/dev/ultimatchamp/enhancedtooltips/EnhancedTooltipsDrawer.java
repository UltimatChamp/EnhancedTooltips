package dev.ultimatchamp.enhancedtooltips;

import dev.ultimatchamp.enhancedtooltips.component.TooltipBackgroundComponent;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipDrawerProvider;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.List;

public class EnhancedTooltipsDrawer implements TooltipDrawerProvider.ITooltipDrawer {
    private static final int EDGE_SPACING = 32;
    private static final int PAGE_SPACING = 12;

    private static int getMaxHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight() - EDGE_SPACING * 2;
    }

    private static int getMaxWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - EDGE_SPACING;
    }

    @Override
    public void drawTooltip(DrawContext context, TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner) {
        if (components.isEmpty()) return;

        TooltipBackgroundComponent backgroundComponent = getBackgroundComponent(components);

        components.removeIf(component -> component.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/) == 0 || component.getWidth(textRenderer) == 0);

        MatrixStack matrices = context.getMatrices();
        List<TooltipPage> pageList = new ArrayList<>();

        int pageWidth = 0;
        int maxWidth = getMaxWidth();
        int totalWidth = 0;

        int pageHeight = -2;
        int maxHeight = getMaxHeight();

        int spacing = components.size() > 1 ? 4 : 0;
        pageHeight += spacing;

        TooltipPage page = new TooltipPage();

        for (TooltipComponent tooltipComponent : components) {
            int width = tooltipComponent.getWidth(textRenderer);
            int height = tooltipComponent.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);

            if (width > maxWidth) {
                List<TooltipComponent> wrappedComponents = wrapComponent(tooltipComponent, textRenderer, maxWidth);
                for (TooltipComponent wrappedComponent : wrappedComponents) {
                    int wrappedWidth = wrappedComponent.getWidth(textRenderer);
                    int wrappedHeight = wrappedComponent.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);

                    if (pageHeight + wrappedHeight > maxHeight) {
                        pageList.add(page);
                        totalWidth += page.width;
                        page = new TooltipPage();
                        pageHeight = -2;
                    }

                    page.components.add(wrappedComponent);
                    page.height = pageHeight += wrappedHeight;
                    page.width = pageWidth = Math.max(page.width, wrappedWidth);
                }
            } else {
                if (pageHeight + height > maxHeight) {
                    pageList.add(page);
                    totalWidth += page.width;
                    page = new TooltipPage();
                    pageHeight = -2;
                }

                page.components.add(tooltipComponent);
                page.height = pageHeight += height;
                page.width = pageWidth = Math.max(page.width, width);
            }
        }

        if (!page.components.isEmpty()) {
            pageList.add(page);
            totalWidth += page.width;
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
            /*  context.draw(() -> TooltipBackgroundRenderer.render(context, p.x, p.y, p.width, p.height, 400/^? if >1.21.1 {^/, Identifier.ofVanilla("tooltip/background")/^?}^/));
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
                    component.drawItems(textRenderer, cx, cy, /*? if >1.21.1 {*/pageWidth, pageHeight,/*?}*/ context);
                    cy += component.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);

                    if (p == pageList.get(0) && component == p.components.get(0) && components.size() > 1) {
                        cy += spacing;
                    }
                } catch (Exception e) {
                    EnhancedTooltips.LOGGER.error("{}", EnhancedTooltips.MOD_ID, e);
                }
            }
        }

        matrices.pop();
    }

    private TooltipBackgroundComponent getBackgroundComponent(List<TooltipComponent> components) {
        for (TooltipComponent component : components) {
            if (component instanceof TooltipBackgroundComponent) {
                return (TooltipBackgroundComponent) component;
            }
        }

        return null;
    }

    private List<TooltipComponent> wrapComponent(TooltipComponent component, TextRenderer textRenderer, int maxWidth) {
        List<TooltipComponent> wrappedComponents = new ArrayList<>();

        if (component instanceof OrderedTextTooltipComponent orderedTextTooltipComponent) {
            Text text = EnhancedTooltipsTextVisitor.get(orderedTextTooltipComponent.text);

            List<OrderedText> lines = textRenderer.wrapLines(text, maxWidth);
            for (OrderedText line : lines) {
                wrappedComponents.add(TooltipComponent.of(line));
            }
        } else {
            wrappedComponents.add(component);
        }

        return wrappedComponents;
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
