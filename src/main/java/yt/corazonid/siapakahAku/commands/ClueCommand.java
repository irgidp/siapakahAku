package yt.corazonid.siapakahAku.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.model.Question;
import yt.corazonid.siapakahAku.util.TitleUtil;

public class ClueCommand implements CommandExecutor {
    private final GameManager gameManager;
    private final int clueNumber;

    public ClueCommand(GameManager gameManager, int clueNumber) {
        this.gameManager = gameManager;
        this.clueNumber = clueNumber;
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
            sender.sendMessage("§c✗ Tidak ada soal yang dimuat. Gunakan /setsoal <question_id>");
            return true;
        }

        String clueText = "";
        switch (clueNumber) {
            case 1:
                clueText = question.getClue1();
                break;
            case 2:
                clueText = question.getClue2();
                // Reset wrong counts untuk clue 2 (player yg sudah 3x di clue 1 bisa coba lagi)
                gameManager.resetWrongAnswerCountsForNewClue();
                break;
            case 3:
                clueText = question.getClue3();
                // Reset wrong counts untuk clue 3 (player yg sudah 3x di clue 2 bisa coba lagi)
                gameManager.resetWrongAnswerCountsForNewClue();
                break;
        }

        // Update current clue number
        gameManager.setCurrentClueNumber(clueNumber);

        // Broadcast title to all players
        TitleUtil.broadcastTitle(sender.getServer(), "§6CLUE " + clueNumber, "§f" + clueText);
        sender.sendMessage("§a✓ Clue " + clueNumber + " ditampilkan");

        return true;
    }
}

