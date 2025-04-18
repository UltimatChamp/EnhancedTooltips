package dev.ultimatchamp.enhancedtooltips.mixin;

import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipComponentManager;
import dev.ultimatchamp.enhancedtooltips.tooltip.EnhancedTooltipsDrawer;
import dev.ultimatchamp.enhancedtooltips.tooltip.TooltipItemStackCache;
import dev.ultimatchamp.enhancedtooltips.util.EnhancedTooltipsTextVisitor;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

//? if >1.21.1 {
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
//?}

@Mixin(DrawContext.class)
public class DrawContextMixin {
    //? if >1.21.1 {
    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo ci) {
    //?} else {
    /*@Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"), cancellable = true)
    private void enhancedTooltips$drawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
    *///?}
        if (components.isEmpty()) return;

        List<TooltipComponent> tooltipComponents = new ArrayList<>(components);
        ItemStack cacheItemStack = TooltipItemStackCache.getItemStack();

        boolean isEmpty = cacheItemStack == null || cacheItemStack.isEmpty();
        if (isEmpty) cacheItemStack = ItemStack.EMPTY;

        if (!isEmpty && components.getFirst() instanceof OrderedTextTooltipComponent ordered) {
            Text name = EnhancedTooltipsTextVisitor.get(ordered.text);
            Text cachedName = cacheItemStack.getItem().getName(cacheItemStack);

            if (!name.getString().equals(cachedName.getString())) {
                TooltipItemStackCache.saveItemStack(ItemStack.EMPTY);
                return;
            }
        }

        TooltipItemStackCache.saveItemStack(ItemStack.EMPTY);
        TooltipComponentManager.invoke(tooltipComponents, cacheItemStack);

        EnhancedTooltipsDrawer.drawTooltip((DrawContext) (Object) this, textRenderer, tooltipComponents, x, y, HoveredTooltipPositioner.INSTANCE, cacheItemStack);
        ci.cancel();
    }
}
