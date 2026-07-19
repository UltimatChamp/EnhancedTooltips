package dev.ultimatchamp.enhancedtooltips.tooltip;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.component.TooltipBackgroundComponent;
import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.mixin.accessors.ClientTextTooltipAccessor;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import dev.ultimatchamp.enhancedtooltips.util.MatricesUtil;
import dev.ultimatchamp.enhancedtooltips.util.PositionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.List;

/*? if <1.21.6 {*//*import dev.ultimatchamp.enhancedtooltips.mixin.accessors.DrawContextAccessor;*//*?}*/

public class EnhancedTooltipsDrawer {
    private static final int EDGE_SPACING = 32;
    private static final int PAGE_SPACING = 12;
    private static long startTime = -1;
    private static ItemStack lastStack = ItemStack.EMPTY;

    private static int getMaxHeight() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() - EDGE_SPACING * 2;
    }

    private static int getMaxWidth() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 - EDGE_SPACING;
    }

    public static void drawTooltip(GuiGraphicsExtractor context, Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, ItemStack currentStack) {
        if (components == null || components.isEmpty() || currentStack.isEmpty()) {
            startTime = -1;
            lastStack = ItemStack.EMPTY;
        }

        if (components == null || components.isEmpty()) return;

        if (!currentStack.isEmpty()) {
            if (lastStack.isEmpty() || !ItemStack.matches(lastStack, currentStack)) {
                startTime = System.nanoTime();
                lastStack = currentStack.copy();
            } else if (startTime == -1) {
                startTime = System.nanoTime();
            }
        }

        TooltipBackgroundComponent backgroundComponent = getBackgroundComponent(components);

        if (components.size() > 1 && components.get(1).getWidth(textRenderer) == 0)
            components.remove(1);

        if (EnhancedTooltipsConfig.load().general.removeAllSpacing)
            components.removeIf(component -> component.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/) == 0 || component.getWidth(textRenderer) == 0);

        MatricesUtil matrices = new MatricesUtil(context.pose());

        float scale = 1;
        /*? if <1.21.6 {*//*scale = EnhancedTooltipsConfig.load().general.scaleFactor;*//*?}*/

        int maxWidth = (int) (getMaxWidth() / scale);
        int maxHeight = (int) (getMaxHeight() / scale);

        int spacing = components.size() > 1 ? 4 : 0;

        List<ClientTooltipComponent> lines = new ArrayList<>();
        for (ClientTooltipComponent tooltipComponent : components) {
            if (tooltipComponent.getWidth(textRenderer) > maxWidth) {
                lines.addAll(wrapComponent(tooltipComponent, textRenderer, maxWidth));
            } else {
                lines.add(tooltipComponent);
            }
        }

        List<TooltipPage> pageList = new ArrayList<>();
        TooltipPage page = new TooltipPage();
        int pageHeight = -2 + spacing;

        for (ClientTooltipComponent line : lines) {
            int width = line.getWidth(textRenderer);
            int height = line.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);

            if (pageHeight + height > maxHeight && !page.components.isEmpty()) {
                pageList.add(page);
                page = new TooltipPage();
                pageHeight = -2;
            }

            page.components.add(line);
            page.height = pageHeight += height;
            page.width = Math.max(page.width, width);
        }

        if (!page.components.isEmpty()) {
            pageList.add(page);
        }

        if (pageList.isEmpty()) return;

        TooltipPage firstPage = pageList.getFirst();

        int scaledOffset = ((int) (12 * scale)) - 12;
        Vector2ic vector2ic = positioner.positionTooltip(context.guiWidth(), context.guiHeight(), x + scaledOffset, y - scaledOffset, (int) (firstPage.width * scale), (int) (firstPage.height * scale));

        firstPage.x = vector2ic.x();
        firstPage.y = (pageList.size() > 1) ? vector2ic.y() - EDGE_SPACING : vector2ic.y() - 6;

        PositionUtil.Bounds placed = new PositionUtil.Bounds(firstPage.x, firstPage.y, (int) (firstPage.width * scale), (int) (firstPage.height * scale));

        for (TooltipPage tooltipPage : pageList.subList(1, pageList.size())) {
            PositionUtil.Bounds bounds = PositionUtil.clampToScreen(placed, (int) (tooltipPage.width * scale), (int) (tooltipPage.height * scale), PAGE_SPACING, PositionUtil.Side.RIGHT);

            tooltipPage.x = bounds.x();
            tooltipPage.y = bounds.y();
            placed = placed.union(bounds);
        }

        matrices.pushMatrix();

        if (!currentStack.isEmpty() && EnhancedTooltipsConfig.load().popUpAnimation.enabled) {
            matrices.trans(x, y, 0);

            float sec = EnhancedTooltipsConfig.load().popUpAnimation.time * 1000;
            float elapsedTime = ((float) (System.nanoTime() - startTime) / 1_000_000) / sec;

            float pop = 1.0f;
            if (elapsedTime < 0.5f) {
                pop = 1.0f + Math.abs((float) Math.sin(elapsedTime * Math.PI * 2)) * ((EnhancedTooltipsConfig.load().popUpAnimation.magnitude / 10) * scale);
            }

            matrices.scal(pop, pop, 1);
            matrices.trans(-x, -y, 0);
        }

        matrices.scal(scale, scale, 1);

        for (int i = 0; i < pageList.size(); i++) {
            TooltipPage p = pageList.get(i);
            p.x = (int) (p.x / scale);
            p.y = (int) (p.y / scale);

            if (backgroundComponent == null) {
                //? if >1.21.11 {
                TooltipRenderUtil.extractTooltipBackground(
                //?} else {
                /*TooltipRenderUtil.renderTooltipBackground(
                *///?}
                        context, p.x, p.y, p.width, p.height/*? if <=1.21.5 {*//*, 400*//*?}*//*? if >1.21.1 {*/, Identifier.withDefaultNamespace("tooltip/background")/*?}*/);
            } else {
                try {
                    backgroundComponent.render(context, p.x, p.y, p.width, p.height, 400, i);
                } catch (Exception e) {
                    EnhancedTooltips.LOGGER.error("[{}]", EnhancedTooltips.MOD_NAME, e);
                }
            }
        }

        matrices.trans(0.0f, 0.0f, 400.0f);

        for (int i = 0; i < pageList.size(); i++) {
            TooltipPage p = pageList.get(i);
            int cx = p.x;
            int cy = p.y;

            for (int j = 0; j < p.components.size(); j++) {
                ClientTooltipComponent component = p.components.get(j);

                try {
                    //? if >1.21.11 {
                    component.extractText(context, textRenderer, cx, cy);
                    //?} else if >1.21.5 {
                    /*component.renderText(context, textRenderer, cx, cy);
                    *///?} else {
                    /*component.renderText(textRenderer, cx, cy, context.pose().last().pose(), ((DrawContextAccessor) context).getBufferSource());
                    *///?}
                    //? if >1.21.11 {
                    component.extractImage(textRenderer, cx, cy, p.width, p.height, context);
                    //?} else {
                    /*component.renderImage(textRenderer, cx, cy, /^? if >1.21.1 {^/p.width, p.height,/^?}^/ context);
                    *///?}
                } catch (Exception e) {
                    EnhancedTooltips.LOGGER.error("[{}]", EnhancedTooltips.MOD_NAME, e);
                }

                cy += component.getHeight(/*? if >1.21.1 {*/textRenderer/*?}*/);

                if (i == 0 && j == 0 && spacing > 0) {
                    cy += spacing;
                }
            }
        }

        matrices.popMatrix();
    }

    private static TooltipBackgroundComponent getBackgroundComponent(List<ClientTooltipComponent> components) {
        for (ClientTooltipComponent component : components) {
            if (component instanceof TooltipBackgroundComponent bgComponent) {
                return bgComponent;
            }
        }

        return null;
    }

    private static List<ClientTooltipComponent> wrapComponent(ClientTooltipComponent component, Font textRenderer, int maxWidth) {
        List<ClientTooltipComponent> wrappedComponents = new ArrayList<>();

        if (component instanceof ClientTextTooltipAccessor orderedTextTooltipComponent) {
            Component text = EnhancedTooltipsTextVisitor.get(orderedTextTooltipComponent.getText());

            List<FormattedCharSequence> lines = textRenderer.split(text, maxWidth);
            for (FormattedCharSequence line : lines) {
                wrappedComponents.add(ClientTooltipComponent.create(line));
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
        private final List<ClientTooltipComponent> components;

        private TooltipPage() {
            this(0, 0, 0, 0, new ArrayList<>());
        }

        private TooltipPage(int x, int y, int width, int height, List<ClientTooltipComponent> components) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.components = components;
        }
    }
}
