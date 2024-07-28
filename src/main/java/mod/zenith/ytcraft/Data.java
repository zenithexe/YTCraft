package mod.zenith.ytcraft;


import java.util.*;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;



public class Data {

    public static Player streamer;

    public static Set<String> isUserMobAlive = new HashSet<>();
    public static Map<Entity,String> entityByUser = new LinkedHashMap<>();

    private static int activeTimeMin = 5;
    private static int activeTimeSec = 0;

    private static int restTimeMin = 5;
    private static int restTimeSec = 0;

    public static boolean isActiveTimerMode = false;

    public static void setActiveTime(int min, int sec){
        activeTimeMin=min;
        activeTimeSec=sec;
    }

    public static void setRestTime(int min, int sec){
        restTimeMin=min;
        restTimeSec=sec;
    }

    public static int[] getActiveTime(){
        return new int[]{activeTimeMin, activeTimeSec};
    }

    public static int[] getResTime(){
        return new int[]{restTimeMin,restTimeSec};
    }
    
    public static Map<String,Integer> EntityTypeToViewersList = new HashMap<>();


}
