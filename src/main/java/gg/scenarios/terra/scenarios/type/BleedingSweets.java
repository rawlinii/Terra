package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BleedingSweets extends Scenario {

    private boolean enabled = false;

    @EventHandler
    public void BleedSweets(EntityDeathEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Location location = player.getLocation();
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        ItemStack gold = new ItemStack(Material.GOLD_INGOT, 5);
        ItemStack arrow = new ItemStack(Material.ARROW, 16);
        ItemStack string = new ItemStack(Material.STRING, 1);
        ItemStack book = new ItemStack(Material.BOOK, 1);
        event.getDrops().add(diamond);
        event.getDrops().add(gold);
        event.getDrops().add(arrow);
        event.getDrops().add(string);
        event.getDrops().add(book);

    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Bleeding Sweets";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.SUGAR).setName((enabled ? ChatColor.GREEN + "Bleeding Sweets" : ChatColor.RED + "Bleeding Sweets")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Bleeding Sweets";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - When a player dies");
        explain.add(ChatColor.BLUE + " - They drop 1 diamond, 5 gold, 16 arrows, 1 book and 1 string.");
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
        return new ItemCreator(Material.SUGAR).setName((enabled ? ChatColor.GREEN + "Bleeding Sweets" : ChatColor.RED + "Bleeding Sweets")).setLore(getScenarioExplanation()).get();
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
