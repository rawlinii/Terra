package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

public class TimeBomb extends Scenario {
    private boolean enabled= false;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final Location loc = player.getLocation().clone();


        Block block = loc.getBlock();

        block = block.getRelative(BlockFace.DOWN);
        block.setType(Material.CHEST);

        Chest chest = (Chest) block.getState();

        block = block.getRelative(BlockFace.NORTH);
        block.setType(Material.CHEST);

        for (ItemStack item : event.getDrops()) {
            if (item == null || item.getType() == Material.AIR) {
                continue;
            }

            chest.getInventory().addItem(item);
        }
        ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta gMeta = goldenHead.getItemMeta();
        gMeta.setDisplayName(AQUA + "Golden Head");
        gMeta.setLore(Arrays.asList("You've crafted a Golden Head!", "Consuming this will grant you even greater effects", "than a normal Golden Apple!"));
        goldenHead.setItemMeta(gMeta);
        chest.getInventory().addItem(goldenHead);

        event.getDrops().clear();

        final ArmorStand stand = player.getWorld().spawn(chest.getLocation().clone().add(0.5, 1, 0), ArmorStand.class);

        stand.setCustomNameVisible(true);
        stand.setSmall(true);

        stand.setGravity(false);
        stand.setVisible(false);

        stand.setMarker(true);

        new BukkitRunnable() {
            private int time = 30 + 1; // add one for countdown.

            public void run() {
                time--;

                if (time == 0) {

                    Terra.getInstance().getUtils().broadcast(Terra.getInstance().getReference().getScenario()
                            .replace("Scenario", "Timebomb")+"§a" + player.getName() + "'s §fcorpse has exploded!");
                    loc.getBlock().setType(Material.AIR);

                    loc.getWorld().createExplosion(loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5, 10, false, true);
                    loc.getWorld().strikeLightning(loc); // Using actual lightning to kill the items.

                    stand.remove();
                    cancel();
                    return;
                }
                else if (time == 1) {
                    stand.setCustomName("§4" + time + "s");
                }
                else if (time == 2) {
                    stand.setCustomName("§c" + time + "s");
                }
                else if (time == 3) {
                    stand.setCustomName("§6" + time + "s");
                }
                else if (time <= 15) {
                    stand.setCustomName("§e" + time + "s");
                }
                else {
                    stand.setCustomName("§a" + time + "s");
                }
            }
        }.runTaskTimer(Terra.getInstance(), 0, 20);
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Timebomb";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.TNT).setName((enabled ? GREEN + "Timebomb" : RED + "Timebomb")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Timebomb";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(YELLOW + "Explanation: ");
        explain.add(BLUE + " - Players items go in a chest and blows up after 30 seconds");
        return explain;
    }

    @Override
    public String getState() {
        if (enabled) {
            return GREEN + "Enabled";
        } else {
            return RED + "Disabled";
        }
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemCreator(Material.TNT).setName((enabled ? GREEN + "Timebomb" : RED + "Timebomb")).setLore(getScenarioExplanation()).get();
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