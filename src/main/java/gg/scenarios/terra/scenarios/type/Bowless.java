package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bowless extends Scenario {

    private boolean enabled = false;

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        e.getDrops().removeIf(stack -> stack.getType() == Material.BOW);
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (e.getRecipe().getResult().getType() == Material.BOW) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for (LivingEntity livingEntity : e.getViewers()) {
                if (livingEntity instanceof Player) {
                    Player player = (Player) livingEntity;
                    player.sendMessage(ChatColor.RED + "Bows cannot be crafted in Bowless games.");
                }
            }
        }
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;

    }

    @Override
    public String getDefaultName() {
        return "Bowless";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.BOW).setName((enabled ? ChatColor.GREEN + "Bowless" : ChatColor.RED + "Bowless")).setLore(Arrays.asList(getState())).get();

    }

    @Override
    public String getName() {
        return "Bowless";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Bows cannot be crafted");
        explain.add(ChatColor.BLUE + " - Bows cannot be dropped by a mob");
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
        return new ItemCreator(Material.BOW).setName((enabled ? ChatColor.GREEN + "Bowless" : ChatColor.RED + "Bowless")).setLore(getScenarioExplanation()).get();
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