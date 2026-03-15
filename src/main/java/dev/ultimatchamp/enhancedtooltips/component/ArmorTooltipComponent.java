package dev.ultimatchamp.enhancedtooltips.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

//? if >1.21.5 {
/*import net.minecraft.client.renderer.RenderPipelines;
*///?} else {
import net.minecraft.client.renderer.RenderType;
//?}
//? if 1.21.1 {
/*import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
*///?}

public record ArmorTooltipComponent(ItemStack stack) implements ClientTooltipComponent {
    @Override
    public int getHeight(/*? if >1.21.1 {*/@NotNull Font textRenderer/*?}*/) {
        int height = 0;

        //? if >1.21.1 {
        if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(Minecraft.getInstance().level).getEquipmentSlotForItem(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        *///?}
            var c = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
            if (c != null) {
                //? if >1.21.1 {
                var opt = c.modifiers().stream().filter(i -> i.attribute().is(Attributes.ARMOR)).findAny();
                if (opt.isPresent() && opt.get().modifier().amount() > 0) height = 9;
                //?} else {
                /*if (stack.getItem() instanceof ArmorItem armor && armor.getDefense() > 0) height = 9;
                *///?}
            }
        }

        return height;
    }

    @Override
    public int getWidth(@NotNull Font textRenderer) {
        int width = 0;

        //? if >1.21.1 {
        if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(Minecraft.getInstance().level).getEquipmentSlotForItem(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        *///?}
            var c = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
            if (c != null) {
                //? if >1.21.1 {
                var opt = c.modifiers().stream().filter(i -> i.attribute().is(Attributes.ARMOR)).findAny();
                if (opt.isEmpty() || opt.get().modifier().amount() < 0) return 0;
                int prot = (int) opt.get().modifier().amount();
                //?} else {
                /*if (!(stack.getItem() instanceof ArmorItem armor) || armor.getDefense() < 0) return 0;
                int prot = armor.getDefense();
                *///?}

                width += prot / 2 * 9;
            }
        }

        return width;
    }

    @Override
    //? if >1.21.1 {
    @SuppressWarnings("deprecation")
    public void renderImage(@NotNull Font textRenderer, int x, int y, int width, int height, @NotNull GuiGraphics context) {
    //?} else {
    /*public void renderImage(Font textRenderer, int x, int y, GuiGraphics context) {
    *///?}
        //? if >1.21.1 {
        if (ModelViewerTooltipComponent.getEquipmentSlot(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        //?} else {
        /*if (EntityType.ARMOR_STAND.create(Minecraft.getInstance().level).getEquipmentSlotForItem(stack).getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
        *///?}
            var c = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
            if (c != null) {
                //? if >1.21.1 {
                var opt = c.modifiers().stream().filter(i -> i.attribute().is(Attributes.ARMOR)).findAny();
                if (opt.isEmpty()) return;

                int prot = (int) opt.get().modifier().amount();
                //?} else {
                /*if (!(stack.getItem() instanceof ArmorItem armor)) return;
                int prot = armor.getDefense();
                *///?}

                for (int j = 0; j < prot / 2; j++) {
                    //? if >1.21.5 {
                    /*context.blitSprite(RenderPipelines.GUI_TEXTURED, ResourceLocation.withDefaultNamespace("hud/armor_full"), x + j * 9, y, textRenderer.lineHeight, textRenderer.lineHeight);
                    *///?} else if >1.21.1 {
                    context.blitSprite(RenderType::guiTextured, ResourceLocation.withDefaultNamespace("hud/armor_full"), textRenderer.lineHeight, textRenderer.lineHeight, 0, 0, x + j * 9, y, textRenderer.lineHeight, textRenderer.lineHeight);
                    //?} else {
                    /*context.blitSprite(ResourceLocation.withDefaultNamespace("hud/armor_full"), x + j * 9, y, textRenderer.lineHeight, textRenderer.lineHeight);
                    *///?}
                }

                if (prot % 2 == 1) {
                    //? if >1.21.5 {
                    /*context.blitSprite(RenderPipelines.GUI_TEXTURED, ResourceLocation.withDefaultNamespace("hud/armor_half"), x + prot / 2 * 9, y, textRenderer.lineHeight, textRenderer.lineHeight);
                    *///?} else if >1.21.1 {
                    context.blitSprite(RenderType::guiTextured, ResourceLocation.withDefaultNamespace("hud/armor_half"), textRenderer.lineHeight, textRenderer.lineHeight, 0, 0, x + prot / 2 * 9, y, textRenderer.lineHeight, textRenderer.lineHeight);
                    //?} else {
                    /*context.blitSprite(ResourceLocation.withDefaultNamespace("hud/armor_half"), x + prot / 2 * 9, y, textRenderer.lineHeight, textRenderer.lineHeight);
                    *///?}
                }
            }
        }
    }
}
