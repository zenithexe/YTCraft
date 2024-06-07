package mod.zenith.ytcraft;


import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;

import org.bukkit.entity.Entity;



public class Data {
    public static Set<String> isUserMobAlive = new HashSet<>();
    
    public static Map<Entity,String> entityByUser = new LinkedHashMap<>();
}
