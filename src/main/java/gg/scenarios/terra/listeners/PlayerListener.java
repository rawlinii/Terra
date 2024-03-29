package gg.scenarios.terra.listeners;

import com.google.gson.internal.$Gson$Preconditions;
import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.managers.profiles.Logger;
import gg.scenarios.terra.managers.profiles.PlayerState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.Random;

public class PlayerListener implements Listener {


    private Terra terra;
    private GameManager gameManager;
    private Reference reference;

    public PlayerListener(Terra terra) {
        this.terra = terra;
        gameManager = terra.getGameManager();
        reference = terra.getReference();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (gameManager.isStartingFall()) {
            if (event.getEntityType().equals(EntityType.PLAYER)) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onLogger(EntityDamageEvent event) {
        if (event.getEntity() instanceof Zombie) {
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                if (event.getEntity().getCustomName() != null) {
                    if (!gameManager.isPvp()) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Zombie)) return;

        event.getDrops().clear();

        Zombie zombie = (Zombie) event.getEntity();
        UHCPlayer uhcPlayer = UHCPlayer.getByName(zombie.getCustomName());

        if (uhcPlayer == null || uhcPlayer.getLogger() == null) return;

        final Logger logger = uhcPlayer.getLogger();

        event.setDroppedExp(logger.getExperience());

        if (event.getEntity().getKiller() != null) {
            final Player killer = event.getEntity().getKiller();
            logger.handleDeath(killer);
        } else {
            logger.handleDeath(null);
        }
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();
        try {
            if (!gameManager.isNotch()) {
                if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 1) {
                    event.setCancelled(true);
                }
            } else if (event.getItem().getType() == Material.GOLDEN_APPLE && event.getItem().getDurability() == 0) {
                ItemStack goldenHead = event.getItem();
                if (goldenHead.getItemMeta().getDisplayName().contains("Golden Head")) {

                    if (gameManager.isAbsorption()) {
                        PotionEffect pe = new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0);
                        pe.apply(p);
                    }
                    PotionEffect re = new PotionEffect(PotionEffectType.REGENERATION, 200, 1);

                    re.apply(p);
                }
                if (!gameManager.isAbsorption()){
                    p.removePotionEffect(PotionEffectType.ABSORPTION);
                }
            }
        } catch (Exception ignored) {

        }
    }

    @EventHandler
    public void shears(BlockBreakEvent e) {
        if (e.getBlock().getType().equals(Material.LEAVES) || e.getBlock().getType().equals(Material.LEAVES_2)) {
            if (gameManager.isShears()) {
                if (e.getPlayer().getItemInHand().getType().equals(Material.SHEARS)) {
                    if (getRandomValue(gameManager.getRandom(), 0, 100, 1) <= gameManager.getAppleRates()) {
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
                    }
                }
            }
        }
    }

