package mod.zenith.ytcraft.EventListeners;

import mod.zenith.ytcraft.AdventureLib.TabList;
import mod.zenith.ytcraft.Data;
import mod.zenith.ytcraft.YTCraft;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        Entity entity = e.getEntity();
        AuthorMobDeathHandler.removeAuthorMob(entity);
    }
}
