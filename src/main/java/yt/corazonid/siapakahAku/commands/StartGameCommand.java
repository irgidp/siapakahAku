package yt.corazonid.siapakahAku.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.util.SoundUtil;

public class StartGameCommand implements CommandExecutor {
    private final GameManager gameManager;

    public StartGameCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("siapakahaku.moderator")) {
            sender.sendMessage("§cAnda tidak memiliki izin untuk menggunakan command ini!");
            return true;
        }

        if (gameManager.getRegisteredPlayers().isEmpty()) {
            sender.sendMessage("§c✗ Tidak ada player yang terdaftar!");
            return true;
        }

        gameManager.startGame();
        SoundUtil.playGameStartSound(sender.getServer());

        return true;
    }
}

