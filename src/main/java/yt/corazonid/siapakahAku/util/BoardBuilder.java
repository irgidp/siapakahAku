package yt.corazonid.siapakahAku.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import java.util.*;

public class BoardBuilder {

    /**
     * Build 5 parallel lanes dengan 11 posisi masing-masing (0-10)
     * Setiap lane di-offset ke arah X
     * Setiap posisi di-offset ke arah Z (forward)
     *
     * @param world Target world
     * @param startLoc Base location (start dari sini)
     * @param spacing Jarak antar pijakan (default: 4 blocks)
     * @param laneSpacing Jarak antar lane (default: 5 blocks)
     * @return Map of posisi -> lokasi teleport (lane_number, position_number)
     */
    public static Map<String, Location> buildBoard(World world, Location startLoc, int spacing, int laneSpacing) {
        Map<String, Location> boardLocations = new LinkedHashMap<>();
        List<Block> builtBlocks = new ArrayList<>();

        // 5 lanes, 11 posisi per lane (0-10)
        for (int lane = 1; lane <= 5; lane++) {
            for (int pos = 0; pos <= 10; pos++) {
                // Hitung offset berdasarkan lane dan position
                int xOffset = (lane - 1) * laneSpacing;
                int zOffset = pos * spacing;

                Location blockLoc = startLoc.clone().add(xOffset, 0, zOffset);

                // Tentukan material berdasarkan posisi
                Material mat = getMaterialForPosition(pos);

                // Build 2x2 platform
                buildPlatform(blockLoc, mat, builtBlocks);

                // Simpan teleport location di atas platform (Y+0.5 = atas blok tanpa gap)
                Location teleportLoc = blockLoc.clone().add(0.5, 0.5, 0.5);

                // Key format: "lane_position" (contoh: "1_5" = lane 1, posisi 5)
                String key = lane + "_" + pos;
                boardLocations.put(key, teleportLoc);
            }
        }

        return boardLocations;
    }

    /**
     * Build 2x2 platform blok di bawah lokasi (Y-1)
     */
    private static void buildPlatform(Location center, Material mat, List<Block> builtBlocks) {
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                Location loc = center.clone().add(x, -1, z);
                Block block = loc.getBlock();
                block.setType(mat);
                builtBlocks.add(block);
            }
        }
    }

    /**
     * Tentukan material berdasarkan posisi dalam lane (0-10)
     */
    private static Material getMaterialForPosition(int pos) {
        if (pos == 0) {
            return Material.WHITE_CONCRETE;  // Start position
        } else if (pos == 10) {
            return Material.LIME_CONCRETE;   // Finish position
        } else if (pos % 2 == 0) {
            return Material.BLUE_CONCRETE;   // Even positions (2, 4, 6, 8)
        } else {
            return Material.PURPLE_CONCRETE; // Odd positions (1, 3, 5, 7, 9)
        }
    }

    /**
     * Hapus semua blok yang sudah di-build
     */
    public static void clearBoard(List<Block> blocks) {
        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
    }
}

