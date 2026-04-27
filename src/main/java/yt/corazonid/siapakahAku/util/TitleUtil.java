package yt.corazonid.siapakahAku.util;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import java.time.Duration;

public class TitleUtil {

    /**
     * Broadcast title to all players (default timing)
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
     * Broadcast clue title with longer display time and better colors
     */
    public static void broadcastClueTitle(Server server, String title, String subtitle) {
        // Warna lebih terang dan readable
        Title titleObj = Title.title(
            Component.text(title),  // "§6CLUE 1" - GOLD
            Component.text("§e" + subtitle),  // Subtitle dengan warna YELLOW (lebih terang dari putih)
            Title.Times.of(
                Duration.ofMillis(300),      // Fade in: 0.3s
                Duration.ofSeconds(7),       // Stay on screen: 7 second (vs 3 sebelumnya)
                Duration.ofMillis(300)       // Fade out: 0.3s
            )
        );

        for (Player player : server.getOnlinePlayers()) {
            player.showTitle(titleObj);
        }
    }

    /**
     * Broadcast winner title with special timing
     */
    public static void broadcastWinnerTitle(Server server, String title, String subtitle) {
        Title titleObj = Title.title(
            Component.text(title),
            Component.text(subtitle),
            Title.Times.of(Duration.ofMillis(500), Duration.ofSeconds(5), Duration.ofMillis(500))
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

