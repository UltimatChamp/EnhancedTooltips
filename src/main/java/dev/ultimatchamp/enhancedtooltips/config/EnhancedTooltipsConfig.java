package dev.ultimatchamp.enhancedtooltips.config;

import blue.endless.jankson.*;
import blue.endless.jankson.api.SyntaxError;
import dev.ultimatchamp.enhancedtooltips.EnhancedTooltips;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.TranslatableOption;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
//?} else neoforge {
/*import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.ModList;
*///?}

public class EnhancedTooltipsConfig {
    public GeneralConfig general = new GeneralConfig();
    public PopUpAnimationConfig popUpAnimation = new PopUpAnimationConfig();
    public ItemPreviewAnimationConfig itemPreviewAnimation = new ItemPreviewAnimationConfig();
    public BorderConfig border = new BorderConfig();
    public BackgroundConfig background = new BackgroundConfig();
    public FoodAndDrinksConfig foodAndDrinks = new FoodAndDrinksConfig();
    public MobsConfig mobs = new MobsConfig();
    public MapConfig mapTooltip = new MapConfig();
    public PaintingConfig paintingTooltip = new PaintingConfig();
    public DurabilityConfig durability = new DurabilityConfig();
    public HeldItemTooltipConfig heldItemTooltip = new HeldItemTooltipConfig();

    public static class GeneralConfig {
        @Comment("Shows the rarity of an item in its tooltip.\n(default: true)")
        public boolean rarityTooltip = true;

        @Comment("Shows the category of an item in a badge on its tooltip.\n(default: true)")
        public boolean itemBadges = true;

        @Comment("Adjusts the size of tooltips.\nA scale of 100% displays the tooltip at its default size.\n(default: 1.0)")
        public float scaleFactor = 1f;
    }

    public static class PopUpAnimationConfig {
        @Comment("Toggles the tooltip pop-up animation.\n(default: true)")
        public boolean enabled = true;

        @Comment("Duration of the pop-up animation in seconds.\n(default: 1.5)")
        public float time = 1.5f;

        @Comment("Magnitude of the pop-up animation.\n(default: 1.0)")
        public float magnitude = 1.0f;
    }

    public static class ItemPreviewAnimationConfig {
        @Comment("Toggles the item preview bouncing animation.\n(default: true)")
        public boolean enabled = true;

        @Comment("Duration of the item preview animation in seconds.\n(default: 1.0)")
        public float time = 1f;

        @Comment("Magnitude of the item preview animation.\n(default: 2.0)")
        public float magnitude = 2f;
    }

    public static class BorderConfig {
        @Comment("Determines how the border color of tooltips is set.\nRARITY/ITEM_NAME/CUSTOM (default: RARITY)")
        public BorderColorMode borderColor = BorderColorMode.RARITY;

        @Comment("Custom border colors when borderColor is set to CUSTOM.")
        public CustomBorderColorsConfig customBorderColors = new CustomBorderColorsConfig();
    }

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

    public static class CustomBorderColorsConfig {
        @Comment("'Common' rarity border color.\n(default: #5000FF50)")
        public Color common = BorderColor.COMMON.getColor();

        @Comment("'Uncommon' rarity border color.\n(default: #FFFF55FF)")
        public Color uncommon = BorderColor.UNCOMMON.getColor();

        @Comment("'Rare' rarity border color.\n(default: #55FFFFFF)")
        public Color rare = BorderColor.RARE.getColor();

        @Comment("'Epic' rarity border color.\n(default: #FF00FFFF)")
        public Color epic = BorderColor.EPIC.getColor();

        @Comment("Gradient end color for the border.\n(default: #5000FF50)")
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

    public static class BackgroundConfig {
        @Comment("Background color of the tooltip.\n(default: #100010F0)")
        public Color backgroundColor = new Color(0xF0100010, true);
    }

    public static class FoodAndDrinksConfig {
        @Comment("Shows the maximum hunger which can be gained from an item in its tooltip.\n(default: true)")
        public boolean hungerTooltip = true;

        @Comment("Shows the maximum saturation which can be gained from an item in its tooltip.\n(default: true)")
        public boolean saturationTooltip = true;

