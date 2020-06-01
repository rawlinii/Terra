package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WebCage extends Scenario {



    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (enabled) {
            if (event.getEntity().getKiller() == null) return;
            for (Location loc : Utils.generateSphere(event.getEntity().getKiller().getLocation(), 4, true)){
                loc.getBlock().setType(Material.WEB);
            }
        }
    }



    private boolean enabled = false;

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "WebCage";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.WEB).setName((enabled ? ChatColor.GREEN + "WebCage" : ChatColor.RED + "WebCage")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "WebCage";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Whenever you get a kill, a sphere of webs will generate around you");
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
        return new ItemCreator(Material.WEB).setName((enabled ? ChatColor.GREEN + "WebCage" : ChatColor.RED + "WebCage")).setLore(getScenarioExplanation()).get();
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
