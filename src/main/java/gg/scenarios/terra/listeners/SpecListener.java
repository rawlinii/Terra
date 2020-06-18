package gg.scenarios.terra.listeners;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.block.Chest;
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SpecListener implements Listener {


    private Terra main;

    public SpecListener(Terra main) {
        this.main = main;
    }


    private HashMap<String, Integer> totalD = new HashMap<String, Integer>();
    private HashMap<String, Integer> totalG = new HashMap<String, Integer>();
    private ArrayList<Location> locs = new ArrayList<>();


    @EventHandler
    public void onDiamond(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.DIAMOND_ORE) return;
        if (locs.contains(event.getBlock().getLocation())) return;

        Player player = event.getPlayer();
        int amount = 0;
        Location loc = event.getBlock().getLocation();

        for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
            for (int y = loc.getBlockY() - 1; y <= loc.getBlockY() + 1; y++) {
                for (int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++) {
                    if (loc.getWorld().getBlockAt(x, y, z).getType() == Material.DIAMOND_ORE) {
                        amount++;
                        locs.add(loc.getWorld().getBlockAt(x, y, z).getLocation());
                    }
                }
            }
        }

        if (totalD.containsKey(player.getName())) {
            totalD.put(player.getName(), totalD.get(player.getName()) + amount);
        } else {
            totalD.put(player.getName(), amount);
        }

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                if (UHCPlayer.getByName(online.getName()).isXrayAlerts()) {
                    online.sendMessage("[§9S§f] §7" + player.getPlayerListName() + " §7» §bDiamond §f[V:§b" + amount + "§f] [T:§b" + totalD.get(player.getName()) + "§f]");
                }
            }
        }
    }

    @EventHandler
    public void onMineGold(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.GOLD_ORE) return;
        if (locs.contains(event.getBlock().getLocation())) return;

        Player player = event.getPlayer();
        int amount = 0;
        Location loc = event.getBlock().getLocation();

        for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
            for (int y = loc.getBlockY() - 1; y <= loc.getBlockY() + 1; y++) {
                for (int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++) {
                    if (loc.getWorld().getBlockAt(x, y, z).getType() == Material.GOLD_ORE) {
                        amount++;
                        locs.add(loc.getWorld().getBlockAt(x, y, z).getLocation());
                    }
                }
            }
        }

        if (totalG.containsKey(player.getName())) {
            totalG.put(player.getName(), totalG.get(player.getName()) + amount);
        } else {
            totalG.put(player.getName(), amount);
        }

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                if (UHCPlayer.getByName(online.getName()).isXrayAlerts()) {
                    online.sendMessage("[§9S§f] §7" + player.getPlayerListName() + " §7» §6Gold §f[V:§6" + amount + "§f] [T:§6" + totalG.get(player.getName()) + "§f]");
                }
            }
        }
    }

    @EventHandler
    public void onPearl(PlayerTeleportEvent event) {
        if (main.getGameManager().getGameState().equals(GameState.STARTED)) {
            if (event.getCause() != TeleportCause.ENDER_PEARL) {
                return;
            }

            Player player = event.getPlayer();

            if (main.getGameManager().getMods().contains(player.getUniqueId())) return;

            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                    if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                        online.sendMessage("[§9S§f] §5Pearl:§6" + player.getName() + "§f->§d" + convertHealth(event.getFrom().distance(event.getTo())) + " blocks.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSpectatorInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (main.getGameManager().getMods().contains(player.getUniqueId())) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.FURNACE || event.getClickedBlock().getType() == Material.BREWING_STAND || event.getClickedBlock().getType() == Material.BURNING_FURNACE)
                    return;

                if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
                    Chest chest = (Chest) event.getClickedBlock().getState();
                    Inventory inv = Bukkit.createInventory(null, chest.getInventory().getSize(), "Chest");
                    inv.setContents(chest.getInventory().getContents());
                    player.openInventory(inv);
                }
                event.setCancelled(true);
            }
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
        }
    }


    @EventHandler
    public void onPortal(PlayerChangedWorldEvent event) {
        if (main.getGameManager().getGameState().equals(GameState.STARTED)) {

            Player player = event.getPlayer();

            if (main.getGameManager().getMods().contains(player.getUniqueId())) {
                return;
            }

            if (!event.getFrom().getName().equals("world") && !player.getWorld().getName().equals("world")) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §dPortal:§6" + player.getName() + "§f from §a" + event.getFrom().getName() + "§f to §c" + player.getWorld().getName());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHeal(PlayerItemConsumeEvent event) {
        if (main.getGameManager().getGameState().equals(GameState.STARTED)) {

            if (event.getItem().getType() != Material.GOLDEN_APPLE) return;

            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            if ((main.getGameManager().getMods().contains(player.getUniqueId()))) return;

            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                    if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                        online.sendMessage("[§9S§f] §aHeal: §6" + player.getName() + "§f<->§6" + (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head") ? "§5Golden Head" : "Golden Apple"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (main.getGameManager().getGameState().equals(GameState.STARTED)) {

            if (!(event.getWhoClicked() instanceof Player)) return;

            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getRecipe().getResult();

            if ((main.getGameManager().getMods().contains(player.getUniqueId()))) return;

            if (item.getType() == Material.GOLDEN_APPLE) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§6" + (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Golden Head") ? "§5Golden Head" : "Golden Apple"));
                        }
                    }
                }
            }

            if (item.getType() == Material.DIAMOND_HELMET) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§bDia. Helmet");
                        }
                    }
                }
            }

            if (item.getType() == Material.DIAMOND_CHESTPLATE) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§bDia. Chest");
                        }
                    }
                }
            }

            if (item.getType() == Material.DIAMOND_LEGGINGS) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§bDia. Leggings");
                        }
                    }
                }
            }

            if (item.getType() == Material.DIAMOND_BOOTS) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§bDia. Boots");
                        }
                    }
                }
            }

            if (item.getType() == Material.BOW) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§bBow");
                        }
                    }
                }
            }

            if (item.getType() == Material.DIAMOND_SWORD) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§bDia. Sword");
                        }
                    }
                }
            }

            if (item.getType() == Material.ANVIL) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§dAnvil");
                        }
                    }
                }
            }

            if (item.getType() == Material.ENCHANTMENT_TABLE) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§dEnchant. Table");
                        }
                    }
                }
            }

            if (item.getType() == Material.BREWING_STAND_ITEM) {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                        if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                            online.sendMessage("[§9S§f] §2Craft§f: §a" + player.getName() + "§f<->§dBrewing Stand");
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(final EntityDamageEvent event) {
        if (main.getGameManager().getGameState().equals(GameState.STARTED)) {

            if (!(event.getEntity() instanceof Player)) return;

            final Player player = (Player) event.getEntity();

            if ((main.getGameManager().getMods().contains(player.getUniqueId()))) return;

            if (event instanceof EntityDamageByEntityEvent) {
                onDamageByOther(player, (EntityDamageByEntityEvent) event);
                return;
            }

            final DamageCause cause = event.getCause();
            final Damageable dmg = player;
            final double olddamage = dmg.getHealth();

            Bukkit.getServer().getScheduler().runTaskLater(main, new Runnable() {
                public void run() {
                    double damage = olddamage - dmg.getHealth();

                    if (cause == DamageCause.LAVA) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dLava §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dFire §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.CONTACT) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {

                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dCactus §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.DROWNING) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dDrowning §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.FALL) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dFall §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.LIGHTNING) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dLightning §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.MAGIC) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dMagic §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.POISON) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dPoison §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.STARVATION) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dStarving §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.SUFFOCATION) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dSuffocation §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.VOID) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dVoid §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.WITHER) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dWither §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (cause == DamageCause.BLOCK_EXPLOSION) {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§dTNT §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else {
                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPveAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§d??? §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    }
                }
            }, 1);
        }
    }

    private void onDamageByOther(final Player player, final EntityDamageByEntityEvent event) {
        if (main.getGameManager().getGameState().equals(GameState.STARTED)) {

            final Damageable dmg = player;

            if ((main.getGameManager().getMods().contains(player.getUniqueId()))) return;

            final double olddamage = dmg.getHealth();

            Bukkit.getServer().getScheduler().runTaskLater(main, new Runnable() {
                public void run() {
                    double damage = olddamage - dmg.getHealth();

                    if (event.getDamager() instanceof Player) {
                        Player killer = (Player) event.getDamager();
                        if (main.getGameManager().getMods().contains(killer.getUniqueId())) return;
                        Damageable dmgr = killer;

                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPvpAlerts()) {
                                    online.sendMessage("[§9S§f] §4PvP§f:§a" + killer.getName() + "§f-M>§c" + player.getName() + " §f[§a" + convertHealth((dmgr.getHealth() / 2)) + "§f:§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    } else if (event.getDamager() instanceof Projectile) {
                        Projectile p = (Projectile) event.getDamager();

                        if (p.getShooter() instanceof Player) {
                            Player shooter = (Player) p.getShooter();
                            Damageable dmgr = shooter;

                            if (p instanceof Arrow) {
                                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                        if (UHCPlayer.getByName(online.getName()).isPvpAlerts()) {
                                            online.sendMessage("[§9S§f] §4PvP§f:§a" + shooter.getName() + "§f-B>§c" + player.getName() + " §f[§a" + convertHealth((dmgr.getHealth() / 2)) + "§f:§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                        }
                                    }
                                }
                            } else if (p instanceof Snowball) {
                                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                        if (UHCPlayer.getByName(online.getName()).isPvpAlerts()) {

                                            online.sendMessage("[§9S§f] §4PvP§f:§a" + shooter.getName() + "§f-S>§c" + player.getName() + " §f[§a" + convertHealth((dmgr.getHealth() / 2)) + "§f:§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                        }
                                    }
                                }
                            } else if (p instanceof Egg) {
                                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                        if (UHCPlayer.getByName(online.getName()).isPvpAlerts()) {

                                            online.sendMessage("[§9S§f] §4PvP§f:§a" + shooter.getName() + "§f-E>§c" + player.getName() + " §f[§a" + convertHealth((dmgr.getHealth() / 2)) + "§f:§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                        }
                                    }
                                }
                            } else if (p instanceof FishHook) {
                                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                        if (UHCPlayer.getByName(online.getName()).isPvpAlerts()) {

                                            online.sendMessage("[§9S§f] §4PvP§f:§a" + shooter.getName() + "§f-F>§c" + player.getName() + " §f[§a" + convertHealth((dmgr.getHealth() / 2)) + "§f:§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                        }
                                    }
                                }
                            } else {
                                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                    if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                        if (UHCPlayer.getByName(online.getName()).isPvpAlerts()) {

                                            online.sendMessage("[§9S§f] §4PvP§f:§a" + shooter.getName() + "§f-?P>§c" + player.getName() + " §f[§a" + convertHealth((dmgr.getHealth() / 2)) + "§f:§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!(p.getShooter() instanceof Entity)) {
                                return;
                            }

                            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                                if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                    if (UHCPlayer.getByName(online.getName()).isPvpAlerts()) {

                                        online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§d" + ((Entity) p.getShooter()).getType().name().substring(0, 1).toUpperCase() + ((Entity) p.getShooter()).getType().name().substring(1).toLowerCase().replaceAll("_", "") + " §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                    }
                                }
                            }
                        }
                    } else if (event.getDamager() instanceof LivingEntity) {
                        Entity e = event.getDamager();

                        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                            if (main.getGameManager().getMods().contains(online.getUniqueId())) {
                                if (UHCPlayer.getByName(online.getName()).isPvpAlerts()) {
                                    online.sendMessage("[§9S§f] §5PvE§f:§c" + player.getName() + "§f<-§d" + e.getType().name().substring(0, 1).toUpperCase() + e.getType().name().substring(1).toLowerCase().replaceAll("_", "") + " §f[§c" + convertHealth((dmg.getHealth() / 2)) + "§f] [§6" + convertHealth((damage / 2)) + "§f]");
                                }
                            }
                        }
                    }
                }
            }, 1);
        }
    }


    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (!(entity instanceof Player)) return; // not a player
        Player other = (Player) entity;
        if (main.getGameManager().getMods().contains(player.getUniqueId())) {
            if (player.getItemInHand().getType() == Material.BOOK) {
                openInv(player, other);
            }
        }

    }

    public void openInv(Player player, final Player target) {
        final Inventory inv = Bukkit.getServer().createInventory(target, 45, "Player Inventory");
        final Damageable dmg = target;

        ArrayList<String> lore2 = new ArrayList<String>();
        lore2.add("§cThis is a placeholder, the player is naked");
        ItemStack armorPlaceHolder = new ItemCreator(Material.STAINED_GLASS_PANE).setName(ChatColor.RED + "Armor Place Holder").get();

        inv.setItem(0, target.getInventory().getHelmet());
        inv.setItem(1, target.getInventory().getChestplate());
        inv.setItem(2, target.getInventory().getLeggings());
        inv.setItem(3, target.getInventory().getBoots());

        ItemStack n = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = n.getItemMeta();
        infoMeta.setDisplayName("§cPlayer information");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§aName: §7" + target.getName());
        lore.add(" ");
        lore.add("§aHearts: §7" + (((int) dmg.getHealth()) / 2));
        lore.add("§aHunger: §7" + (target.getFoodLevel() / 2));
        lore.add("§aXp level: §7" + target.getLevel());
        lore.add("§aLocation: §7" + target.getWorld().getEnvironment().name().replaceAll("_", "").toLowerCase().replaceAll("normal", "overworld") + ", x:" + target.getLocation().getBlockX() + ", y:" + target.getLocation().getBlockY() + ", z:" + target.getLocation().getBlockZ());
        lore.add(" ");

        ItemStack info = new ItemCreator(Material.SKULL_ITEM).setOwner(target.getName()).setName(ChatColor.GREEN + "Player Information").setLore(lore).get();
        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);
        inv.setItem(8, info);

        int i = 9;
        for (ItemStack item : target.getInventory().getContents()) {
            inv.setItem(i, item);
            i++;
        }
        player.openInventory(inv);

    }


    public static String convertHealth(double damage) {
        NumberFormat nf = new DecimalFormat("##.##");
        return nf.format(damage);
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (main.getGameManager().getMods().contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPick(PlayerPickupItemEvent event) {
        if (main.getGameManager().getMods().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
