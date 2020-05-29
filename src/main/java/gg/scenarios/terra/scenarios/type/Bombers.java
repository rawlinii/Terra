package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.events.GameStartEvent;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.scenarios.Scenario;

import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bombers extends Scenario {


    private boolean enabled = false;
    private Terra thorn = Terra.getInstance();

    ItemStack fns = new ItemStack(Material.FLINT_AND_STEEL);

    @EventHandler
    public void onGame(GameStartEvent event) {
        fns.addEnchantment(Enchantment.DURABILITY, 3);
        thorn.getGameManager().getPlayers().stream().filter(UHCPlayer::isOnline).forEach(uhcPlayer -> {
            uhcPlayer.getPlayer().getInventory().addItem(fns);
        });
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        e.getDrops().add(new ItemStack(Material.TNT, 1));
    }


    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Bombers";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.TNT).setName((enabled ? ChatColor.GREEN + "Bombers" : ChatColor.RED + "Bombers")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Bombers";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Everyone starts with a unbreakable flint and steel");
        explain.add(ChatColor.BLUE + " - Mobs drop 1 tnt on death");
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
        return new ItemCreator(Material.TNT).setName((enabled ? ChatColor.GREEN + "Bombers" : ChatColor.RED + "Bombers")).setLore(getScenarioExplanation()).get();
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