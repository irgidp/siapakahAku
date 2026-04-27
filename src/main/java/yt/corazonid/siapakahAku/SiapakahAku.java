package yt.corazonid.siapakahAku;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import yt.corazonid.siapakahAku.manager.GameManager;
import yt.corazonid.siapakahAku.commands.*;
import yt.corazonid.siapakahAku.listener.*;

public final class SiapakahAku extends JavaPlugin {
    private static GameManager gameManager;

    @Override
    public void onEnable() {
        // Initialize GameManager
        gameManager = new GameManager(this);

        // Register commands
        getCommand("regis").setExecutor(new RegisCommand(gameManager));
        getCommand("unregis").setExecutor(new UnregisCommand(gameManager));
        getCommand("listplayer").setExecutor(new ListPlayerCommand(gameManager));
        getCommand("startboard").setExecutor(new StartBoardCommand(gameManager));
        getCommand("startgame").setExecutor(new StartGameCommand(gameManager));
        getCommand("setsoal").setExecutor(new SetSoalCommand(gameManager));
        getCommand("clue1").setExecutor(new ClueCommand(gameManager, 1));
        getCommand("clue2").setExecutor(new ClueCommand(gameManager, 2));
        getCommand("clue3").setExecutor(new ClueCommand(gameManager, 3));
        getCommand("jawaban").setExecutor(new JawabanCommand(gameManager));
        getCommand("cekposisi").setExecutor(new CekposisiCommand(gameManager));

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new ChatListener(gameManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(gameManager), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(gameManager), this);

        getLogger().info("§a═══════════════════════════════");
        getLogger().info("§a  TebakSiapakahAku Plugin Enabled!");
        getLogger().info("§a═══════════════════════════════");
    }

    @Override
    public void onDisable() {
        getLogger().info("§cTebakSiapakahAku Plugin Disabled!");
    }

    public static GameManager getGameManager() {
        return gameManager;
    }
}
