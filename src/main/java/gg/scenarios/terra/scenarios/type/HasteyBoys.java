package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HasteyBoys extends Scenario {

    @EventHandler
    public void on(CraftItemEvent e) {
        ItemStack item = e.getRecipe().getResult();
        CraftingInventory inv = e.getInventory();
        String name = item.getType().name();
        if ((name.contains("SPADE")) || (name.contains("AXE")) || (name.contains("PICKAXE"))) {
            addHBEnchantments(item);
            inv.setResult(item);
        }

    }

    public void addHBEnchantments(ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        meta.addEnchant(Enchantment.DURABILITY, 3, true);
        item.setItemMeta(meta);
    }
    private boolean enabled = false;

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Hastey Boys";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.DIAMOND_PICKAXE).setName((enabled ? ChatColor.GREEN + "HasteyBoys" : ChatColor.RED + "HasteyBoys")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Hastey Boys";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - All tools are enchanted with Eff3");
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
        return new ItemCreator(Material.DIAMOND_PICKAXE).setName((enabled ? ChatColor.GREEN + "HasteyBoys" : ChatColor.RED + "HasteyBoys")).setLore(getScenarioExplanation()).get();

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