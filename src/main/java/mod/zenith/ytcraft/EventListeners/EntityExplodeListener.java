package mod.zenith.ytcraft.EventListeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;


public class EntityExplodeListener implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        Entity entity = e.getEntity();

        if(entity instanceof LivingEntity){
            AuthorMobDeathHandler.removeAuthorMob(entity);
        }

    }
}
