package yt.corazonid.siapakahAku.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import yt.corazonid.siapakahAku.SiapakahAku;
import yt.corazonid.siapakahAku.model.GamePlayer;
import yt.corazonid.siapakahAku.model.Question;
import yt.corazonid.siapakahAku.util.TitleUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class GameManager {
    private final SiapakahAku plugin;
    private final Map<String, GamePlayer> registeredPlayers = new LinkedHashMap<>();
    private final Set<Integer> occupiedLanes = new HashSet<>();
    private final Map<String, Question> questions = new HashMap<>();
    private final Map<String, Location> boardLocations = new LinkedHashMap<>();
    private final List<Block> builtBlocks = new ArrayList<>();

    private boolean gameActive = false;
    private Question currentQuestion = null;
    private boolean answerRevealed = false;
    private int correctAnswerCountThisRound = 0;  // Track berapa yang jawab benar untuk soal ini

    // Ronde system - track jawaban per player
    private final Set<String> playersAnsweredCorrectlyThisRound = new HashSet<>();  // Players yang sudah benar di ronde ini
    private final Map<String, Integer> wrongAnswerCountPerPlayer = new HashMap<>();  // Track wrong answer count per player per clue
    private int currentClueNumber = 0;  // Track clue mana sekarang (1, 2, 3)

    public GameManager(SiapakahAku plugin) {
        this.plugin = plugin;
        loadQuestions();
    }

    /**
     * Load questions from questions.yml
     */
    private void loadQuestions() {
        try {
            InputStream inputStream = plugin.getResource("questions.yml");
            if (inputStream == null) {
                Bukkit.getLogger().warning("[TebakSiapakahAku] questions.yml not found!");
                return;
            }

            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            if (config.contains("questions")) {
                for (String key : config.getConfigurationSection("questions").getKeys(false)) {
                    String clue1 = config.getString("questions." + key + ".clue1", "");
                    String clue2 = config.getString("questions." + key + ".clue2", "");
                    String clue3 = config.getString("questions." + key + ".clue3", "");
                    String answer = config.getString("questions." + key + ".answer", "");

                    questions.put(key.toLowerCase(), new Question(key, clue1, clue2, clue3, answer));
                }
                Bukkit.getLogger().info("[TebakSiapakahAku] Loaded " + questions.size() + " questions");
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[TebakSiapakahAku] Error loading questions: " + e.getMessage());
        }
    }

    /**
     * Register a player to an available lane
     */
    public boolean registerPlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            return false;
        }

        if (registeredPlayers.containsKey(playerName)) {
            return false;  // Already registered
        }

        // Find available lane (1-5)
        int availableLane = -1;
        for (int i = 1; i <= 5; i++) {
            if (!occupiedLanes.contains(i)) {
                availableLane = i;
                break;
            }
        }

        if (availableLane == -1) {
            return false;  // No available lanes
        }

        GamePlayer gamePlayer = new GamePlayer(playerName, player, availableLane);
        registeredPlayers.put(playerName, gamePlayer);
        occupiedLanes.add(availableLane);

        Bukkit.broadcastMessage("§a✓ " + playerName + " terdaftar di Lintasan " + availableLane);
        return true;
    }

    /**
     * Unregister a player
     */
    public boolean unregisterPlayer(String playerName) {
        GamePlayer gamePlayer = registeredPlayers.remove(playerName);
        if (gamePlayer != null) {
            occupiedLanes.remove(gamePlayer.getLaneNumber());
            Bukkit.broadcastMessage("§c✗ " + playerName + " telah dihapus");
            return true;
        }
        return false;
    }

    /**
     * Get registered player by name
     */
    public GamePlayer getGamePlayer(String playerName) {
        return registeredPlayers.get(playerName);
    }

    /**
     * Get all registered players
     */
    public Collection<GamePlayer> getRegisteredPlayers() {
        return registeredPlayers.values();
    }

    /**
     * Check if a player is registered
     */
    public boolean isPlayerRegistered(String playerName) {
        return registeredPlayers.containsKey(playerName);
    }

    /**
     * Start game and freeze all players
     */
    public void startGame() {
        gameActive = true;
        for (GamePlayer gp : registeredPlayers.values()) {
            gp.setMovementFrozen(true);
        }
        Bukkit.broadcastMessage("§e═══════════════════");
        Bukkit.broadcastMessage("§6✦ GAME DIMULAI! ✦");
        Bukkit.broadcastMessage("§e═══════════════════");
    }

    /**
     * Stop game
     */
    public void stopGame() {
        gameActive = false;
        for (GamePlayer gp : registeredPlayers.values()) {
            gp.setMovementFrozen(false);
        }
        currentQuestion = null;
        answerRevealed = false;
    }

    /**
     * Load a question by ID
     */
    public boolean loadQuestion(String questionId) {
        Question q = questions.get(questionId.toLowerCase());
        if (q != null) {
            currentQuestion = q;
            answerRevealed = false;
            correctAnswerCountThisRound = 0;
            currentClueNumber = 0;

            // Reset ronde state untuk soal baru
            playersAnsweredCorrectlyThisRound.clear();
            wrongAnswerCountPerPlayer.clear();

            // JANGAN broadcast ke chat player - hide soal name!
            Bukkit.getLogger().info("[TebakSiapakahAku] Soal baru dimulai: " + questionId.toUpperCase());
            return true;
        }
        return false;
    }

    /**
     * Get current question
     */
    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    /**
     * Check if answer is correct
     */
    public boolean isCorrectAnswer(String answer) {
        if (currentQuestion == null) {
            return false;
        }
        return currentQuestion.isCorrectAnswer(answer);
    }

    /**
     * Process correct answer for a player
     */
    public void processCorrectAnswer(String playerName) {
        GamePlayer gp = registeredPlayers.get(playerName);
        if (gp != null) {
            gp.advancePosition();
            Bukkit.broadcastMessage("§a✓ " + playerName + " §amenjawab dengan benar! (Ke-" + gp.getScore() + "/10)");

            // Check if player won
            if (gp.hasWon()) {
                announceWinner(playerName);
            }
        }
    }

    /**
     * Announce winner and end game
     */
    public void announceWinner(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            Bukkit.broadcastMessage("§6");
            Bukkit.broadcastMessage("§6╔═══════════════════╗");
            Bukkit.broadcastMessage("§6║     PEMENANG!      ║");
            Bukkit.broadcastMessage("§6║  " + playerName + "  ║");
            Bukkit.broadcastMessage("§6╚═══════════════════╝");
            Bukkit.broadcastMessage("§6");

            TitleUtil.broadcastTitle(player.getServer(), "§6★ PEMENANG ★", "§e" + playerName);

            // Spawn fireworks at player location
            for (int i = 0; i < 10; i++) {
                new org.bukkit.util.Vector(Math.random() - 0.5, 0.5, Math.random() - 0.5);
            }

            stopGame();
        }
    }

    /**
     * Reveal current answer
     */
    public void revealAnswer() {
        if (currentQuestion != null && !answerRevealed) {
            answerRevealed = true;
            String answer = currentQuestion.getAnswer();
            Bukkit.broadcastMessage("§b━━━━━━━━━━━━━━━━━");
            Bukkit.broadcastMessage("§bJAWABAN: §f" + answer.toUpperCase());
            Bukkit.broadcastMessage("§b━━━━━━━━━━━━━━━━━");
        }
    }

    /**
     * Get list of all questions
     */
    public Set<String> getAvailableQuestions() {
        return questions.keySet();
    }

    /**
     * Check if game is active
     */
    public boolean isGameActive() {
        return gameActive;
    }

    /**
     * Set board locations (dipanggil oleh command startboard)
     */
    public void setBoardLocations(Map<String, Location> locations) {
        this.boardLocations.putAll(locations);
    }

    /**
     * Get pijakan location untuk player
     */
    public Location getPlayerLocation(String playerName) {
        GamePlayer gp = registeredPlayers.get(playerName);
        if (gp == null) {
            return null;
        }
        String key = gp.getBoardKey();
        return boardLocations.get(key);
    }

    /**
     * Teleport player ke pijakan berikutnya
     */
    public void teleportPlayerToNextPosition(String playerName) {
        GamePlayer gp = registeredPlayers.get(playerName);
        if (gp == null) {
            return;
        }

        gp.advancePosition();
        String key = gp.getBoardKey();
        Location loc = boardLocations.get(key);

        if (loc != null && gp.getPlayer() != null) {
            gp.getPlayer().teleport(loc);
            gp.getPlayer().playSound(loc, org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 0.8f, 1.5f);
        }
    }

    /**
     * Add block yang sudah di-build (untuk cleanup nanti)
     */
    public void addBuiltBlock(Block block) {
        builtBlocks.add(block);
    }

    /**
     * Clear semua built blocks
     */
    public void clearAllBlocks() {
        for (Block block : builtBlocks) {
            block.setType(org.bukkit.Material.AIR);
        }
        builtBlocks.clear();
    }

    /**
     * Register correct answer untuk soal ini
     */
    public void registerCorrectAnswer(String playerName) {
        correctAnswerCountThisRound++;
    }

    /**
     * Get jumlah yang jawab benar untuk soal ini
     */
    public int getCorrectAnswerCountThisRound() {
        return correctAnswerCountThisRound;
    }

    /**
     * Check if player sudah jawab benar di ronde ini
     */
    public boolean hasPlayerAnsweredCorrectly(String playerName) {
        return playersAnsweredCorrectlyThisRound.contains(playerName);
    }

    /**
     * Register player sebagai yang sudah jawab benar di ronde ini
     */
    public void registerPlayerCorrectAnswer(String playerName) {
        playersAnsweredCorrectlyThisRound.add(playerName);
        correctAnswerCountThisRound++;
    }

    /**
     * Get wrong answer count untuk player di clue saat ini
     */
    public int getWrongAnswerCountForPlayer(String playerName) {
        return wrongAnswerCountPerPlayer.getOrDefault(playerName, 0);
    }

    /**
     * Increment wrong answer count untuk player
     */
    public void incrementWrongAnswerCount(String playerName) {
        wrongAnswerCountPerPlayer.put(playerName, getWrongAnswerCountForPlayer(playerName) + 1);
    }

    /**
     * Reset wrong answer counts saat clue baru
     */
    public void resetWrongAnswerCountsForNewClue() {
        wrongAnswerCountPerPlayer.clear();
    }

    /**
     * Set current clue number (1, 2, 3)
     */
    public void setCurrentClueNumber(int clueNum) {
        this.currentClueNumber = clueNum;
    }

    /**
     * Get current clue number
     */
    public int getCurrentClueNumber() {
        return currentClueNumber;
    }
}

