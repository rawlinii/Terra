package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.events.GameStartEvent;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.scenarios.Scenario;

import gg.scenarios.terra.tasks.SkyHighTask;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkyHigh extends Scenario {

    private ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE);
    @EventHandler
    public void onGameStart(GameStartEvent event){
        event.getPlayers().stream()
                .filter(UHCPlayer::isOnline)
                .filter(UHCPlayer::isPlaying)
                .forEach(uhcPlayer -> {
                    uhcPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.STAINED_CLAY, 126));
                    uhcPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BLOCK, 2));
                    uhcPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.PUMPKIN, 1));
                    uhcPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.STRING, 2));
                    uhcPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.FEATHER, 16));
                    uhcPlayer.getPlayer().getInventory().addItem(shovel);
                });
        Bukkit.getScheduler().runTaskLater(main, SkyHighTask::new, 20*60*45);
    }


    private boolean enabled = false;

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "SkyHigh";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.SNOW_BLOCK).setName((enabled ? ChatColor.GREEN + "SkyHigh" : ChatColor.RED + "SkyHigh")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "SkyHigh";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - After 45 minutes every player under y100 will take ");
        explain.add(ChatColor.BLUE + " - 1/2 heart damage every 30 seconds");


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
        return new ItemCreator(Material.SNOW_BLOCK).setName((enabled ? ChatColor.GREEN + "SkyHigh" : ChatColor.RED + "SkyHigh")).setLore(getScenarioExplanation()).get();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        shovel.addEnchantment(Enchantment.DIG_SPEED, 4);
        shovel.addEnchantment(Enchantment.DURABILITY, 3);
        this.enabled = enabled;
    }

}