package yt.corazonid.siapakahAku.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.model.GamePlayer;
import java.util.Collection;

public class CekposisiCommand implements CommandExecutor {
    private final GameManager gameManager;

    public CekposisiCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // FIX: Hanya moderator yang dapat menggunakan command ini
        if (!sender.hasPermission("siapakahaku.moderator")) {
            sender.sendMessage("§c✗ Hanya moderator yang dapat menggunakan command ini!");
            return true;
        }

        // Get all registered players
        Collection<GamePlayer> registeredPlayers = gameManager.getRegisteredPlayers();

        if (registeredPlayers.isEmpty()) {
            sender.sendMessage("§c✗ Tidak ada player yang terdaftar!");
            return true;
        }

        // Teleport semua player ke pijakan masing-masing
        int teleportedCount = 0;
        for (GamePlayer gamePlayer : registeredPlayers) {
            String playerName = gamePlayer.getPlayerName();
            Player player = gamePlayer.getPlayer();

            if (player == null || !player.isOnline()) {
                continue;  // Skip offline players
            }

            // Get player's current pijakan location
            Location pijakanLoc = gameManager.getPlayerLocation(playerName);
            if (pijakanLoc == null) {
                sender.sendMessage("§c⚠ " + playerName + " - pijakan location tidak ditemukan!");
                continue;
            }

            // Teleport player to pijakan
            player.teleport(pijakanLoc);
            teleportedCount++;
        }

        // Broadcast result
        sender.sendMessage("§a");
        sender.sendMessage("§a✓ Cekposisi: " + teleportedCount + " player di-teleport ke pijakan masing-masing!");
        sender.sendMessage("§a");
        Bukkit.broadcastMessage("§6ℹ [Moderator] Semua player di-teleport ke pijakan masing-masing!");

        return true;
    }
}

