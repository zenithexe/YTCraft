package com.zenith.YTCraft.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenith.YTCraft.YTCraft;
import com.zenith.YTCraft.config.processors.CustomMobSettingsProcessor;
import com.zenith.YTCraft.config.processors.ItemGiveSettingsProcessor;
import com.zenith.YTCraft.config.processors.MobSpawnSettingsProcessor;
import com.zenith.YTCraft.config.processors.TimerSettingsProcessor;
import com.zenith.YTCraft.config.types.PluginSettings;

/**
 * Handles loading and saving settings.json file
 */
public class SettingsLoader {

    private static File settingsFile;
    private static Gson gson;
    private static PluginSettings settings;

    /**
     * Initialize the settings loader
     */
    public static void init(YTCraft plugin) {
        settingsFile = new File(plugin.getDataFolder(), "settings.json");
        gson = new GsonBuilder()
                .setLenient()
                .setPrettyPrinting()
                .create();

        loadFromFile();
    }

    /**
     * Get the loaded settings
     */
    public static PluginSettings getSettings() {
        return settings;
    }

    /**
     * Load settings from file and process them
     */
    private static void loadFromFile() {
        if (!settingsFile.exists()) {
            saveDefaultSettings();
        }

        try (Reader reader = Files.newBufferedReader(settingsFile.toPath())) {
            settings = gson.fromJson(reader, PluginSettings.class);
            YTCraft.getPlugin().getLogger().info("Settings loaded from settings.json");
        } catch (IOException e) {
            YTCraft.getPlugin().getLogger().log(java.util.logging.Level.SEVERE, "Failed to load settings.json", e);
        }
    }

    /**
     * Process loaded settings into runtime state
     */
    public static void processSettings() {
        if (settings == null) {
            YTCraft.getPlugin().getLogger().severe("Cannot process settings - settings is null");
            return;
        }
        
        TimerSettingsProcessor.process(settings.getTimerSettings());
        MobSpawnSettingsProcessor.process(settings.getMobSpawnSettings());
        ItemGiveSettingsProcessor.process(settings.getItemGiveSettings());
        CustomMobSettingsProcessor.process(settings.getCustomMobSettings());
    }

    /**
     * Save current settings to file
     */
    public static void saveSettings() {
        if (settings == null) {
            YTCraft.getPlugin().getLogger().warning("Cannot save settings - settings is null");
            return;
        }

        try (Writer writer = Files.newBufferedWriter(settingsFile.toPath())) {
            gson.toJson(settings, writer);
            YTCraft.getPlugin().getLogger().info("Settings saved to settings.json");
        } catch (IOException e) {
            YTCraft.getPlugin().getLogger().log(java.util.logging.Level.SEVERE, "Failed to save settings.json", e);
        }
    }

    /**
     * Reload settings from file and reprocess
     */
    public static void reload() {
        YTCraft.getPlugin().getLogger().info("Reloading settings...");
        loadFromFile();
        processSettings();
        YTCraft.getPlugin().getLogger().info("Settings reloaded successfully");
    }

    /**
     * Load settings from file without processing
     */
    public static void loadSettings() {
        loadFromFile();
    }

    /**
     * Reload mob tiers into runtime state
     */
    public static void loadMobTiers() {
        if (settings != null && settings.getMobSpawnSettings() != null) {
            MobSpawnSettingsProcessor.process(settings.getMobSpawnSettings());
        }
    }

    /**
     * Reload custom mobs into registry
     */
    public static void loadCustomMobs() {
        if (settings != null && settings.getCustomMobSettings() != null) {
            CustomMobSettingsProcessor.process(settings.getCustomMobSettings());
        }
    }

    /**
     * Save default settings.json from resources
     */
    private static void saveDefaultSettings() {
        settingsFile.getParentFile().mkdirs();

        InputStream defaultConfig = YTCraft.getPlugin().getResource("settings.json");

        if (defaultConfig != null) {
            try (OutputStream out = new FileOutputStream(settingsFile)) {
                byte[] buffer = new byte[1024];
                int length;

                while ((length = defaultConfig.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                YTCraft.getPlugin().getLogger().info("Default settings.json created");

            } catch (IOException e) {
                YTCraft.getPlugin().getLogger().log(java.util.logging.Level.SEVERE,
                        "Failed to save default settings.json", e);
            }

        } else {
            YTCraft.getPlugin().getLogger().warning("Default settings.json not found in resources");
        }
    }
}
