package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnchantedDeath extends Scenario {

    private boolean enabled = false;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if (event.getEntity().getType() == EntityType.PLAYER && event.getEntity().getKiller() != null) {
            event.getDrops().add(new ItemStack(Material.ENCHANTMENT_TABLE));
        }
    }

    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {

        ItemStack item = event.getRecipe().getResult();

        if (item.getType() == Material.ENCHANTMENT_TABLE) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Enchanted death";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.ENCHANTMENT_TABLE).setName((enabled ? ChatColor.GREEN + "Enchanted death" : ChatColor.RED + "Enchanted death")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Enchanted death";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - You can only get enchanted tables from killing players");
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
        return new ItemCreator(Material.ENCHANTMENT_TABLE).setName((enabled ? ChatColor.GREEN + "Enchanted death" : ChatColor.RED + "Enchanted death")).setLore(getScenarioExplanation()).get();

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