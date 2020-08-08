package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.events.GameStartEvent;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lootcrates extends Scenario {

    private boolean enabled = false;
    private Terra thorn = Terra.getInstance();
    private final ArrayList<ItemStack> t1 = thorn.getGameManager().getT1();
    private final ArrayList<ItemStack> t2 = thorn.getGameManager().getT2();


    @EventHandler
    public void onGame(GameStartEvent event) {
        Bukkit.getScheduler().runTaskTimer(main, () -> {
            thorn.getGameManager().getPlayers().stream().filter(UHCPlayer::isOnline).forEach(uhcPlayer -> {
                Utils.giveItem(uhcPlayer.getPlayer(), giveLootCrate());
            });
        }, 0L, (600L * 20L));
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        try {
            Player player = (Player) event.getPlayer();
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (event.getItem().getType().equals(Material.ENDER_CHEST) || event.getItem().getType().equals(Material.CHEST)) {
                    if (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Tier 1 Lootcrate")) {
                        player.getInventory().setItemInHand(t1.get(thorn.getGameManager().getRandom().nextInt(t1.size())));
                        player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 10, 1);
                        event.setCancelled(true);
                    } else if (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Tier 2 Lootcrate")) {
                        player.getInventory().setItemInHand(t2.get(thorn.getGameManager().getRandom().nextInt(t2.size())));
                        player.playSound(player.getLocation(), Sound.FIREWORK_BLAST, 10, 1);
                        event.setCancelled(true);
                    } else {

                    }
                }
            }
        } catch (NullPointerException e) {

        }
    }

    private ItemStack giveLootCrate() {
        if (thorn.getGameManager().getRandom().nextBoolean()) {
            ItemStack itemStack = new ItemStack(Material.CHEST);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN + "Tier 1 Lootcrate");
            itemMeta.setLore(Arrays.asList("Right click to open this lootcrate!"));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } else {
            ItemStack itemStack = new ItemStack(Material.ENDER_CHEST);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN + "Tier 2 Lootcrate");
            itemMeta.setLore(Arrays.asList("Right click to open this lootcrate!"));
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }


    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Lootcrates";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.CHEST).setName((enabled ? ChatColor.GREEN + "Lootcrates" : ChatColor.RED + "Lootcrates")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Lootcrates";
    }

    @Override
    public java.util.List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Every 10 minutes you will be given a crate");
        explain.add(ChatColor.BLUE + " - clicking it will have a random item");
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
        return new ItemCreator(Material.CHEST).setName((enabled ? ChatColor.GREEN + "Lootcrates" : ChatColor.RED + "Lootcrates")).setLore(getScenarioExplanation()).get();
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