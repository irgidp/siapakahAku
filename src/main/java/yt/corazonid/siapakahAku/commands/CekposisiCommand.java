package yt.corazonid.siapakahAku.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.model.GamePlayer;

public class CekposisiCommand implements CommandExecutor {
    private final GameManager gameManager;

    public CekposisiCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check if moderator - if not moderator, deny
        if (!sender.hasPermission("siapakahaku.moderator")) {
            sender.sendMessage("§c✗ Hanya moderator yang dapat menggunakan command ini!");
            return true;
        }
        Player moderator = (Player) sender;
        Player targetPlayer = null;
        String targetPlayerName = null;

        // FIX #4: If moderator provides argument, teleport that specific player
        if (args.length > 0) {
            targetPlayerName = args[0];
            targetPlayer = Bukkit.getPlayer(targetPlayerName);
            if (targetPlayer == null) {
                moderator.sendMessage("§c✗ Player '" + targetPlayerName + "' tidak ditemukan!");
                return true;
            }
        } else {
            // No argument - teleport the moderator themselves
            targetPlayer = moderator;
            targetPlayerName = moderator.getName();
        }

        // Check if target player is registered
        if (!gameManager.isPlayerRegistered(targetPlayerName)) {
            moderator.sendMessage("§c✗ Player '" + targetPlayerName + "' tidak terdaftar dalam game ini!");
            return true;
        }

        GamePlayer gamePlayer = gameManager.getGamePlayer(targetPlayerName);
        if (gamePlayer == null) {
            moderator.sendMessage("§c✗ Error: Player data tidak ditemukan!");
            return true;
        }

        // Get player's current pijakan location
        Location pijakanLoc = gameManager.getPlayerLocation(targetPlayerName);
        if (pijakanLoc == null) {
            moderator.sendMessage("§c✗ Pijakan location tidak ditemukan! Pastikan /startboard sudah dijalankan.");
            return true;
        }

        // Teleport player back to pijakan
        targetPlayer.teleport(pijakanLoc);

        if (args.length > 0) {
            // Moderator teleported someone else
            moderator.sendMessage("§a✓ " + targetPlayerName + " di-teleport kembali ke pijakan!");
            moderator.sendMessage(String.format("§f  Lane %d  |  Posisi %d/10",
                gamePlayer.getLaneNumber(), gamePlayer.getScore()));
            targetPlayer.sendMessage("§a✓ Anda di-teleport moderator kembali ke pijakan!");
        } else {
            // Moderator teleported themselves
            moderator.sendMessage("§a✓ Anda di-teleport kembali ke pijakan!");
            moderator.sendMessage(String.format("§f  Lane %d  |  Posisi %d/10",
                gamePlayer.getLaneNumber(), gamePlayer.getScore()));
        }

        return true;
    }
}

