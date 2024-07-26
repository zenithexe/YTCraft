package mod.zenith.ytcraft.AdventureLib;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import mod.zenith.ytcraft.Data;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class Titles {

    private static Player player = Data.streamer;

    public static void showTimerActiveTitle(final @NonNull Audience target) {

        final Component mainTitle = Component.text("Chat Control Activated!", NamedTextColor.RED);
        final Component subtitle = Component.text("Now Viewers can take Actions", NamedTextColor.WHITE);
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(2000), Duration.ofMillis(500));

        // Creates a simple title with the default values for fade-in, stay on screen and fade-out durations
        final Title title = Title.title(mainTitle, subtitle, times);

        // Send the title to your audience
        target.showTitle(title);
        player.playSound(player.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0f, 1.0f);

    }

    public static void showTimerRestTitle(final @NotNull Audience target){

        final Component mainTitle = Component.text("Rest Time", NamedTextColor.RED);
        final Component subtitle = Component.text("Viewers actions disabled.", NamedTextColor.WHITE);
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(2000), Duration.ofMillis(500));

        // Creates a simple title with the default values for fade-in, stay on screen and fade-out durations
        final Title title = Title.title(mainTitle, subtitle, times);

        // Send the title to your audience
        target.showTitle(title);
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);

    }
}
