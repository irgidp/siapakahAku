package yt.corazonid.siapakahAku.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import yt.corazonid.siapakahAku.manager.GameManager;

public class EntityDamageListener implements Listener {
    private final GameManager gameManager;

    public EntityDamageListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Prevent player-vs-player damage during game (PvP prevention)
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();

            // Check if both players are registered in the game
            if (gameManager.isPlayerRegistered(damager.getName()) &&
                gameManager.isPlayerRegistered(damaged.getName())) {
                // Cancel PvP damage
                event.setCancelled(true);
                damager.sendMessage("§c✗ PvP dinonaktifkan! Fokus menjawab soal saja.");
            }
        }
    }
}

