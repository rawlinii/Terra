package gg.scenarios.terra.world;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class IncreasedCaneRates extends BlockPopulator {
    private int canePatchChance;
    private Material cane;
    private BlockFace[] faces;

    public IncreasedCaneRates() {
        this.faces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
        this.canePatchChance = 25;
        this.cane = Material.SUGAR_CANE_BLOCK;
    }

    public void populate(final World world, final Random random, final Chunk source) {
        if (random.nextInt(100) < this.canePatchChance) {
            for (int i = 0; i < 16; ++i) {
                Block block;
                if (random.nextBoolean()) {
                    block = this.getHighestBlock(source, random.nextInt(16), i);
                } else {
                    block = this.getHighestBlock(source, i, random.nextInt(16));
                }
                if (block.getType() == Material.GRASS || block.getType() == Material.SAND) {
                    this.createCane(block, random);
                }
            }
        }
    }

    private void createCane(final Block block, final Random rand) {
        boolean create = false;
        for (final BlockFace face : this.faces) {
            if (block.getRelative(face).getType().name().toLowerCase().contains("water")) {
                create = true;
            }
        }
        if (!create) {
            return;
        }
        for (int i = 1; i < rand.nextInt(4) + 3; ++i) {
            block.getRelative(0, i, 0).setType(this.cane);
        }
    }

    private Block getHighestBlock(final Chunk chunk, final int x, final int z) {
        Block block = null;
        for (int i = 127; i >= 0; --i) {
            if ((block = chunk.getBlock(x, i, z)).getTypeId() != 0) {
                return block;
            }
        }
        return block;
    }
}