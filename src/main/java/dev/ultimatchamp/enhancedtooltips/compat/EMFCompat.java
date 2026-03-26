package dev.ultimatchamp.enhancedtooltips.compat;

import net.minecraft.network.chat.Component;
import traben.entity_model_features.EMFAnimationApi;

public class EMFCompat {
    public static final Component KEY = Component.literal("enhancedtooltips entity do not touch");

    public static void registerVanillaModelCondition() {
        EMFAnimationApi.registerVanillaModelCondition((entity) -> entity.etf$getCustomName().contains(KEY));
    }
}
