package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Timber extends Scenario {

    private boolean enabled = false;

    @EventHandler
    public void timberEvent(BlockBreakEvent event){
        if (Terra.getInstance().getGameManager().getGameState() == GameState.STARTED) {
            if (event.getBlock().getType() == (Material.LOG) || event.getBlock().getType() == Material.LOG_2) {
                Block block = event.getBlock();
                breakBlock(block, event, event.getPlayer());
            }
        }else{
            event.setCancelled(true);
        }
    }
    private void breakBlock(Block block1, BlockBreakEvent event, Player player) {
        for (BlockFace blockFace : BlockFace.values()) {
            if (block1.getRelative(blockFace).getType() == Material.LOG || block1.getRelative(blockFace).getType() == Material.LOG_2) {
                Block block = block1.getRelative(blockFace);
                block.breakNaturally();
                block1.breakNaturally();
                player.playSound(player.getLocation(), Sound.DIG_WOOD, 10, 2);
                breakBlock(block, event, player);

                if (getRandomValue(Terra.getInstance().getGameManager().getRandom(),0, 100, 1) <= 0.3) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
                }
            }
        }
    }

    private double getRandomValue(final Random random,
                                  final int lowerBound,
                                  final int upperBound,
                                  final int decimalPlaces){

        if(lowerBound < 0 || upperBound <= lowerBound || decimalPlaces < 0){
            throw new IllegalArgumentException("Put error message here");
        }

        final double dbl =
                ((random == null ? new Random() : random).nextDouble() //
                        * (upperBound - lowerBound))
                        + lowerBound;
        return Double.parseDouble((String.format("%." + decimalPlaces + "f", dbl)).replace(",", "."));
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Timber";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.WOOD_AXE).setName((enabled ? ChatColor.GREEN + "Timber" : ChatColor.RED + "Timber")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Timber";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Breaking the bottom log with break the entire tree");
        return explain;
    }

    @Override
    public String getState() {
        if (enabled) {
            return ChatColor.GREEN + "Enabled";
        } else {
            return ChatColor.RED + "Disabled";
        }
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemCreator(Material.WOOD_AXE).setName((enabled ? ChatColor.GREEN + "Timber" : ChatColor.RED + "Timber")).setLore(getScenarioExplanation()).get();

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}