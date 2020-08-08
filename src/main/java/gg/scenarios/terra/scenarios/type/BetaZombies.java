package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BetaZombies extends Scenario {


    private boolean enabled = false;

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {

        if (event.getEntity() instanceof Zombie) {
            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), new ItemStack(Material.FEATHER, 2));
        }
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Beta Zombies";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.ROTTEN_FLESH).setName((enabled ? ChatColor.GREEN + "Beta Zombies" : ChatColor.RED + "Beta Zombies")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Beta Zombies";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Zombies drop feathers");
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
        return new ItemCreator(Material.ROTTEN_FLESH).setName((enabled ? ChatColor.GREEN + "Beta Zombies" : ChatColor.RED + "Beta Zombies")).setLore(getScenarioExplanation()).get();
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