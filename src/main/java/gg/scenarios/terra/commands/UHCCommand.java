package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.managers.profiles.PlayerState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.tasks.*;
import gg.scenarios.terra.utils.ConfigOptions;
import gg.scenarios.terra.utils.GuiBuilder;
import gg.scenarios.terra.utils.HotBarMessage;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class UHCCommand implements CommandExecutor {

    private Terra uhc = Terra.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("in game only");
        } else {
            Player player = (Player) sender;
            if (player.hasPermission("uhc.host")) {
                if (!(args.length > 0)) {
                    player.sendMessage(ChatColor.GOLD + "--------- UHC HELP --------");
                    player.sendMessage(ChatColor.DARK_GREEN + "/uhc start");
                    player.sendMessage(ChatColor.DARK_AQUA + "/uhc host");
                    player.sendMessage(ChatColor.AQUA + "/uhc mod");
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "/uhc demod");
                    player.sendMessage(ChatColor.RED + "/uhc config");
                    player.sendMessage(ChatColor.GRAY + "/uhc scenarios");
                    player.sendMessage(ChatColor.GOLD + "---------------------------");
                } else if (args[0].equals("scenarios")) {
                    Inventory scen = Bukkit.createInventory(null, 54, "Scenarios");
                    int i = 0;
                    for (Scenario scenario : uhc.getScenarioManager().getScenarios()) {
                        scen.setItem(i, scenario.getAdminItemStack());
                        i++;
                    }
                    player.openInventory(scen);
                } else if (args[0].equalsIgnoreCase("start")) {
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Game is now starting");
                    player.performCommand("heal all");
                    player.performCommand("whitelist all");
                    player.performCommand("whitelist on");
                    Bukkit.getServer().getOnlinePlayers().forEach(player1 -> player1.getInventory().clear());
                    if (uhc.getGameManager().getTeamState() == TeamState.TEAM) {
                        uhc.getGameManager().getPlayers().stream().filter(UHCPlayer::isOnline).filter(UHCPlayer::isInLobby).forEach(uhcPlayer -> {
                            uhcPlayer.getPlayer().performCommand("team create");
                        });
                    }
                    new PrepareGameTask();
                } else if (args[0].equalsIgnoreCase("config")) {
                    player.openInventory(ConfigOptions.config());
                } else if (args[0].equalsIgnoreCase("undeathban")) {
                    try {
                        UHCPlayer player1 = UHCPlayer.getByName(args[1]);
                        if (player1.getPlayerState().equals(PlayerState.DEAD)) {
                            if (player1.isOnline()) {
                                player1.getPlayer().kickPlayer(ChatColor.RED + "Respawned rejoin");
                            }
                            player1.setPlayerState(PlayerState.RESPAWNED);
                            uhc.getUtils().broadcast(ChatColor.GREEN + "" + ChatColor.BOLD + args[1] + " has been respawned!");
                        }
                    } catch (NullPointerException e) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', uhc.getReference().getError() + " &cCannot respawn player"));
                    }
                } else if (args[0].equalsIgnoreCase("host")) {
                    if (uhc.getGameManager().getHost() != null) {
                        player.sendMessage(ChatColor.RED + "This game already has a host");
                    } else {
                        uhc.getGameManager().setHost(UHCPlayer.getByName(player.getName()));
                        player.sendMessage(ChatColor.RED + "You are now the host of the game");
                        uhc.getUtils().toggle(player);
                        Location location = new Location(Bukkit.getWorld("uhc"), 0, 100, 0);
                        player.teleport(location);
                        UHCPlayer.getByUUID(player.getUniqueId()).setPlayerState(PlayerState.HOST);
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    uhc.getUtils().unloadWorld(Bukkit.getWorld("uhc"));
                    Bukkit.getScheduler().runTaskLater(uhc, () -> {
                        uhc.getUtils().deleteWorld("uhc");
                    }, 20 * 30);

                } else if (args[0].equalsIgnoreCase("stop")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "restart");
                } else if (args[0].equalsIgnoreCase("mod")) {
                    if (uhc.getGameManager().getMods().contains(player.getUniqueId())) {
                        UHCPlayer uhcPlayer = UHCPlayer.getByName(player.getName());

                        uhc.getUtils().toggle(player);
                        player.setGameMode(GameMode.SURVIVAL);
                        uhcPlayer.setPlayerState(PlayerState.LOBBY);
                        uhcPlayer.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
                        if (uhc.getGameManager().getGameState() == GameState.LOBBY) {
                            uhc.getGameManager().getPlayers().add(uhcPlayer);
                        }
                    } else {
                        uhc.getUtils().toggle(player);
                        player.setGameMode(GameMode.CREATIVE);
                        UHCPlayer uhcPlayer = UHCPlayer.getByName(player.getName());
                        uhcPlayer.setPlayerState(PlayerState.MODERATOR);
                        uhc.getGameManager().getPlayers().remove(uhcPlayer);
                        Location location = new Location(Bukkit.getWorld("uhc"), 0, 100, 0);
                        player.teleport(location);
                    }
                } else if (args[0].equalsIgnoreCase("teams")) {
                    GuiBuilder guiBuilder = new GuiBuilder();
                    guiBuilder.name("&aUHC Teams Settings");
                    guiBuilder.rows(3);
                    guiBuilder.item(0, new ItemCreator(Material.STAINED_GLASS, 3).setName("&6Random &7Teams").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(9, new ItemCreator(Material.STAINED_GLASS, 6).setName("&cR&7v&bB &7Teams").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(18, new ItemCreator(Material.STAINED_GLASS, 4).setName("&cR&7v&aG&7v&bB&7v&eY &7Teams").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(6, new ItemCreator(Material.STAINED_GLASS, 1).setName("&7Auction").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(15, new ItemCreator(Material.STAINED_GLASS, 2).setName("&7Captains").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(24, new ItemCreator(Material.STAINED_GLASS, 1).setName("&7Picked").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(3, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(4, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(5, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(12, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(13, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(14, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(21, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(22, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));
                    guiBuilder.item(23, new ItemCreator(Material.GLASS).setName(" ").get()).onClick(event -> event.setCancelled(true));


                    guiBuilder.item(7, new ItemCreator(Material.PAPER).setName("&7Set Game to &7Auction").setLore(Arrays.asList("&6&LNOTE: ADMIN&^")).get()).onClick(event -> {
                        event.setCancelled(true);
                        uhc.getGameManager().setTeamState(TeamState.SLAVEMARKET);
                        player.sendMessage(ChatColor.GRAY + "Teams have been set to Auction");
                    });

                    guiBuilder.item(16, new ItemCreator(Material.PAPER).setName("&7Set Game to &7Captains").setLore(Arrays.asList("&6&LNOTE: ADMIN&^")).get()).onClick(event -> {
                        event.setCancelled(true);
                        uhc.getGameManager().setTeamState(TeamState.CAPTAINS);
                        player.sendMessage(ChatColor.GRAY + "Teams have been set to Auction");
                    });
                    guiBuilder.item(25, new ItemCreator(Material.PAPER).setName("&7Set Game to &7Picked").setLore(Arrays.asList("&6&LNOTE: ADMIN&^")).get()).onClick(event -> {
                        event.setCancelled(true);
                        uhc.getGameManager().setTeamState(TeamState.SLAVEMARKET);
                        player.sendMessage(ChatColor.GRAY + "Teams have been set to Picked");
                    });
                    guiBuilder.item(1, new ItemCreator(Material.PAPER).setName("&7Set Game to &6Random &7Teams").get()).onClick(event -> {
                        event.setCancelled(true);
                        uhc.getGameManager().setTeamState(TeamState.RANDOM);
                        player.sendMessage(ChatColor.GRAY + "Teams have been set to Random");
                    });
                    guiBuilder.item(10, new ItemCreator(Material.PAPER).setName("&7Set Game to &cR&7v&bB &7Teams").get()).onClick(event -> {
                        event.setCancelled(true);
                        uhc.getGameManager().setTeamState(TeamState.RvB);
                        player.sendMessage(ChatColor.GRAY + "Teams have been set to RvB");
                    });
                    guiBuilder.item(19, new ItemCreator(Material.PAPER).setName("&7Set Game to &cR&7v&aG&7v&bB&7v&eY &7Teams").get()).onClick(event -> {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.GRAY + "Teams have been set to RvBvGvY");
                    });
                    guiBuilder.item(2, new ItemCreator(Material.BEACON).setName("&7Generate &6Random &7Teams").get()).onClick(event -> {
                        event.setCancelled(true);
                        new RandomTeamsTask();
                    });
                    guiBuilder.item(11, new ItemCreator(Material.BEACON).setName("&7Generate &cR&7v&bB &7Teams").get()).onClick(event -> {
                        event.setCancelled(true);
                        new RvBTask();
                    });
                    guiBuilder.item(20, new ItemCreator(Material.BEACON).setName("&7Generate &cR&7v&aG&7v&bB&7v&eY &7Teams").get()).onClick(event -> {
                        event.setCancelled(true);
                        new RvGvBvYTask();
                    });
                    guiBuilder.item(8, new ItemCreator(Material.BEACON).setName("&7Start &6Auction").setLore(Arrays.asList("&6&LNOTE: ADMIN&^")).get()).onClick(event -> {
                        event.setCancelled(true);
                        new AuctionTask(); //TODO: Make this work
                    });
                    guiBuilder.item(17, new ItemCreator(Material.BEACON).setName("&7Start &6Captains").setLore(Arrays.asList("&6&LNOTE: ADMIN&^")).get()).onClick(event -> {
                        event.setCancelled(true);
                        //TODO: MAKE CAPTAINS
                    });
                    guiBuilder.item(26, new ItemCreator(Material.BEACON).setName("&7Started &6Picked").setLore(Arrays.asList("&6&LNOTE: ADMIN&^")).get()).onClick(event -> {
                        event.setCancelled(true);
                        //TODO: START PICKED
                    });

                    player.openInventory(guiBuilder.make());

                } else if (args[0].equalsIgnoreCase("slots")) {
                    if (args[1] == null){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', uhc.getReference().getError() + "&7Please specify a &6number&7."));
                    }else{
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', uhc.getReference().getMain() + "&7Set slots to &6" + args[1] + "&7."));

                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "No perm");
            }
        }
        return false;
    }
}
