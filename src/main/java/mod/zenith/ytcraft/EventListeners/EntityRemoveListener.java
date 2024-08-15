package mod.zenith.ytcraft.EventListeners;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityRemoveListener implements Listener {
    @EventHandler
    public void onEntityRemove(EntityRemoveFromWorldEvent e){
        Bukkit.getLogger().info("Mob Remove Triggered.");
        Entity entity = e.getEntity();
        AuthorMobDeathHandler.removeAuthorMob(entity);
    }
}
