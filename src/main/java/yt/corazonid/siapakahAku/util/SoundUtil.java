package yt.corazonid.siapakahAku.util;

import org.bukkit.Sound;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class SoundUtil {

    /**
     * Broadcast sound to all players
     */
    public static void broadcastSound(Server server, Sound sound) {
        for (Player player : server.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        }
    }

    /**
     * Play sound to specific player
     */
    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }

    /**
     * Play correct answer sound
     */
    public static void playCorrectAnswerSound(Server server) {
        broadcastSound(server, Sound.ENTITY_PLAYER_LEVELUP);
    }

    /**
     * Play wrong answer sound
     */
    public static void playWrongAnswerSound(Player player) {
        playSound(player, Sound.ENTITY_VILLAGER_NO);
    }

    /**
     * Play game start sound
     */
    public static void playGameStartSound(Server server) {
        broadcastSound(server, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }
}

