package yt.corazonid.siapakahAku.commands;

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
        // Check permission
        if (!sender.hasPermission("siapakahaku.moderator")) {
            sender.sendMessage("§cAnda tidak memiliki izin untuk menggunakan command ini!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCommand ini hanya bisa dijalankan oleh player!");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();

        // Check if player is registered
        if (!gameManager.isPlayerRegistered(playerName)) {
            player.sendMessage("§c✗ Anda tidak terdaftar dalam game ini!");
            return true;
        }

        GamePlayer gamePlayer = gameManager.getGamePlayer(playerName);
        if (gamePlayer == null) {
            player.sendMessage("§c✗ Error: Player data tidak ditemukan!");
            return true;
        }

        // Get player's current pijakan location
        Location pijakanLoc = gameManager.getPlayerLocation(playerName);
        if (pijakanLoc == null) {
            player.sendMessage("§c✗ Pijakan location tidak ditemukan! Pastikan /startboard sudah dijalankan.");
            return true;
        }

        // Teleport player back to pijakan
        player.teleport(pijakanLoc);
        player.sendMessage("§a✓ Anda di-teleport kembali ke pijakan!");
        player.sendMessage(String.format("§f  Lane %d  |  Posisi %d/10",
            gamePlayer.getLaneNumber(), gamePlayer.getScore()));

        return true;
    }
}

