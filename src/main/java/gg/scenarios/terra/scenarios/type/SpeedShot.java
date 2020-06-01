package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedShot extends Scenario {


    private boolean enabled = false;

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Arrow damager = (Arrow) event.getDamager();

        if (!(damager.getShooter() instanceof Player)) {
            return;
        }
        if (damager.getShooter().equals(player)){
            return;
        }


        Player killer = (Player) damager.getShooter();

        double distance = killer.getLocation().distance(player.getLocation());

        if (distance >= 50){
            killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
        }
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "SpeedShot";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.SUGAR).setName((enabled ? ChatColor.GREEN + "SpeedShot" : ChatColor.RED + "SpeedShot")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "SpeedShot";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Whenever you shoot someone over 50 blocks you will gain speed 2 for the next 10 seconds");
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
        return new ItemCreator(Material.DIAMOND_BOOTS).setName((enabled ? ChatColor.GREEN + "SpeedShot" : ChatColor.RED + "SpeedShot")).setLore(getScenarioExplanation()).get();
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
