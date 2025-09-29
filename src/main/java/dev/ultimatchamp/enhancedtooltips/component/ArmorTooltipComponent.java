package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;

//? if >1.21.5 {
import net.minecraft.client.gl.RenderPipelines;
//?} else {
/*import net.minecraft.client.render.RenderLayer;
*///?}
//? if 1.21.1 {
/*import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
*///?}

public record ArmorTooltipComponent(ItemStack stack) implements TooltipComponent {

    @Override
    @SuppressWarnings("deprecation")
    public int getHeight(/*? if >1.21.1 {*/TextRenderer textRenderer/*?}*/) {
        int height = 0;

        //? if >1.21.1 {
        if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(MinecraftClient.getInstance().world).getPreferredEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        *///?}
            var c = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
            if (c != null) {
                //? if >1.21.1 {
                var opt = c.modifiers().stream().filter(i -> i.attribute().matches(EntityAttributes.ARMOR)).findAny();
                if (opt.isPresent() && opt.get().modifier().value() > 0) height = 9;
                //?} else {
                /*if (stack.getItem() instanceof ArmorItem armor && armor.getProtection() > 0) height = 9;
                *///?}
            }
        }

        return height;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getWidth(TextRenderer textRenderer) {
        int width = 0;

        //? if >1.21.1 {
        if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(MinecraftClient.getInstance().world).getPreferredEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        *///?}
            var c = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
            if (c != null) {
                //? if >1.21.1 {
                var opt = c.modifiers().stream().filter(i -> i.attribute().matches(EntityAttributes.ARMOR)).findAny();
                if (opt.isEmpty() || opt.get().modifier().value() < 0) return 0;
                int prot = (int) opt.get().modifier().value();
                //?} else {
                /*if (!(stack.getItem() instanceof ArmorItem armor) || armor.getProtection() < 0) return 0;
                int prot = armor.getProtection();
                *///?}

                width += prot / 2 * 9;
            }
        }

        return width;
    }

    @Override
    //? if >1.21.1 {
    @SuppressWarnings("deprecation")
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
                //? if >1.21.1 {
                var opt = c.modifiers().stream().filter(i -> i.attribute().matches(EntityAttributes.ARMOR)).findAny();
                if (opt.isEmpty()) return;

                int prot = (int) opt.get().modifier().value();
                //?} else {
                /*if (!(stack.getItem() instanceof ArmorItem armor)) return;
                int prot = armor.getProtection();
                *///?}

                for (int j = 0; j < prot / 2; j++) {
                    //? if >1.21.5 {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/armor_full"), x + j * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                    //?} else if >1.21.1 {
                    /*context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("hud/armor_full"), textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + j * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                    *///?} else {
                    /*context.drawGuiTexture(Identifier.ofVanilla("hud/armor_full"), x + j * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                    *///?}
                }

                if (prot % 2 == 1) {
                    //? if >1.21.5 {
                    context.drawGuiTexture(RenderPipelines.GUI_TEXTURED, Identifier.ofVanilla("hud/armor_half"), x + prot / 2 * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                    //?} else if >1.21.1 {
                    /*context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.ofVanilla("hud/armor_half"), textRenderer.fontHeight, textRenderer.fontHeight, 0, 0, x + prot / 2 * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                    *///?} else {
                    /*context.drawGuiTexture(Identifier.ofVanilla("hud/armor_half"), x + prot / 2 * 9, y, textRenderer.fontHeight, textRenderer.fontHeight);
                    *///?}
                }
            }
        }
    }
}
