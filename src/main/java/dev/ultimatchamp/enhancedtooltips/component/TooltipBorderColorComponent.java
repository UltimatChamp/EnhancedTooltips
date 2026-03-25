package dev.ultimatchamp.enhancedtooltips.component;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

//? if >1.21.5 {
import net.minecraft.client.renderer.RenderPipelines;
//?} else if >1.21.1 {
/*import net.minecraft.client.renderer.RenderType;
*///?}

public class TooltipBorderColorComponent extends TooltipBackgroundComponent {
    private final ItemStack stack;
    private final EnhancedTooltipsConfig config;

    public TooltipBorderColorComponent(ItemStack stack) {
        this.stack = stack;
        this.config = EnhancedTooltipsConfig.load();
    }

    @Override
    protected void renderBorder(GuiGraphicsExtractor context, int x, int y, int width, int height, int z, int page) {
        if (config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.RARITY) {
            Identifier renderId = (stack.getRarity() == Rarity.COMMON) ? Identifier.withDefaultNamespace("tooltip/frame") : Identifier.withDefaultNamespace("tooltip/frame/" + stack.getRarity().name().toLowerCase());
            Identifier checkId = Identifier.withDefaultNamespace("textures/gui/sprites/" + renderId.getPath() + ".png");

            if (Minecraft.getInstance().getResourceManager().getResource(checkId).isPresent()) {
                //? if <1.21.6 {
                /*context.pose().pushPose();
                context.pose().translate(0, 0, z);
                *///?}
                context.blitSprite(
                        //? if >1.21.5 {
                        RenderPipelines.GUI_TEXTURED,
                        //?} else if >1.21.1 {
                        /*RenderType::guiTextured,
                        *///?}
                        renderId,
                        x - 9,
                        y - 10,
                        width + 18,
                        height + 18
                );
                /*? if <1.21.6 {*//*context.pose().popPose();*//*?}*/
                return;
            }
        }

        Integer[] color = TooltipHelper.getItemBorderColor(stack);

        int startColor;
        if (color[0] == null || color[0] == -1)
            startColor = EnhancedTooltipsConfig.BorderColor.COMMON.getColor().getRGB();
        else startColor = 0xff000000 | color[0];

        int endColor = EnhancedTooltipsConfig.BorderColor.END_COLOR.getColor().getRGB();
        if (this.config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.ITEM_NAME && color[1] != null)
            endColor = 0xff000000 | color[1];

        if (config.border.borderColor == EnhancedTooltipsConfig.BorderColorMode.CUSTOM) {
            if (stack.getRarity() == Rarity.UNCOMMON)
                startColor = config.border.customBorderColors.uncommon.getRGB();
            else if (stack.getRarity() == Rarity.RARE)
                startColor = config.border.customBorderColors.rare.getRGB();
            else if (stack.getRarity() == Rarity.EPIC)
                startColor = config.border.customBorderColors.epic.getRGB();
            else startColor = config.border.customBorderColors.common.getRGB();

            endColor = config.border.customBorderColors.endColor.getRGB();
        }

        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }
}
