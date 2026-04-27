package yt.corazonid.siapakahAku.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.model.GamePlayer;
import yt.corazonid.siapakahAku.util.BoardBuilder;

import java.util.Map;

public class StartBoardCommand implements CommandExecutor {
    private final GameManager gameManager;

    public StartBoardCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("siapakahaku.moderator")) {
            sender.sendMessage("§cAnda tidak memiliki izin untuk menggunakan command ini!");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCommand ini hanya bisa dijalankan oleh player!");
            return true;
        }

        if (gameManager.getRegisteredPlayers().isEmpty()) {
            sender.sendMessage("§c✗ Tidak ada player yang terdaftar!");
            return true;
        }

        sender.sendMessage("§e⏳ Spawn pijakan...");

        // Build board: 5 lanes x 11 posisi (0-10)
        // Jarak antar pijakan: 4 blocks
        // Jarak antar lane: 5 blocks
        Location startLoc = player.getLocation().add(2, 0, 2);
        Map<String, Location> boardLocations = BoardBuilder.buildBoard(
            player.getWorld(),
            startLoc,
            4,  // spacing antar posisi (forward/Z axis)
            5   // lane offset (left/X axis)
        );

        gameManager.setBoardLocations(boardLocations);

        sender.sendMessage("§a✓ Board berhasil di-spawn! (" + boardLocations.size() + " pijakan)");

        // Teleport semua player ke pijakan 0 mereka (start position)
        for (GamePlayer gp : gameManager.getRegisteredPlayers()) {
            String key = gp.getBoardKey();  // "lane_position" format
            Location loc = boardLocations.get(key);
            if (loc != null && gp.getPlayer() != null) {
                gp.getPlayer().teleport(loc);
                Bukkit.broadcastMessage("§f  • §b" + gp.getPlayerName() + " §fdi teleport ke lane " + gp.getLaneNumber() + ", posisi start");
            }
        }

        sender.sendMessage("§a✓ Semua player di-teleport ke pijakan start!");

        return true;
    }
}

