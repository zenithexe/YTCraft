package com.zenith.YTCraft.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;

public class EntityCombustListener implements Listener {

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {

        boolean isFireBySunlight = false;

        //Prevent combusting from sunlight (Fire not by Entity or Block).
        if (!(event instanceof EntityCombustByBlockEvent) && !(event instanceof EntityCombustByEntityEvent)) {
            isFireBySunlight = true;
        }

        if (isFireBySunlight && event.getEntity().hasMetadata("isChatSpawned")) {
            event.setCancelled(true);
        }

    }

}
