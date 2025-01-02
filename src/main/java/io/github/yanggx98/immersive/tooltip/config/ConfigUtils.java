package io.github.yanggx98.immersive.tooltip.config;

import me.grison.jtoml.impl.Toml;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConfigUtils {
    private final static String SUFFIX = ".toml";

    public static Toml initConfiguration(String fileName, ConfigurationInitCallback callback) throws Exception {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toString(), fileName + SUFFIX);
        boolean ret = checkFileExists(configFile);
        Toml toml = Toml.parse(configFile);
        if (!ret) {
            callback.init(configFile, toml);
        }
        return toml;
    }

    private static boolean checkFileExists(File file) {
        if (!file.exists()) {
            if (file.getParentFile().mkdirs()) {
                System.out.println("[ConfigUtils] Creating config directory");
            }
            try {
                if (file.createNewFile()) {
                    System.out.println("[ConfigUtils] Created config file");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
        return true;
    }

    public static void write(File file, String config) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(config.getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public interface ConfigurationInitCallback {
        void init(File file, Toml toml);
    }
}
