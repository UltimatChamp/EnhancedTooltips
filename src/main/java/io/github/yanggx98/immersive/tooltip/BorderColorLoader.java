package io.github.yanggx98.immersive.tooltip;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BorderColorLoader extends SinglePreparationResourceReloader<Map<String, Integer>> implements IdentifiableResourceReloadListener {
    public static final BorderColorLoader INSTANCE = new BorderColorLoader();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private Map<String, Integer> borderColorMap = new HashMap<>();

    static final Identifier ID = Identifier.of(ImmersiveTooltip.MOD_ID, "border");

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    protected Map<String, Integer> prepare(ResourceManager manager, Profiler profiler) {
        Map<String, Integer> map = new HashMap<>();
        String dataType = "border";
        for (Map.Entry<Identifier, List<Resource>> entry : manager.findAllResources(dataType, id -> id.getPath().endsWith(".json")).entrySet()) {
            Identifier identifier = entry.getKey();
            if (identifier.getNamespace().equals(ImmersiveTooltip.MOD_ID)) {
                try {
                    for (Resource resource : entry.getValue()) {
                        InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                        JsonObject jsonObject = JsonHelper.deserialize(GSON, reader, JsonObject.class);

                        if (jsonObject != null) {
                            jsonObject.entrySet().forEach(e -> {
                                try {
                                    Integer color = e.getValue().getAsInt();
                                    map.put(e.getKey(), color);
                                } catch (Exception ignored) {
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    @Override
    protected void apply(Map<String, Integer> prepared, ResourceManager manager, Profiler profiler) {
        this.borderColorMap = prepared;
    }

    public Map<String, Integer> getBorderColorMap() {
        return ImmutableMap.copyOf(this.borderColorMap);
    }
}
