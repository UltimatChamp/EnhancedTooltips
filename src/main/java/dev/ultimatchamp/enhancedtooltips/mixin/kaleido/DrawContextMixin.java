package dev.ultimatchamp.enhancedtooltips.mixin.kaleido;

import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipComponentAPI;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.api.TooltipDrawerProvider;
import dev.ultimatchamp.enhancedtooltips.kaleido.render.tooltip.impl.TooltipItemStackCache;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.OrderedTextTooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//? if >1.21.1 {
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
//?}

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {
    //? if >1.21.1 {
    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V", at = @At("HEAD"), cancellable = true)
    private void injectDrawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo ci) {
    //?} else {
    /*@Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", at = @At("HEAD"), cancellable = true)
    private void injectDrawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
    *///?}
        List<TooltipComponent> mutableComponents = new ArrayList<>(components);

        Optional<TooltipComponent> markComponent = mutableComponents.stream()
                .filter(component -> component instanceof OrderedTextTooltipComponent)
                .filter(component -> EnhancedTooltips.MARK_KEY.equals(getContent(((OrderedTextTooltipComponent) component).text)))
                .findFirst();

        if (markComponent.isPresent()) {
            mutableComponents.remove(markComponent.get());

            ItemStack cacheItemStack = TooltipItemStackCache.getItemStack();
            if (cacheItemStack != null) {
                TooltipComponentAPI.EVENT.invoker().of(mutableComponents, cacheItemStack);

                TooltipDrawerProvider.ITooltipDrawer drawer = TooltipDrawerProvider.getTooltipDrawer();
                if (drawer != null) {
                    drawer.drawTooltip((DrawContext) (Object) this, textRenderer, mutableComponents, x, y, HoveredTooltipPositioner.INSTANCE);
                    ci.cancel();
                }
            }
        }
    }

    @Unique
    private String getContent(OrderedText text) {
        if (text == null) {
            return null;
        }

        try {
            Field[] fields = text.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.canAccess(text)) {
                    field.setAccessible(true);
                }

                if (field.get(text) instanceof String content) {
                    return content;
                }
            }
        } catch (IllegalAccessException e) {
            EnhancedTooltips.LOGGER.error("{}", EnhancedTooltips.MOD_ID, e);
        }

        return null;
    }
}
