package yt.corazonid.siapakahAku.model;

import org.bukkit.entity.Player;

public class GamePlayer {
    private final String playerName;
    private final Player player;
    private final int laneNumber;        // 1-5
    private int currentPosition;         // 0-10 (progress on lane, 0=start, 10=finish)
    private boolean isMovementFrozen;

    public GamePlayer(String playerName, Player player, int laneNumber) {
        this.playerName = playerName;
        this.player = player;
        this.laneNumber = laneNumber;
        this.currentPosition = 0;        // Start at position 0 (start block)
        this.isMovementFrozen = false;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Player getPlayer() {
        return player;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public int getCurrentPosition() {
        return currentPosition;  // 0-10
    }

    public void setCurrentPosition(int position) {
        this.currentPosition = Math.max(0, Math.min(position, 10));  // Clamp 0-10
    }

    public void advancePosition() {
        if (currentPosition < 10) {
            currentPosition++;
        }
    }

    public boolean isMovementFrozen() {
        return isMovementFrozen;
    }

    public void setMovementFrozen(boolean frozen) {
        this.isMovementFrozen = frozen;
    }

    public int getScore() {
        return currentPosition;  // Score is 0-10
    }

    public boolean hasWon() {
        return currentPosition >= 10;  // Position 10 = won
    }

    /**
     * Get board key untuk lookup pijakan lokasi
     * Format: "lane_position" (contoh: "2_5" = lane 2, position 5)
     */
    public String getBoardKey() {
        return laneNumber + "_" + currentPosition;
    }

    @Override
    public String toString() {
        return String.format("%s (Lane %d, Pos %d/10)", playerName, laneNumber, getScore());
    }
}

