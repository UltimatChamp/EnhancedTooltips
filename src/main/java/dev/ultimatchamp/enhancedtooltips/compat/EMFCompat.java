package dev.ultimatchamp.enhancedtooltips.compat;

import dev.ultimatchamp.enhancedtooltips.config.EnhancedTooltipsConfig;
import traben.entity_model_features.EMFAnimationApi;

public class EMFCompat {
    public static final String KEY = "enhancedtooltips entity do not touch";

    public static void registerVanillaModelCondition() {
        try {
            EMFAnimationApi.registerVanillaModelCondition((entity) -> {
                if (!EnhancedTooltipsConfig.load().mobs.disableEmfModels) return false;

                var customName = entity.etf$getCustomName();
                return customName != null && customName.getString().contains(KEY);
            });
        } catch (Exception ignored) {}
    }
}
