package yt.corazonid.siapakahAku.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.model.GamePlayer;
import yt.corazonid.siapakahAku.util.SoundUtil;

public class ChatListener implements Listener {
    private final GameManager gameManager;

    public ChatListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String message = event.getMessage().trim();

        // Check if player is registered and game is active
        if (!gameManager.isPlayerRegistered(playerName) || !gameManager.isGameActive()) {
            return;  // Not a game player or game not active
        }

        // Check if current question is set
        if (gameManager.getCurrentQuestion() == null) {
            return;
        }

        // RONDE SYSTEM: Check if player already answered correctly this round
        if (gameManager.hasPlayerAnsweredCorrectly(playerName)) {
            // Player sudah jawab benar - ignore jawaban mereka
            event.setCancelled(true);
            player.sendMessage("§6ℹ Anda sudah menjawab benar di ronde ini. Nantikan ronde berikutnya!");
            return;
        }

        // Check if answer is correct
        if (gameManager.isCorrectAnswer(message)) {
            // Cancel the public chat message
            event.setCancelled(true);

            // Process correct answer
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                Bukkit.getPluginManager().getPlugin("siapakahAku"),
                () -> {
                    GamePlayer gp = gameManager.getGamePlayer(playerName);
                    if (gp != null) {
                        // Register correct answer untuk ronde ini (prevent spam)
                        gameManager.registerPlayerCorrectAnswer(playerName);
                        int correctCount = gameManager.getCorrectAnswerCountThisRound();

                        // Teleport ke posisi berikutnya
                        gameManager.teleportPlayerToNextPosition(playerName);

                        // Broadcast success dengan correct count
                        Bukkit.broadcastMessage("§a✓ " + playerName + " §amenjawab dengan benar! (Jawaban ke-" + correctCount + ") (Progress: " + gp.getScore() + "/10)");

                        // Sound
                        SoundUtil.playCorrectAnswerSound(Bukkit.getServer());

                        // Check win
                        if (gp.hasWon()) {
                            gameManager.announceWinner(playerName);
                        }
                    }
                }
            );
        } else {
            // Wrong answer - check max wrong attempts per clue
            int wrongCount = gameManager.getWrongAnswerCountForPlayer(playerName);

            if (wrongCount >= 3) {
                // Already 3x wrong - block this answer
                event.setCancelled(true);
                player.sendMessage("§c✗ Anda sudah 3x salah di clue ini! Tunggu clue berikutnya.");
                SoundUtil.playWrongAnswerSound(player);
                return;
            }

            // Not yet 3x - increment and allow this wrong attempt
            event.setCancelled(true);
            gameManager.incrementWrongAnswerCount(playerName);
            int newWrongCount = gameManager.getWrongAnswerCountForPlayer(playerName);

            player.sendMessage("§c✗ Jawaban salah! (" + newWrongCount + "/3) Coba lagi...");
            SoundUtil.playWrongAnswerSound(player);
        }
    }
}

