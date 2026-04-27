package yt.corazonid.siapakahAku.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import yt.corazonid.siapakahAku.manager.GameManager;

public class RegisCommand implements CommandExecutor {
    private final GameManager gameManager;

    public RegisCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("siapakahaku.moderator")) {
            sender.sendMessage("§cAnda tidak memiliki izin untuk menggunakan command ini!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /regis <playername>");
            return true;
        }

        String playerName = args[0];

        if (gameManager.registerPlayer(playerName)) {
            sender.sendMessage("§a✓ " + playerName + " berhasil terdaftar");
        } else {
            sender.sendMessage("§c✗ Gagal mendaftarkan " + playerName + " (tidak ada lane kosong atau sudah terdaftar)");
        }

        return true;
    }
}

