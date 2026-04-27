package yt.corazonid.siapakahAku.util;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import java.time.Duration;

public class TitleUtil {

    /**
     * Broadcast title to all players
     */
    public static void broadcastTitle(Server server, String title, String subtitle) {
        Title titleObj = Title.title(
            Component.text(title),
            Component.text(subtitle),
            Title.Times.of(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
        );

        for (Player player : server.getOnlinePlayers()) {
            player.showTitle(titleObj);
        }
    }

    /**
     * Show title to a specific player
     */
    public static void showTitle(Player player, String title, String subtitle) {
        Title titleObj = Title.title(
            Component.text(title),
            Component.text(subtitle),
            Title.Times.of(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
        );

        player.showTitle(titleObj);
    }

    /**
     * Clear title
     */
    public static void clearTitle(Player player) {
        player.clearTitle();
    }
}

