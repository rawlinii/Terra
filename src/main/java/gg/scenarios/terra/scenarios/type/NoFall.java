package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  NoFall extends Scenario {


    private boolean enabled = false;

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntityType().equals(EntityType.PLAYER)){
            if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                event.setCancelled(true);
            }
        }
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "NoFall";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.DIAMOND_BOOTS).setName((enabled ? ChatColor.GREEN + "NoFall" : ChatColor.RED + "NoFall")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "NoFall";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Fall damage is disabled");
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
        return new ItemCreator(Material.DIAMOND_BOOTS).setName((enabled ? ChatColor.GREEN + "NoFall" : ChatColor.RED + "NoFall")).setLore(getScenarioExplanation()).get();
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