        @Comment("Shows a list of effects applied on consuming an item in its tooltip.\nWITH_ICONS/WITHOUT_ICONS/OFF (default: WITH_ICONS)")
        public EffectsTooltipMode effectsTooltip = EffectsTooltipMode.WITH_ICONS;
    }

    public enum EffectsTooltipMode implements TranslatableOption {
        WITH_ICONS(0, "enhancedtooltips.config.effectsTooltip.withIcons"),
        WITHOUT_ICONS(1, "enhancedtooltips.config.effectsTooltip.withoutIcons"),
        OFF(2, "options.off");

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

    public static class MobsConfig {
        @Comment("The rotation speed of the model.\n(default: 0.2)")
        public float rotationSpeed = 0.2f;

        @Comment("Shows a preview of the armor piece on an armor stand.\n(default: true)")
        public boolean armorTooltip = true;

        @Comment("Shows a preview of the horse armor on a horse.\n(default: true)")
        public boolean horseArmorTooltip = true;

        @Comment("Shows a preview of the wolf armor on a wolf.\n(default: true)")
        public boolean wolfArmorTooltip = true;

        @Comment("Shows a preview of the bucket entity in a bucket.\n(default: true)")
        public boolean bucketTooltip = true;

        @Comment("Shows a preview of the spawn egg entity.\n(default: true)")
        public boolean spawnEggTooltip = true;
    }

    public static class MapConfig {
        @Comment("Shows a preview of the filled map in its tooltip.\n(default: true)")
        public boolean enabled = true;
    }

    public static class PaintingConfig {
        @Comment("Shows a preview of the painting in its tooltip.\n(default: true)")
        public boolean enabled = true;
    }

    public static class DurabilityConfig {
        @Comment("Shows the durability of an item in its tooltip.\nVALUE/PERCENTAGE/OFF (default: VALUE)")
        public DurabilityTooltipMode durabilityTooltip = DurabilityTooltipMode.VALUE;

        @Comment("Shows the durability of an item, represented by a bar, in its tooltip.\n(default: false)")
        public boolean durabilityBar = false;
    }

    public enum DurabilityTooltipMode implements TranslatableOption {
        VALUE(0, "enhancedtooltips.config.durabilityTooltip.value"),
        PERCENTAGE(1, "enhancedtooltips.config.durabilityTooltip.percentage"),
        OFF(2, "options.off");

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

    public static class HeldItemTooltipConfig {
        @Comment("Toggles the improved held items tooltips feature.\nON/MINIMAL/OFF (default: ON)")
        public HeldItemTooltipMode mode = HeldItemTooltipMode.ON;

        @Comment("Shows a neat background behind the held item tooltip text.\n(default: true)")
        public boolean showBackground = true;

        @Comment("Defines the line limit for the held item tooltip.\n(default: 10)")
        public int maxLines = 10;

        @Comment("Adjusts the size of the held item tooltips.\nA scale of 100% displays the tooltip at its default size.\n(default: 1.0)")
        public float scaleFactor = 1f;

        @Comment("Shows a dynamic tilt animation for the held item tooltip when scrolling the hotbar.\n(default: true)")
        public boolean tiltAnimation = true;

        @Comment("Duration of the tilt animation in ms.\n(default: 300)")
        public int tiltDuration = 300;

        @Comment("Magnitude of the tilt animation.\n(default: 10.0)")
        public float tiltMagnitude = 10f;

        @Comment("Smoothness of the tilt animation.\n(default: 2.0)")
        public float tiltEasing = 2f;
    }

    public enum HeldItemTooltipMode implements TranslatableOption {
        ON(0, "options.on"),
        MINIMAL(1, "options.particles.minimal"),
        OFF(2, "options.off");

        private final int id;
        private final String translationKey;

        HeldItemTooltipMode(final int id, final String translationKey) {
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

    public static final Path CONFIG_PATH =
            //? if fabric {
            FabricLoader.getInstance().getConfigDir().resolve("enhancedtooltips.json5");
            //?} else if neoforge {
            /*FMLPaths.CONFIGDIR.get().resolve("enhancedtooltips.json5");
            *///?}

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
        //? if fabric {
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
        //?} else if neoforge {
        /*if (ModList.get().isLoaded("yet_another_config_lib_v3")) {
        *///?}
            return EnhancedTooltipsGui.createConfigScreen(parent);
        } else {
            return new NoConfigScreenWarning(parent);
        }
    }
}
