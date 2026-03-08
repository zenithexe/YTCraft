package com.zenith.YTCraft.custommobs;

import java.lang.reflect.InvocationTargetException;

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

            // Create PlayerDisguise instance with player username
            Object disguise = playerDisguiseClass.getConstructor(String.class)
                    .newInstance(customMob.getPlayerUsername());

            // Set the player name to fetch skin from Mojang
            playerDisguiseClass.getMethod("setName", String.class)
                    .invoke(disguise, customMob.getPlayerUsername());

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

            Bukkit.getLogger().info(String.format("Applied player skin '%s' to %s with name '%s'", customMob.getPlayerUsername(), entity.getType(), authorName));


          

            return true;

        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().severe(String.format("Failed to find LibsDisguises class: %s", e.getMessage()));
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            Bukkit.getLogger().severe(String.format("Failed to find LibsDisguises method: %s", e.getMessage()));
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            Bukkit.getLogger().severe(String.format("Failed to apply skin: %s - %s", e.getClass().getSimpleName(), e.getMessage()));
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
}
