package dev.ultimatchamp.enhancedtooltips.config;

import blue.endless.jankson.*;
import blue.endless.jankson.api.SyntaxError;
import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.TranslatableOption;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

public class EnhancedTooltipsConfig {
    @Comment("-> General\n(default: true)")
    public boolean rarityTooltip = true;

    @Comment("(default: 1.0)")
    public float scaleFactor = 1f;

    @Comment("(default: true)")
    public boolean itemBadges = true;

    @Comment("-> Border\nRARITY/ITEM_NAME/CUSTOM (default: RARITY)")
    public BorderColorMode borderColor = BorderColorMode.RARITY;

    public enum BorderColorMode implements TranslatableOption {
        RARITY(0, "enhancedtooltips.config.borderColor.rarity"),
        ITEM_NAME(1, "enhancedtooltips.config.borderColor.itemName"),
        CUSTOM(2, "generator.custom");

        private final int id;
        private final String translationKey;

        BorderColorMode(final int id, final String translationKey) {
            this.id = id;
            this.translationKey = translationKey;
        }

        public int getId() {
            return this.id;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }
    }

    public CustomBorderColorsConfig customBorderColors = new CustomBorderColorsConfig();

    public static class CustomBorderColorsConfig {
        @Comment("(default: #5000FF50)")
        public Color common = BorderColor.COMMON.getColor();

        @Comment("(default: #FFFF55FF)")
        public Color uncommon = BorderColor.UNCOMMON.getColor();

        @Comment("(default: #55FFFFFF)")
        public Color rare = BorderColor.RARE.getColor();

        @Comment("(default: #FF00FFFF)")
        public Color epic = BorderColor.EPIC.getColor();

        @Comment("(default: #5000FF50)")
        public Color endColor = BorderColor.END_COLOR.getColor();
    }

    public enum BorderColor {
        COMMON(0x505000FF),
        UNCOMMON(0xFFFFFF55),
        RARE(0xFF55FFFF),
        EPIC(0xFFFF00FF),
        END_COLOR(0x505000FF);

        private final int rgb;

        BorderColor(int rgb) {
            this.rgb = rgb;
        }

        public Color getColor() {
            return new Color(rgb, true);
        }
    }

    @Comment("-> Background\n(default: #100010F0)")
    public Color backgroundColor = new Color(0xF0100010, true);

    @Comment("-> Food & Drinks\n(default: true)")
    public boolean hungerTooltip = true;

    @Comment("(default: true)")
    public boolean saturationTooltip = true;

    @Comment("OFF/WITHOUT_ICONS/WITH_ICONS (default: WITH_ICONS)")
    public EffectsTooltipMode effectsTooltip = EffectsTooltipMode.WITH_ICONS;

    public enum EffectsTooltipMode implements TranslatableOption {
        OFF(0, "options.off"),
        WITHOUT_ICONS(1, "enhancedtooltips.config.effectsTooltip.withoutIcons"),
        WITH_ICONS(2, "enhancedtooltips.config.effectsTooltip.withIcons");

        private final int id;
        private final String translationKey;

        EffectsTooltipMode(final int id, final String translationKey) {
            this.id = id;
            this.translationKey = translationKey;
        }

        public int getId() {
            return this.id;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }
    }

    @Comment("-> Mobs\n(default: true)")
    public boolean armorTooltip = true;

    @Comment("(default: true)")
    public boolean bucketTooltip = true;

    @Comment("(default: true)")
    public boolean spawnEggTooltip = true;

    @Comment("-> Durability\nOFF/VALUE/PERCENTAGE (default: VALUE)")
    public DurabilityTooltipMode durabilityTooltip = DurabilityTooltipMode.VALUE;

    public enum DurabilityTooltipMode implements TranslatableOption {
        OFF(0, "options.off"),
        VALUE(1, "enhancedtooltips.config.durabilityTooltip.value"),
        PERCENTAGE(2, "enhancedtooltips.config.durabilityTooltip.percentage");

        private final int id;
        private final String translationKey;

