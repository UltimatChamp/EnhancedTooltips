package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

//? if >1.21.5 {
import net.minecraft.client.gl.RenderPipelines;
//?} else {
/*import net.minecraft.client.render.RenderLayer;
*///?}

public record ArmorTooltipComponent(ItemStack stack) implements TooltipComponent {

    @Override
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        int height = 0;

        //? if >1.21.1 {
        if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(MinecraftClient.getInstance().world).getPreferredEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        *///?}
            var c = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
            if (c != null) {
                for (var i : c.modifiers()) {
                    if (i.attribute().matches(/*? if >1.21.1 {*/EntityAttributes.ARMOR/*?} else {*//*EntityAttributes.GENERIC_ARMOR*//*?}*/)) {
                        height = 9;
                        break;
                    }
                }
            }
        }

        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int width = 0;

        //? if >1.21.1 {
        if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            //?} else {
            /*if (EntityType.ARMOR_STAND.create(MinecraftClient.getInstance().world).getPreferredEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
            *///?}
            var c = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
            if (c != null) {
                for (var i : c.modifiers()) {
                    if (i.attribute().matches(/*? if >1.21.1 {*/EntityAttributes.ARMOR/*?} else {*//*EntityAttributes.GENERIC_ARMOR*//*?}*/)) {
                        width += (int) i.modifier().value() / 2 * 9;
                        break;
                    }
                }
            }
        }

        return width;
    }

    @Override
    //? if >1.21.1 {
    public void drawItems(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context) {
    //?} else {
    /*public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
    *///?}
        //? if >1.21.1 {
        if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(MinecraftClient.getInstance().world).getPreferredEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        *///?}
            var c = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
            if (c != null) {
                for (var i : c.modifiers()) {
                    if (i.attribute().matches(/*? if >1.21.1 {*/EntityAttributes.ARMOR/*?} else {*//*EntityAttributes.GENERIC_ARMOR*//*?}*/)) {
                        for (int j = 0; j < i.modifier().value() / 2; j++) {
                            //? if >1.21.5 {
                            context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/armor_full"), x + j * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                             //?} else if >1.21.1 {
                            /*context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("hud/armor_full"), textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + j * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                             *///?} else {
                            /*context.drawGuiTexture(Identifier.ofVanilla("hud/armor_full"), x + j * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                            *///?}
                        }
                    }
                    if (i.modifier().value() % 2 == 1) {
                        //? if >1.21.5 {
                        context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/armor_half"), x + (int) i.modifier().value() / 2 * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                        //?} else if >1.21.1 {
                        /*context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("hud/armor_half"), textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + (int) i.modifier().value() / 2 * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                        *///?} else {
                        /*context.drawGuiTexture(Identifier.ofVanilla("hud/armor_half"), x + (int) i.modifier().value() / 2 * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                        *///?}
                    }
                    break;
                }
            }
        }
    }
}
