package yt.corazonid.siapakahAku.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.model.Question;

public class JawabanCommand implements CommandExecutor {
    private final GameManager gameManager;

    public JawabanCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("siapakahaku.moderator")) {
            sender.sendMessage("§cAnda tidak memiliki izin untuk menggunakan command ini!");
            return true;
        }

        Question question = gameManager.getCurrentQuestion();
        if (question == null) {
            sender.sendMessage("§c✗ Tidak ada soal yang dimuat");
            return true;
        }

        gameManager.revealAnswer();
        sender.sendMessage("§a✓ Jawaban diungkap");

        return true;
    }
}

