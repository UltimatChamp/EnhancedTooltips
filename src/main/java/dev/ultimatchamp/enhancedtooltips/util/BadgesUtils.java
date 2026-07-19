package dev.ultimatchamp.enhancedtooltips.util;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
//?} else if neoforge {
/*import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;
*///?}

public class BadgesUtils {
    private static final Map<String, String> mods = new HashMap<>();
    private static boolean modsCollected = false;

    public static @NotNull Pair<@NotNull Component, @NotNull Integer> getBadgeText(ItemStack stack) {
        Component text = Component.empty();
        int fillColor = 0;

        Pair<@NotNull Component, @NotNull Integer> badge = ItemGroupsUtils.getItemBadge(stack.getItem());
        if (badge != null) {
            text = badge.left();
            fillColor = badge.right();
        }

        String namespace = BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace();
        boolean hasBadge = !text.toFlatList().isEmpty();

        boolean asVanillaGroup = false;
        if (hasBadge && !namespace.equals("minecraft")) {
            String badgeString = text.getString();
            for (String groupName : ItemGroupsUtils.getItemGroupNames()) {
                if (groupName.contains(badgeString)) {
                    asVanillaGroup = true;
                    break;
                }
            }
        }

        if (!hasBadge || asVanillaGroup) {
            text = Component.literal(getMods().getOrDefault(namespace, ""));
            fillColor = getColorFromModName(namespace);
        }

        return Pair.of(text, fillColor);
    }

    public static Map<String, String> getMods() {
        if (modsCollected) return mods;

        //? if fabric {
        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            if (modContainer.getMetadata().getId().equals("minecraft")) continue;
            mods.put(modContainer.getMetadata().getId(), modContainer.getMetadata().getName());
        }
        //?} else {
        /*for (ModContainer modContainer : ModList.get().getSortedMods()) {
            if (modContainer.getModId().equals("minecraft")) continue;
            mods.put(modContainer.getNamespace(), modContainer.getModInfo().getDisplayName());
        }
        *///?}

        modsCollected = true;
        return mods;
    }

    public static int darkenColor(int color, float factor) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        red = Math.max(0, (int) (red * factor));
        green = Math.max(0, (int) (green * factor));
        blue = Math.max(0, (int) (blue * factor));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static int getColorFromModName(String modName) {
        float hue = Math.floorMod(modName.hashCode(), 360) / 360f;
        return 0xFF000000 | (Color.HSBtoRGB(hue, 0.6f, 0.85f) & 0xFFFFFF);
    }

    public static void drawFrame(GuiGraphicsExtractor context, int x, int y, int width, int height, int z, int color) {
        renderVerticalLine(context, x, y, height - 2, z, color);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1, width - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1 + height - 1, width - 2, z, color);
    }

    private static void renderVerticalLine(GuiGraphicsExtractor context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, color);
    }

    private static void renderHorizontalLine(GuiGraphicsExtractor context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, color);
    }
}
