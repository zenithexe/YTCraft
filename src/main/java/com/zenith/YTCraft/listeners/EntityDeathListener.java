package com.zenith.YTCraft.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.zenith.YTCraft.listeners.handlers.AuthorMobDeathHandler;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        Entity entity = e.getEntity();
        AuthorMobDeathHandler.removeAuthorMob(entity);
    }
}
