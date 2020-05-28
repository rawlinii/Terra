package gg.scenarios.terra.listeners;

import gg.scenarios.terra.Terra;

import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import sun.security.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class LobbyListener implements Listener {

    private Terra thorn = Terra.getInstance();

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (thorn.getGameManager().getGameState() == GameState.LOBBY || thorn.getGameManager().getGameState() == GameState.BIDDING || event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            Player player = (Player) event.getPlayer();
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType().equals(EntityType.PLAYER)) {
            Player player = (Player) event.getEntity();

            if (thorn.getGameManager().getGameState() == GameState.LOBBY || thorn.getGameManager().getGameState() == GameState.BIDDING || event.getEntity().getWorld().getName().equalsIgnoreCase("world")) {
                event.setCancelled(true);
            } else if (thorn.getGameManager().getMods().contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }

    }


    @EventHandler
    public void health(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            if (thorn.getGameManager().getGameState() == GameState.LOBBY || thorn.getGameManager().getGameState() == GameState.BIDDING || event.getEntity().getWorld().getName().equalsIgnoreCase("world")) {
                Player player = (Player) event.getEntity();
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void health(PlayerDropItemEvent event) {
        if (thorn.getGameManager().getGameState() == GameState.LOBBY || thorn.getGameManager().getGameState() == GameState.BIDDING || event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        } else if (Terra.getInstance().getGameManager().getMods().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;

        if (event.getInventory().getTitle().equals("Player Selector") && thorn.getGameManager().getMods().contains(player.getUniqueId())) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
                return;

            Player target = Bukkit.getServer().getPlayer(event.getCurrentItem().getItemMeta().getDisplayName().substring(2, event.getCurrentItem().getItemMeta().getDisplayName().length()));
            if (target != null) {
                player.teleport(target);
            }
            event.setCancelled(true);
        }

        if (thorn.getGameManager().getMods().contains(player.getUniqueId())) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
                return;

            if (player.getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)
            return;

        Player player = event.getPlayer();

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem().getType().equals(Material.COMPASS) &&  thorn.getGameManager().getMods().contains(player.getUniqueId())) {
                openPlayerInv(player);
                event.setCancelled(true);
            }
            if (event.getItem().getType().equals(Material.INK_SACK) &&  thorn.getGameManager().getMods().contains(player.getUniqueId())) {
                if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                } else {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 10000000, 0));
                }
                event.setCancelled(true);
            }
            if (event.getItem().getType().equals(Material.FEATHER) &&  thorn.getGameManager().getMods().contains(player.getUniqueId())) {
                player.teleport(new Location(player.getWorld(), 0, 100, 0));
                event.setCancelled(true);
            }
        } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getItem().getType().equals(Material.COMPASS) &&  thorn.getGameManager().getMods().contains(player.getUniqueId())) {
                ArrayList<Player> players = new ArrayList<Player>();
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (!thorn.getGameManager().getMods().contains(online.getUniqueId())) {
                        players.add(online);
                    }
                }
                Player target = players.get(new Random().nextInt(players.size()));
                player.teleport(target.getLocation());
                player.getWorld().getChunkAt(target.getLocation()).load();

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', thorn.getReference().getStaff()) + "You teleported to §6" + target.getName() + "§7.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof Player || event.getEntity() != null) {
                Player damager = (Player) event.getDamager();
                if ( thorn.getGameManager().getMods().contains(damager.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private void openPlayerInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Player Selector");

        for (UHCPlayer online : thorn.getGameManager().getPlayers()) {
            if (online.isOnline()) {
                if (! thorn.getGameManager().getMods().contains(online.getPlayer().getUniqueId())) {
                    try {
                        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                        SkullMeta meta = (SkullMeta) item.getItemMeta();
                        meta.setDisplayName(ChatColor.GREEN + online.getPlayer().getName());
                        meta.setOwner(online.getPlayer().getName());
                        meta.setLore(Collections.singletonList(ChatColor.GRAY + "Click to teleport to " + online.getPlayer().getName() + "."));
                        item.setItemMeta(meta);
                        inv.addItem(item);
                    } catch (NullPointerException e) {

                    }
                }
            }
        }
        player.openInventory(inv);
    }


}