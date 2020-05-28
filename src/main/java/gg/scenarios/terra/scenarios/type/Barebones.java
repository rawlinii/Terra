package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Barebones extends Scenario {


    private boolean enabled = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if (event.getBlock().getType() == Material.DIAMOND_ORE || event.getBlock().getType() == Material.GOLD_ORE || event.getBlock().getType() == Material.REDSTONE_ORE || event.getBlock().getType() == Material.IRON_ORE){
            Block block =event.getBlock();
            event.setCancelled(true);
            block.setType(Material.AIR);
            block.getState().update();
            block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.4, 0.5), new ItemStack(Material.IRON_INGOT));
            block.getWorld().spawn(block.getLocation(), ExperienceOrb.class).setExperience(new Random().nextInt(4));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1));
        event.getDrops().add(new ItemStack(Material.DIAMOND, 1));
        event.getDrops().add(new ItemStack(Material.ARROW, 16));
        event.getDrops().add(new ItemStack(Material.STRING, 2));
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Cow) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.COOKED_BEEF, 3));
            e.getDrops().add(new ItemStack(Material.LEATHER, 1));
            if (Terra.getInstance().getScenarioManager().getScenarioByName("Bombers").isEnabled()){
                e.getDrops().add(new ItemStack(Material.TNT, 1));
            }
        } else if (e.getEntity() instanceof Chicken) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 3));
            e.getDrops().add(new ItemStack(Material.FEATHER, 1));
            if (Terra.getInstance().getScenarioManager().getScenarioByName("Bombers").isEnabled()){
                e.getDrops().add(new ItemStack(Material.TNT, 1));
            }
        } else if (e.getEntity() instanceof Horse) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.LEATHER, 1));
            if (Terra.getInstance().getScenarioManager().getScenarioByName("Bombers").isEnabled()){
                e.getDrops().add(new ItemStack(Material.TNT, 1));
            }
        } else if (e.getEntity() instanceof Pig) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.GRILLED_PORK, 3));
            if (Terra.getInstance().getScenarioManager().getScenarioByName("Bombers").isEnabled()){
                e.getDrops().add(new ItemStack(Material.TNT, 1));
            }
        } else if (e.getEntity() instanceof Sheep) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.GRILLED_PORK, 3));
            e.getDrops().add(new ItemStack(Material.WOOL, 1));
            if (Terra.getInstance().getScenarioManager().getScenarioByName("Bombers").isEnabled()){
                e.getDrops().add(new ItemStack(Material.TNT, 1));
            }
        }
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        ItemStack item = event.getRecipe().getResult();
        if (item.getType() == Material.ENCHANTMENT_TABLE ||item.getType() == Material.GOLDEN_APPLE || item.getType() == Material.ANVIL) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
return true;
    }

    @Override
    public String getDefaultName() {
        return "Barebones";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.BONE).setName((enabled ? ChatColor.GREEN + "Barebones" : ChatColor.RED + "Barebones")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Barebones";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Food is cooked, Mining any ore except coal or iron will drop an iron ingot");
        explain.add(ChatColor.BLUE + " - Nether is off, Iron is the highest tier obtainable through mining");
        explain.add(ChatColor.BLUE + " - You cannot craft enchantment tables, golden apples or anvils");
        explain.add(ChatColor.BLUE + " - When players die they drop 1 diamond, 1 golden apple, 15 arrows, and 2 string");
        return explain;
    }

    @Override
    public String getState() {
        if (enabled) {
            return ChatColor.GREEN + "Barebones";
        } else {
            return ChatColor.RED + "Barebones";
        }
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemCreator(Material.BONE).setName((enabled ? ChatColor.GREEN + "Barebones" : ChatColor.RED + "Barebones")).setLore(getScenarioExplanation()).get();
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