        DurabilityTooltipMode(final int id, final String translationKey) {
            this.id = id;
            this.translationKey = translationKey;
        }

        public int getId() {
            return this.id;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }
    }

    @Comment("(default: false)")
    public boolean durabilityBar = false;

    private static final Jankson JANKSON = Jankson.builder()
            .registerSerializer(Color.class, (color, marshaller) -> new JsonPrimitive(String.format("#%02X%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()))) // in RRGGBBAA
            .registerDeserializer(JsonPrimitive.class, Color.class, (json, marshaller) -> {
                String hex = json.asString();
                if (hex.startsWith("#")) hex = hex.substring(1);

                if (hex.length() == 8) {
                    int r = Integer.parseInt(hex.substring(0, 2), 16); // Red
                    int g = Integer.parseInt(hex.substring(2, 4), 16); // Green
                    int b = Integer.parseInt(hex.substring(4, 6), 16); // Blue
                    int a = Integer.parseInt(hex.substring(6, 8), 16); // Alpha
                    return new Color(r, g, b, a);
                } else if (hex.length() == 6) { // no alpha
                    return Color.decode("#" + hex);
                } else {
                    throw new IllegalArgumentException("Invalid color format: " + json.asString());
                }
            })
            .build();

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("enhancedtooltips.json5");

    private static EnhancedTooltipsConfig cachedConfig;

    public static EnhancedTooltipsConfig load() {
        if (cachedConfig != null) {
            return cachedConfig;
        }

        EnhancedTooltipsConfig config;

        try {
            if (!Files.exists(CONFIG_PATH)) {
                EnhancedTooltips.LOGGER.info("[EnhancedTooltips] Config file not found. Creating a new one...");
                config = new EnhancedTooltipsConfig();
                save(config);
            } else {
                var configContent = Files.readString(CONFIG_PATH).trim();

                if (!configContent.startsWith("{") || !configContent.endsWith("}")) {
                    EnhancedTooltips.LOGGER.error("[EnhancedTooltips] Config file is empty or invalid. Creating a new one...");
                    config = new EnhancedTooltipsConfig();
                    save(config);
                } else {
                    var configJson = ensureDefaults(JANKSON.load(configContent));
                    config = JANKSON.fromJson(configJson, EnhancedTooltipsConfig.class);
                }
            }
        } catch (IOException | SyntaxError e) {
            EnhancedTooltips.LOGGER.error("[EnhancedTooltips]", e);
            config = new EnhancedTooltipsConfig();
            save(config);
        }

        cachedConfig = config;
        return cachedConfig;
    }

    public static void save(EnhancedTooltipsConfig config) {
        try {
            var jsonString = JANKSON.toJson(config).toJson(true, true);
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, jsonString);
            cachedConfig = config;
        } catch (IOException e) {
            EnhancedTooltips.LOGGER.error("[EnhancedTooltips]", e);
        }
    }

    private static JsonObject ensureDefaults(JsonObject configJson) {
        var modified = false;
        var defaultConfig = new EnhancedTooltipsConfig();

        for (var field : EnhancedTooltipsConfig.class.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) continue;

            try {
                var fieldName = field.getName();
                var defaultValue = field.get(defaultConfig);

                if (!configJson.containsKey(fieldName)) {
                    EnhancedTooltips.LOGGER.info("[EnhancedTooltips] Missing config field '{}'. Re-saving as default.", fieldName);
                    configJson.put(fieldName, JANKSON.toJson(defaultValue));
                    modified = true;
                }
            } catch (IllegalAccessException e) {
                EnhancedTooltips.LOGGER.error("[EnhancedTooltips] Failed to access field '{}'", field.getName(), e);
            }
        }

        if (modified) {
            var config = JANKSON.fromJson(configJson, EnhancedTooltipsConfig.class);
            save(config);
        }

        return configJson;
    }

    public static Screen createConfigScreen(Screen parent) {
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
            return EnhancedTooltipsGui.createConfigScreen(parent);
        } else {
            return new NoConfigScreenWarning(parent);
        }
    }
}
