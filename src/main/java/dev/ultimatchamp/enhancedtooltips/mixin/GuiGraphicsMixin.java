package dev.ultimatchamp.enhancedtooltips.mixin;

import dev.ultimatchamp.enhancedtooltips.mixin.accessors.ClientTextTooltipAccessor;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipComponentManager;
import dev.ultimatchamp.enhancedtooltips.tooltip.EnhancedTooltipsDrawer;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipItemStackCache;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

//? if >1.21.1 {
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
//?}

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    //? if >1.21.5 {
    /*//? if fabric {
    /^@Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, @Nullable ResourceLocation texture, CallbackInfo ci) {
    ^///?} else {
    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, @Nullable ResourceLocation texture, ItemStack stack, CallbackInfo ci) {
    //?}
    *///?} else if >1.21.1 {
    @Inject(method = "renderTooltipInternal", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, @Nullable ResourceLocation texture, CallbackInfo ci) {
    //?} else {
    /*@Inject(method = "renderTooltipInternal", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font textRenderer, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci) {
    *///?}
        if (components == null || components.isEmpty()) return;

        List<ClientTooltipComponent> tooltipComponents = new ArrayList<>(components);
        ItemStack cacheItemStack =
                //? if <=1.21.5 || !neoforge {
                TooltipItemStackCache.getItemStack();
                //?} else {
                /*stack;
                *///?}

        boolean isEmpty = cacheItemStack == null || cacheItemStack.isEmpty();
        if (isEmpty) cacheItemStack = ItemStack.EMPTY;

        if (!isEmpty && components.getFirst() instanceof ClientTextTooltipAccessor ordered) {
            Component name = EnhancedTooltipsTextVisitor.get(ordered.getText());
            Component cachedName = EnhancedTooltipsTextVisitor.get(TooltipHelper.getDisplayName(cacheItemStack).getVisualOrderText());

            if (!name.equals(cachedName)) {
                cacheItemStack = ItemStack.EMPTY;
            }
        }

        //? if <=1.21.5 || !neoforge {
        TooltipItemStackCache.saveItemStack(ItemStack.EMPTY);
        //?}
        TooltipComponentManager.invoke(tooltipComponents, cacheItemStack);

        EnhancedTooltipsDrawer.drawTooltip((GuiGraphics) (Object) this, textRenderer, tooltipComponents, x, y, positioner, cacheItemStack);
        ci.cancel();
    }
}
