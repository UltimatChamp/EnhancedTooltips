package dev.ultimatchamp.enhancedtooltips.mixin;

import dev.ultimatchamp.enhancedtooltips.mixin.accessors.ClientTextTooltipAccessor;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipComponentManager;
import dev.ultimatchamp.enhancedtooltips.tooltip.EnhancedTooltipsDrawer;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipHelper;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipItemStackCache;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

//? if >1.21.1 {
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
//?}

@Mixin(GuiGraphicsExtractor.class)
public class DrawContextMixin {
    //? if >1.21.11 {
    //? if fabric {
    @Inject(method = "tooltip", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo ci) {
    //?} else {
    /*@Inject(method = "tooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Lnet/minecraft/resources/Identifier;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
        private void enhancedTooltips$drawTooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, ItemStack stack, CallbackInfo ci) {
    *///?}
    //?} else if >1.21.5 {
    /*//? if fabric {
    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo ci) {
    //?} else {
    /^@Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Lnet/minecraft/resources/Identifier;Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, ItemStack stack, CallbackInfo ci) {
    ^///?}
    *///?} else if >1.21.1 {
    /*@Inject(method = "renderTooltipInternal", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, @Nullable Identifier style, CallbackInfo ci) {
    *///?} else {
    /*@Inject(method = "renderTooltipInternal", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(Font font, List<ClientTooltipComponent> lines, int xo, int yo, ClientTooltipPositioner positioner, CallbackInfo ci) {
    *///?}
        if (lines == null || lines.isEmpty()) return;

        List<ClientTooltipComponent> tooltipComponents = new ArrayList<>(lines);
        ItemStack cacheItemStack =
                //? if <=1.21.5 || !neoforge {
                TooltipItemStackCache.getItemStack();
                //?} else {
                /*stack;
                *///?}

        if (!cacheItemStack.isEmpty() && lines.getFirst() instanceof ClientTextTooltipAccessor ordered) {
            if (!EnhancedTooltipsTextVisitor.styledEquals(ordered.getText(), TooltipHelper.getDisplayName(cacheItemStack).getVisualOrderText())) {
                cacheItemStack = ItemStack.EMPTY;
            }
        }

        //? if <=1.21.5 || !neoforge {
        TooltipItemStackCache.saveItemStack(ItemStack.EMPTY);
        //?}
        TooltipComponentManager.invoke(tooltipComponents, cacheItemStack);

        EnhancedTooltipsDrawer.drawTooltip((GuiGraphicsExtractor) (Object) this, font, tooltipComponents, xo, yo, positioner, cacheItemStack);
        ci.cancel();
    }
}
