package com.zenith.YTCraft.config.processors;

import com.zenith.YTCraft.config.types.TimerSettings;
import com.zenith.YTCraft.data.TimerState;

/**
 * Processes TimerSettings into runtime state
 */
public class TimerSettingsProcessor {

    /**
     * Process timer settings into TimerState
     */
    public static void process(TimerSettings settings) {
        TimerState.setActiveTime(settings.getActiveSeconds());
        TimerState.setRestTime(settings.getRestSeconds());
        TimerState.setIsAlwaysActive(settings.isAlwaysActive());
    }
}
