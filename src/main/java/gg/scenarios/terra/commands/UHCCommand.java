package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.profiles.PlayerState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.tasks.PrepareGameTask;
import gg.scenarios.terra.tasks.ScatterTask;
import gg.scenarios.terra.utils.ConfigOptions;
import gg.scenarios.terra.utils.HotBarMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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


                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    uhc.getUtils().unloadWorld(Bukkit.getWorld("uhc"));
                    Bukkit.getScheduler().runTaskLater(uhc, () -> {
                        uhc.getUtils().deleteWorld("uhc");
                    }, 20 * 30);

                } else if (args[0].equalsIgnoreCase("stop")) {
                    Bukkit.shutdown();
                } else if (args[0].equalsIgnoreCase("mod")) {
                    if (uhc.getGameManager().getMods().contains(player.getUniqueId())) {
                        UHCPlayer uhcPlayer = UHCPlayer.getByName(player.getName());

                        uhc.getUtils().toggle(player);
                        player.setGameMode(GameMode.SURVIVAL);
                        uhcPlayer.setPlayerState(PlayerState.LOBBY);
                        uhcPlayer.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation());
                        uhc.getGameManager().getPlayers().add(uhcPlayer);
                    } else {
                        uhc.getUtils().toggle(player);
                        player.setGameMode(GameMode.CREATIVE);
                        UHCPlayer uhcPlayer = UHCPlayer.getByName(player.getName());
                        uhcPlayer.setPlayerState(PlayerState.MODERATOR);
                        uhc.getGameManager().getPlayers().remove(uhcPlayer);
                        Location location = new Location(Bukkit.getWorld("uhc"), 0, 100, 0);
                        player.teleport(location);
                    }
                }

            } else {
                player.sendMessage(ChatColor.RED + "No perm");
            }
        }
        return false;
    }
}
