package com.zenith.YTCraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.zenith.YTCraft.listeners.handlers.AuthorMobDeathHandler;

public class EntityRemoveListener implements Listener {
    @EventHandler
    public void onEntityRemove(EntityRemoveFromWorldEvent e){
        Bukkit.getLogger().info("Mob Remove Triggered.");
        Entity entity = e.getEntity();
        AuthorMobDeathHandler.removeAuthorMob(entity);
    }
}
