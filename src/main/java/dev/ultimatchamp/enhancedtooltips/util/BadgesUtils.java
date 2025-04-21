package dev.ultimatchamp.enhancedtooltips.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//? if fabric {
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
//?} else if neoforge {
/*import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;
*///?}

public class BadgesUtils {
    private static Map<String, String> mods = new HashMap<>();

    public static @NotNull Pair<String, Integer> getBadgeText(ItemStack stack) {
        String text = "";
        int fillColor = 0;

        for (Map.Entry<List<Item>, Pair<String, Integer>> entry : ItemGroupsUtils.getItemGroups().entrySet()) {
            if (entry.getKey().contains(stack.getItem())) {
                text = entry.getValue().getLeft();
                fillColor = entry.getValue().getRight();
                break;
            }
        }

        if (text.isEmpty()) {
            String namespace = Registries.ITEM.getId(stack.getItem()).getNamespace();
            text = getMods().getOrDefault(namespace, "");
            fillColor = getColorFromModName(namespace);
        }

        return new Pair<>(text, fillColor);
    }

    public static Map<String, String> getMods() {
        if (!mods.isEmpty()) return mods;

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
        int hash = modName.hashCode();

        int r = (hash >> 16) & 0xFF;
        int g = (hash >> 8) & 0xFF;
        int b = hash & 0xFF;
        int a = 0xFF;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static void drawFrame(DrawContext context, int x, int y, int width, int height, int z, int color) {
        renderVerticalLine(context, x, y, height - 2, z, color);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1, width - 2, z, color);
        renderHorizontalLine(context, x + 1, y - 1 + height - 1, width - 2, z, color);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }
}
