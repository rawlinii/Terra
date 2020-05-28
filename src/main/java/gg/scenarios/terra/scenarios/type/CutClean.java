package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CutClean extends Scenario {


    private boolean enabled = false;


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (block.getType() == Material.IRON_ORE) {
            e.setCancelled(true);
            block.setType(Material.AIR);
            block.getState().update();
            block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.4, 0.5), new ItemStack(Material.IRON_INGOT));
            block.getWorld().spawn(block.getLocation(), ExperienceOrb.class).setExperience(new Random().nextInt(4));
        } else if (block.getType() == Material.GOLD_ORE) {
            e.setCancelled(true);
            block.setType(Material.AIR);
            block.getState().update();
            block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.4, 0.5), new ItemStack(Material.GOLD_INGOT));
            block.getWorld().spawn(block.getLocation(), ExperienceOrb.class).setExperience(new Random().nextInt(6));
        } else if(block.getType()==Material.GRAVEL){
            e.setCancelled(true);
            block.setType(Material.AIR);
            block.getState().update();
            block.getWorld().dropItemNaturally(block.getLocation().add(0.5, .4, 0.5), new ItemStack(Material.FLINT));
        }

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
        } else if (e.getEntity() instanceof Pig ) {
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

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.IRON_INGOT).setName((enabled ? ChatColor.GREEN + "Cutclean" : ChatColor.RED + "CutClean")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
//        return !clazz.getName().equalsIgnoreCase(SlutClean.class.getName()) || !clazz.getName().equalsIgnoreCase(Barebones.class.getName()) || !clazz.getName().equalsIgnoreCase(TripleOres.class.getName());=
    return true;}

    @Override
    public String getDefaultName() {
        return "CutClean";
    }

    @Override
    public String getName() {
        return "CutClean";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Ores are 100% smelted");
        explain.add(ChatColor.BLUE + " - Food is 100% cooked");
        explain.add(ChatColor.BLUE + " - No furnace needed");
        explain.add(ChatColor.BLUE + " - Flint/Apple rates are 100%");
        return explain;
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemCreator(Material.IRON_INGOT).setName((enabled ? ChatColor.GREEN + "Cutclean" : ChatColor.RED + "CutClean")).setLore(getScenarioExplanation()).get();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getState() {
        if (enabled) {
            return ChatColor.GREEN + "Enabled";
        } else {
            return ChatColor.RED + "Disabled";
        }
    }
}