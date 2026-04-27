package yt.corazonid.siapakahAku.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.model.GamePlayer;

public class ListPlayerCommand implements CommandExecutor {
    private final GameManager gameManager;

    public ListPlayerCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("siapakahaku.moderator")) {
            sender.sendMessage("§cAnda tidak memiliki izin untuk menggunakan command ini!");
            return true;
        }

        sender.sendMessage("§e━━━━━━━━━━━━━━━━━━━━━━━━━━");
        sender.sendMessage("§e     DAFTAR PLAYER");
        sender.sendMessage("§e━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (gameManager.getRegisteredPlayers().isEmpty()) {
            sender.sendMessage("§cTidak ada player yang terdaftar");
        } else {
            for (GamePlayer gp : gameManager.getRegisteredPlayers()) {
                sender.sendMessage(String.format("§f  • §b%s §f| §eLintasan %d §f| §aScore: %d/10",
                    gp.getPlayerName(), gp.getLaneNumber(), gp.getScore()));
            }
        }

        sender.sendMessage("§e━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return true;
    }
}

