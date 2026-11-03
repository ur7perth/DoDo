package com.easykillmob;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class ModConfig {

    public static int cps = 20;
    public static int toggleKey = 92;

    private static final Path CONFIG =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("easykillmob.properties");

    public static void load() {

        Properties properties = new Properties();

        if (!CONFIG.toFile().exists()) {
            save();
            return;
        }

        try (InputStream input =
                     new FileInputStream(CONFIG.toFile())) {

            properties.load(input);

            cps = Integer.parseInt(
                    properties.getProperty("cps", "20")
            );

            toggleKey = Integer.parseInt(
                    properties.getProperty("toggleKey", "92")
            );

            cps = Math.max(1, Math.min(20, cps));

        } catch (Exception e) {
            cps = 20;
            toggleKey = 92;
        }
    }

    public static void save() {

        Properties properties = new Properties();

        properties.setProperty("cps", String.valueOf(cps));
        properties.setProperty(
                "toggleKey",
                String.valueOf(toggleKey)
        );

        try (OutputStream output =
                     new FileOutputStream(CONFIG.toFile())) {

            properties.store(output, "EasyKillMob Configuration");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
