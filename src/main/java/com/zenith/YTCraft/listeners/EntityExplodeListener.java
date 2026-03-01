package com.zenith.YTCraft.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.zenith.YTCraft.listeners.handlers.AuthorMobDeathHandler;


public class EntityExplodeListener implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        Entity entity = e.getEntity();

        if(entity instanceof LivingEntity){
            AuthorMobDeathHandler.removeAuthorMob(entity);
        }

    }
}
