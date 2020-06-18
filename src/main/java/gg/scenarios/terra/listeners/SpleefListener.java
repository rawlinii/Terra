package gg.scenarios.terra.listeners;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.events.GameStateChangeEvent;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SpleefListener implements Listener {
    private final Terra terra;
    private final ItemStack spleefShovel = new ItemCreator(Material.DIAMOND_SPADE).addDigSpeed().get();
    private final Location spleefSpawn = new Location(Bukkit.getWorld("world"), -11, 127, -2);

    public SpleefListener(Terra terra) {
        this.terra = terra;
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event){
        if (event.getGameState() != GameState.LOBBY){
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onMove(PlayerMoveEvent event){
        if (terra.getGameManager().getGameState() != GameState.LOBBY) return;
        Player player = event.getPlayer();
        if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SNOW_BLOCK) {
            if (player.getInventory().contains(Material.DIAMOND_SPADE)){
                return;
            }else {
                player.getInventory().addItem(spleefShovel);
            }
        }
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event){
        if (terra.getGameManager().getGameState() != GameState.LOBBY) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (event.getCause().equals(EntityDamageEvent.DamageCause.LAVA) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)){
            player.getInventory().clear();
            player.teleport(spleefSpawn);
            player.setFireTicks(0);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e){
        if (terra.getGameManager().getGameState() != GameState.LOBBY) return;
        Player player = (Player) e.getPlayer();
        Location location = e.getBlock().getLocation();
        if (e.getBlock().getType() == Material.SNOW_BLOCK) {
            e.setCancelled(false);
            Bukkit.getServer().getScheduler().runTaskLater(terra, () -> {
                location.getBlock().setType(Material.SNOW_BLOCK);
            }, 20 * 10);
        }else{
            e.setCancelled(true);
        }
    }
}
