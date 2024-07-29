package mod.zenith.ytcraft;


import java.util.*;

import org.bukkit.entity.Player;



public class Data {

    public static Player streamer;

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
    
    public static Map<String,Integer> Config_EntityType_To_NViewers_List = new HashMap<>();
    public static Set<String> Alive_AuthorMobs = new HashSet<>();
    public static Map<String,String> Author_To_Mob_Duo = new HashMap<>();
    public static Map<String, Map<String,String>> ChannelId_To_AuthorMob_List = new HashMap<>();

}
