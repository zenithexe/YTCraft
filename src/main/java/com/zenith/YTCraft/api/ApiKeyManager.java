package com.zenith.YTCraft.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

public class ApiKeyManager {

    private static List<String> apiKeys = new ArrayList<>();
    private static int currentIndex = 0;
    private static int startIndex = 0;

    public static void setKeys(List<String> keys) {
        apiKeys = (keys != null) ? new ArrayList<>(keys) : new ArrayList<>();
        currentIndex = 0;
        startIndex = 0;
    }

    public static String getCurrentKey() {
        if (apiKeys.isEmpty()) return null;
        return apiKeys.get(currentIndex);
    }

    /**
     * Rotate to the next API key. Returns false if all keys have been tried.
     */
    public static boolean rotateKey() {
        if (apiKeys.size() <= 1) return false;

        currentIndex = (currentIndex + 1) % apiKeys.size();
        if (currentIndex == startIndex) {
            return false;
        }

        Bukkit.getLogger().warning(String.format("[YTCraft] Switched to API key %d of %d", currentIndex + 1, apiKeys.size()));
        return true;
    }

    /**
     * Reset rotation tracking after a successful API call.
     */
    public static void resetRotation() {
        startIndex = currentIndex;
    }

    public static int getKeyCount() {
        return apiKeys.size();
    }
}
