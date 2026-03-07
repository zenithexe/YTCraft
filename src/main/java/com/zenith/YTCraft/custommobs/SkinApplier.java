package com.zenith.YTCraft.custommobs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Base64;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

/**
 * Applies custom skins to entities using LibsDisguises Uses reflection to avoid
 * compile-time dependency
 */
public class SkinApplier {

    private static Boolean libsDisguisesAvailable = null;
    private static ClassLoader libsDisguisesClassLoader = null;

    /**
     * Apply custom skin to an entity based on CustomMob configuration
     */
    public static boolean applySkin(LivingEntity entity, CustomMob customMob, String authorName, Plugin plugin) {
        if (!isLibsDisguisesAvailable()) {
            Bukkit.getLogger().warning("LibsDisguises not found! Cannot apply custom skin.");
            Bukkit.getLogger().warning("Download from: https://www.spigotmc.org/resources/libs-disguises.81/");
            return false;
        }

        try {
            // Get LibsDisguises ClassLoader
            ClassLoader loader = getLibsDisguisesClassLoader();
            
            // Use reflection to avoid compile-time dependency
            Class<?> disguiseAPIClass = Class.forName("me.libraryaddict.disguise.DisguiseAPI", true, loader);
            Class<?> playerDisguiseClass = Class.forName("me.libraryaddict.disguise.disguisetypes.PlayerDisguise", true, loader);
            Class<?> disguiseClass = Class.forName("me.libraryaddict.disguise.disguisetypes.Disguise", true, loader);

            // Create PlayerDisguise instance
            Object disguise = playerDisguiseClass.getConstructor(String.class)
                    .newInstance(customMob.getSkinValue());

            // Configure disguise based on skin source
            switch (customMob.getSkinSource()) {
                case PLAYER:
                    // LibsDisguises will fetch from Mojang automatically
                    playerDisguiseClass.getMethod("setName", String.class)
                            .invoke(disguise, customMob.getSkinValue());
                    break;

                case FILE:
                    // Load skin from file and convert to base64
                    // Not implemented yet
                    break;

                case URL:
                    // Set skin from URL
                    // Not implemented yet
                    break;
            }

            // Get the watcher to set custom name
            Class<?> flagWatcherClass = Class.forName("me.libraryaddict.disguise.disguisetypes.FlagWatcher", true, loader);
            Object watcher = playerDisguiseClass.getMethod("getWatcher").invoke(disguise);
            
            // Set custom name on the watcher (this will display the author's name)
            flagWatcherClass.getMethod("setCustomName", String.class).invoke(watcher, authorName);
            flagWatcherClass.getMethod("setCustomNameVisible", boolean.class).invoke(watcher, true);

            // Apply the disguise: DisguiseAPI.disguiseToAll(entity, disguise)
            // Note: disguiseToAll takes Entity, not LivingEntity
            Class<?> entityClass = Class.forName("org.bukkit.entity.Entity");
            disguiseAPIClass.getMethod("disguiseToAll", entityClass, disguiseClass)
                    .invoke(null, entity, disguise);

            Bukkit.getLogger().info(String.format(
                    "Applied %s skin '%s' to %s",
                    customMob.getSkinSource(),
                    customMob.getSkinValue(),
                    entity.getType()
            ));

            return true;

        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().severe(String.format(
                    "Failed to find LibsDisguises class: %s",
                    e.getMessage()
            ));
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            Bukkit.getLogger().severe(String.format(
                    "Failed to find LibsDisguises method: %s",
                    e.getMessage()
            ));
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            Bukkit.getLogger().severe(String.format(
                    "Failed to apply skin: %s - %s",
                    e.getClass().getSimpleName(),
                    e.getMessage()
            ));
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if LibsDisguises is available
     */
    private static boolean isLibsDisguisesAvailable() {
        if (libsDisguisesAvailable == null) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("LibsDisguises");
            libsDisguisesAvailable = plugin != null && plugin.isEnabled();
            if (libsDisguisesAvailable) {
                libsDisguisesClassLoader = plugin.getClass().getClassLoader();
            }
        }
        return libsDisguisesAvailable;
    }

    /**
     * Get LibsDisguises ClassLoader
     */
    private static ClassLoader getLibsDisguisesClassLoader() {
        if (libsDisguisesClassLoader == null) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("LibsDisguises");
            if (plugin != null) {
                libsDisguisesClassLoader = plugin.getClass().getClassLoader();
            }
        }
        return libsDisguisesClassLoader;
    }

    /**
     * Remove disguise from entity
     */
    public static void removeSkin(LivingEntity entity) {
        if (!isLibsDisguisesAvailable()) {
            return;
        }

        try {
            ClassLoader loader = getLibsDisguisesClassLoader();
            Class<?> disguiseAPIClass = Class.forName("me.libraryaddict.disguise.DisguiseAPI", true, loader);
            Class<?> entityClass = Class.forName("org.bukkit.entity.Entity");

            // Check if disguised: DisguiseAPI.isDisguised(entity)
            boolean isDisguised = (boolean) disguiseAPIClass
                    .getMethod("isDisguised", entityClass)
                    .invoke(null, entity);

            if (isDisguised) {
                // Remove disguise: DisguiseAPI.undisguiseToAll(entity)
                disguiseAPIClass.getMethod("undisguiseToAll", entityClass)
                        .invoke(null, entity);
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            Bukkit.getLogger().warning(String.format("Failed to remove disguise: %s", e.getMessage()));
        }
    }

    /**
     * Load skin from file and convert to base64
     * Looks for skin files in plugins/YTCraft/skins/ directory
     */
    private static String loadSkinFromFile(Plugin plugin, String filename) {
        try {
            // Create skins directory if it doesn't exist
            File skinsDir = new File(plugin.getDataFolder(), "skins");
            if (!skinsDir.exists()) {
                skinsDir.mkdirs();
                Bukkit.getLogger().info("Created skins directory: " + skinsDir.getAbsolutePath());
            }

            // Load the skin file
            File skinFile = new File(skinsDir, filename);
            if (!skinFile.exists()) {
                Bukkit.getLogger().warning(String.format(
                        "Skin file not found: %s (looking in %s)",
                        filename,
                        skinsDir.getAbsolutePath()
                ));
                return null;
            }

            // Read file and convert to base64
            byte[] fileContent = Files.readAllBytes(skinFile.toPath());
            String base64 = Base64.getEncoder().encodeToString(fileContent);
            
            Bukkit.getLogger().info(String.format(
                    "Loaded skin file: %s (%d bytes)",
                    filename,
                    fileContent.length
            ));
            
            return base64;

        } catch (IOException e) {
            Bukkit.getLogger().severe(String.format(
                    "Failed to read skin file %s: %s",
                    filename,
                    e.getMessage()
            ));
            e.printStackTrace();
            return null;
        }
    }
}
