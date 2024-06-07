package mod.zenith.ytcraft.EventListeners;

import mod.zenith.ytcraft.Data;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        Entity entity = e.getEntity();
        String channelId = Data.entityByUser.get(entity);
        Data.isUserMobAlive.remove(channelId);
        Data.entityByUser.remove(entity);
    }
}
