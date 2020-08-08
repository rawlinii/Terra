package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BloodDiamonds extends Scenario {

    private boolean enabled = false;
    @EventHandler
    public void onDamage(BlockBreakEvent event){
        if (event.getBlock().getType().equals(Material.DIAMOND_ORE)){
            event.getPlayer().damage(1D);
        }
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Blood Diamonds";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.REDSTONE).setName((enabled ? ChatColor.GREEN + "Blood Diamonds" : ChatColor.RED + "Blood Diamonds")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Blood Diamonds";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - You will take half a heart of damage when you mine diamond");
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
        return new ItemCreator(Material.REDSTONE).setName((enabled ? ChatColor.GREEN + "Blood Diamonds" : ChatColor.RED + "Blood Diamonds")).setLore(getScenarioExplanation()).get();
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