    @EventHandler
    public void nether(PortalCreateEvent event) {
        if (!gameManager.isNether()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void preDamage(EntityDamageEvent event) {
        if (gameManager.getGameState().equals(GameState.SCATTERING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDecay(LeavesDecayEvent e) {
        if (Bukkit.getOnlinePlayers().size() > 80) e.setCancelled(true);

        if (!e.isCancelled()) {
            if (getRandomValue(gameManager.getRandom(), 0, 100, 1) <= gameManager.getAppleRates()) {
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }

    }


    @EventHandler
    public void onHorseless(EntityMountEvent event) {
        event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        gameManager.getPlayers().remove(UHCPlayer.getByUUID(event.getEntity().getUniqueId()));
    }

    @EventHandler
    public void onBow(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Arrow damager = (Arrow) event.getDamager();

        if (!(damager.getShooter() instanceof Player)) {
            return;
        }
        if (damager.getShooter().equals(player)) {
            return;
        }
        Player killer = (Player) damager.getShooter();

        double distance = killer.getLocation().distance(player.getLocation());
        killer.sendMessage(ChatColor.translateAlternateColorCodes('&', terra.getReference().getPvp() + "&7" + player.getName() + " &7is at " + formatDamage(player.getHealth())));
    }


    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            terra.getGameboard().setScore("§a§lPvE", terra.getGameboard().getScore("§a§lPvE") + 1);
            return;
        }
        Player killer = event.getEntity().getKiller();

        terra.getGameboard().setScore(killer.getName(), terra.getGameboard().getScore(killer.getName()) + 1);
    }

    private double getRandomValue(final Random random,
                                  final int lowerBound,
                                  final int upperBound,
                                  final int decimalPlaces) {

        if (lowerBound < 0 || upperBound <= lowerBound || decimalPlaces < 0) {
            throw new IllegalArgumentException("Put error message here");
        }

        final double dbl =
                ((random == null ? gameManager.getRandom() : random).nextDouble() //
                        * (upperBound - lowerBound))
                        + lowerBound;
        return Double.parseDouble((String.format("%." + decimalPlaces + "f", dbl)).replace(",", "."));
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if (event.getEntity().getType() == EntityType.COW || event.getEntity().getType() == EntityType.PIG) return;
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            if (gameManager.getRandom().nextBoolean()) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        UHCPlayer uhcPlayer = UHCPlayer.getByName(event.getPlayer().getName());
        Player player = uhcPlayer.getPlayer();
        if (gameManager.getGameState().equals(GameState.STARTED)) {
            if (uhcPlayer.isAlive()) {
                if (gameManager.getMods().contains(player.getUniqueId())) {
                } else {
                    Logger logger = new Logger(uhcPlayer);
                    uhcPlayer.setLogger(logger);
                }
            }
        }
    }


    @EventHandler
    public void on(PlayerLoginEvent ev) {
        Player e = ev.getPlayer();
        if (terra.getGameManager().getGameState() == GameState.SCATTERING) {
            ev.disallow(PlayerLoginEvent.Result.KICK_FULL, "You cannot join during the scatter");
            return;
        }
        if (terra.getGameManager().isWhitelistEnabled()) {
            if (gameManager.getWhitelist().contains(e.getUniqueId()) || e.hasPermission("uhc.join") || e.isOp()) {
                ev.allow();
            } else {
                ev.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ChatColor.RED + "The whitelist is still enabled! \n Join our discord for coming matches \nhttps://discordapp.com/invite/qvbZFKa");
            }
        }

        if (UHCPlayer.getByName(e.getName()) == null) {
            if (!terra.getGameManager().getGameState().equals(GameState.STARTED)) {
                UHCPlayer profile = new UHCPlayer(e.getUniqueId(), e.getName());
                profile.setPlayerState(PlayerState.LOBBY);
            } else if (e.hasPermission("uhc.staff")) {
                UHCPlayer profile = new UHCPlayer(e.getUniqueId(), e.getName());
                profile.setPlayerState(PlayerState.SPECTATOR);
            } else if (terra.getGameManager().getGameState() == GameState.STARTED) {
                if (terra.getGameManager().getWhitelist().contains(e.getUniqueId())) {
                    if (UHCPlayer.getByName(e.getName()) == null) {
                        UHCPlayer profile = new UHCPlayer(e.getUniqueId(), e.getName());
                        profile.setPlayerState(PlayerState.INGAME);
                        profile.setScatter(gameManager.findLocation());
                        profile.getPlayer().teleport(profile.getScatter());
                    } else {
                        ev.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.RED + "The game has started \n join our discord for upcoming games!\nhttps://discord.gg/qvbZFKa");
                    }
                } else {
                    ev.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.RED + "The game has started \n join our discord for upcoming games!\nhttps://discord.gg/qvbZFKa");
                }
            } else {
                ev.disallow(PlayerLoginEvent.Result.KICK_FULL, ChatColor.RED + "The game has started \n join our discord for upcoming games!\nhttps://discord.gg/qvbZFKa");

            }

        } else {
            if (UHCPlayer.getByName(e.getName()).getPlayerState().equals(PlayerState.DEAD) && !e.hasPermission("uhc.staff")) {
                ev.disallow(PlayerLoginEvent.Result.KICK_WHITELIST, ChatColor.RED + "You have died in this UHC!");
            }
        }
    }


    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (gameManager.getGameState().equals(GameState.SCATTERING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
      /*  for (UUID uuid : uhc.getMods()) {
            Player mod = Bukkit.getPlayer(uuid);
            if (mod != null) {
                event.getPlayer().hidePlayer(mod);
            }
        }*/
        if (gameManager.getGameState() == GameState.LOBBY) {
            if (UHCPlayer.getByName(event.getPlayer().getName()) == null)
                new UHCPlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        }

        if (gameManager.getMods().contains(event.getPlayer().getUniqueId())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(event.getPlayer());
            }
        }
        event.setJoinMessage(null);
        //  Document found = uhc.getProfiles().find(new Document("uuid",event.getPlayer().getUniqueId().toString())).first();
        try {
            UHCPlayer uhcPlayer = UHCPlayer.getByName(event.getPlayer().getName());
            Player player = event.getPlayer();
            String matchPost;
            if (gameManager.getMatchPost() == null) {
                matchPost = "None.";
            } else {
                matchPost = gameManager.getMatchPost();
            }
            terra.getNms().sendTablist(player, ChatColor.RED + "" + ChatColor.BOLD + "Viszla.cc" + ChatColor.RESET + ChatColor.GRAY + " - " + ChatColor.RED + ChatColor.ITALIC + "@ViszlaUHC \n" +
                    ChatColor.GRAY + "Follow our UHC calender on twitter \n" +
                    ChatColor.GRAY + "Ping: " + ChatColor.RED + ((CraftPlayer) player).getHandle().ping + "ms \n", "\n" + ChatColor.RED + "" + ChatColor.BOLD + "Viszla.cc" + ChatColor.RESET + ChatColor.GRAY + " \n " + ChatColor.GRAY + "MatchPost: " + ChatColor.GOLD +
                    "No Match Scheduled");

            if (gameManager.getGameState().equals(GameState.STARTED)) {
                if (uhcPlayer.getPlayerState().equals(PlayerState.DEAD) || uhcPlayer.getPlayerState().equals(PlayerState.HOST) || uhcPlayer.getPlayerState().equals(PlayerState.MODERATOR) || uhcPlayer.getPlayerState().equals(PlayerState.SPECTATOR)) {
                    uhcPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                } else if (uhcPlayer.getPlayerState().equals(PlayerState.INGAME)) {
                    try {
                        Logger logger = UHCPlayer.getByName(event.getPlayer().getName()).getLogger();
                        Zombie zombie = logger.getZombie();

                        uhcPlayer.setLogger(null);

                        player.teleport(zombie);
                        player.setHealth(zombie.getHealth());
                        player.setTotalExperience(logger.getExperience());

                        zombie.getLocation().getChunk().load();

                        zombie.remove();
                        gameManager.getWhitelistedChunks().remove(zombie.getLocation().getChunk());
                        for (Entity a : player.getNearbyEntities(5, 5, 5)) {
                            if (a instanceof Zombie) {
                                if (((Zombie) a).getCustomName().equals(player.getName())) {
                                    a.remove();
                                }
                            }
                        }
                    } catch (NullPointerException e) {

                    }
                } else if (uhcPlayer.getPlayerState().equals(PlayerState.RESPAWNED)) {
                    Bukkit.getServer().getScheduler().runTaskLater(terra, () -> {
                        gameManager.getPlayers().add(uhcPlayer);
                        uhcPlayer.getPlayer().teleport(uhcPlayer.getDeathLocation());
                        uhcPlayer.getPlayer().getInventory().setArmorContents(uhcPlayer.getArmor());
                        uhcPlayer.getPlayer().getInventory().setContents(uhcPlayer.getInventory());
                        uhcPlayer.getPlayer().setLevel(uhcPlayer.getLevels());
                        uhcPlayer.getPlayer().sendMessage(ChatColor.GREEN + "You have been respawned");
                        uhcPlayer.setPlayerState(PlayerState.INGAME);
                        gameManager.getPlayers().add(uhcPlayer);
                    }, 30L);
                }
            } else if (gameManager.getGameState().equals(GameState.LOBBY)) {
                World world = Bukkit.getWorld("world");
                Location location = world.getSpawnLocation();
                if (player.hasPermission("uhc.host")) {
                    terra.getUtils().set(player, false);
                }

                if (!(gameManager.getPlayers().contains(uhcPlayer))) {
                    gameManager.getPlayers().add(uhcPlayer);
                }
                player.teleport(location);
                player.getInventory().clear();
                player.updateInventory();
                player.setLevel(0);
                player.setMaxHealth(20);
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                player.getInventory().setHelmet(null);
                player.getInventory().setChestplate(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setBoots(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @EventHandler
    public void noBurn(EntityDamageEvent event) {
        if (event.getEntity() instanceof Zombie) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                if (event.getEntity().getCustomName() != null) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!gameManager.getMods().contains(online.getUniqueId())) {
                player.showPlayer(online);
            } else {
                player.hidePlayer(online);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onKill(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (player.getKiller() != null) {
            Player p = (Player) event.getEntity();
            Player target = player.getKiller();
            UHCPlayer uhcPlayer = UHCPlayer.getByName(player.getName());
            uhcPlayer.setDeathLocation(player.getLocation());
            uhcPlayer.setInventory(player.getInventory().getContents());
            uhcPlayer.setArmor(player.getInventory().getArmorContents());
            uhcPlayer.setPlayerState(PlayerState.DEAD);
            uhcPlayer.setLevels(player.getLevel());
            uhcPlayer.getStatistics().getDeaths().increment();
            UHCPlayer targetp = UHCPlayer.getByName(target.getName());
            targetp.getStatistics().getKills().increment();
            gameManager.getPlayers().remove(uhcPlayer);
            if (!terra.getScenarioManager().getScenarioByName("Timebomb").isEnabled() || !terra.getScenarioManager().getScenarioByName("Golden Retriever").isEnabled()) {
                player.getWorld().getBlockAt(player.getLocation()).setType(Material.NETHER_FENCE);
                player.getWorld().getBlockAt(player.getLocation().add(0.0, 1.0, 0.0)).setType(Material.SKULL);
                final Block b = player.getWorld().getBlockAt(player.getLocation().add(0.0, 1.0, 0.0));
                b.setData((byte) 1);
                final BlockState state = player.getWorld().getBlockAt(player.getLocation().add(0.0, 1.0, 0.0)).getState();
                if (state instanceof Skull) {
                    final Skull skull = (Skull) state;
                    skull.setSkullType(SkullType.PLAYER);
                    skull.setOwner(player.getName());
                    skull.update();

                }
            }
            terra.getUtils().kickPlayer(player);
        } else {

            UHCPlayer uhcPlayer = UHCPlayer.getByName(player.getName());
            uhcPlayer.setDeathLocation(player.getLocation());
            uhcPlayer.setInventory(player.getInventory().getContents());
            uhcPlayer.setArmor(player.getInventory().getArmorContents());
            uhcPlayer.setPlayerState(PlayerState.DEAD);
            uhcPlayer.setLevels(player.getLevel());
            uhcPlayer.getStatistics().getDeaths().increment();
            terra.getGameManager().getPlayers().remove(uhcPlayer);
            if (!terra.getScenarioManager().getScenarioByName("Timebomb").isEnabled() || !terra.getScenarioManager().getScenarioByName("Golden Retriever").isEnabled()) {
                player.getWorld().getBlockAt(player.getLocation()).setType(Material.NETHER_FENCE);
                player.getWorld().getBlockAt(player.getLocation().add(0.0, 1.0, 0.0)).setType(Material.SKULL);
                final Block b = player.getWorld().getBlockAt(player.getLocation().add(0.0, 1.0, 0.0));
                b.setData((byte) 1);
                final BlockState state = player.getWorld().getBlockAt(player.getLocation().add(0.0, 1.0, 0.0)).getState();
                if (state instanceof Skull) {
                    final Skull skull = (Skull) state;
                    skull.setSkullType(SkullType.PLAYER);
                    skull.setOwner(player.getName());
                    skull.update();

                }
            }
            terra.getUtils().kickPlayer(player);
        }
    }


    public String formatDamage(double health) {
        if (health >= 15) {
            return ChatColor.GREEN + "" + terra.getUtils().getNf().format(health);
        } else if (health >= 10) {
            return ChatColor.YELLOW + "" + terra.getUtils().getNf().format(health);
        } else if (health >= 5) {
            return ChatColor.RED + "" + terra.getUtils().getNf().format(health);
        } else {
            return ChatColor.DARK_RED + "" + terra.getUtils().getNf().format(health);
        }
    }
}
