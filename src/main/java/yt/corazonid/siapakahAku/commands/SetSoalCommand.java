package yt.corazonid.siapakahAku.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import yt.corazonid.siapakahAku.manager.GameManager;

public class SetSoalCommand implements CommandExecutor {
    private final GameManager gameManager;

    public SetSoalCommand(GameManager gameManager) {
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
            sender.sendMessage("§cUsage: /setsoal <question_id>");
            sender.sendMessage("§eAvailable: " + gameManager.getAvailableQuestions());
            return true;
        }

        String questionId = args[0];

        if (gameManager.loadQuestion(questionId)) {
            sender.sendMessage("§a✓ Soal '" + questionId + "' berhasil dimuat");
            sender.sendMessage("§6ℹ Ronde system: Jawaban benar = maju 1 pijakan, Salah ≤3x = tunggu clue berikutnya");
        } else {
            sender.sendMessage("§c✗ Soal '" + questionId + "' tidak ditemukan");
            sender.sendMessage("§eAvailable: " + gameManager.getAvailableQuestions());
        }

        return true;
    }
}

