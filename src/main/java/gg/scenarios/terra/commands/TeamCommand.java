package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class TeamCommand implements CommandExecutor {


    private Terra main = Terra.getInstance();
    private Utils utils = main.getUtils();
    private GameManager gameManager = main.getGameManager();
    private Reference reference = main.getReference();
    public static ArrayList<Team> teamArrayList = new ArrayList<>();
    public static HashMap<Player, ArrayList<Player>> invites = new HashMap<Player, ArrayList<Player>>();

    @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("team")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can create and manage teams.");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                if (gameManager.getTeamState().equals(TeamState.SOLO) || gameManager.getTeamState().equals(TeamState.RvB) || gameManager.getTeamState().equals(TeamState.RvGvBvY)) {
                    player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                    return true;
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + "Team help:");
                player.sendMessage("§7- §a/team create - Create a team.");
                player.sendMessage("§7- §a/team leave - Leave your team.");
                player.sendMessage("§7- §a/team invite <player> - Invite a player to your team.");
                player.sendMessage("§7- §a/team kick <player> - Kick a player from your team.");
                player.sendMessage("§7- §a/team accept <player> - Accept the players request.");
                player.sendMessage("§7- §a/team deny <player> - Deny the players request.");
                player.sendMessage("§7- §a/team info - Display your teammates.");
                if (player.hasPermission("uhc.teamadmin")) {
                    player.sendMessage("§7- §a/team clear - Clear all teams.");
                }
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (gameManager.getTeamState().equals(TeamState.SOLO) || gameManager.getTeamState().equals(TeamState.SLAVEMARKET) ||gameManager.getTeamState().equals(TeamState.RANDOM) || gameManager.getTeamState().equals(TeamState.RvGvBvY) || gameManager.getTeamState().equals(TeamState.RvB)) {
                        player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                        return true;
                    }

                    if (!gameManager.getGameState().equals(GameState.LOBBY)) {
                        player.sendMessage(ChatColor.RED + "You cannot do this command at the moment.");
                        return true;
                    }

                    if (player.getScoreboard().getPlayerTeam(player) != null) {
                        player.sendMessage(ChatColor.RED + "You are already on a team.");
                        return true;
                    }

                    ArrayList<Team> oteams = new ArrayList<Team>();

                    for (Team team : main.getTeams().getTeams()) {
                        if (team.getSize() == 0) {
                            oteams.add(team);
                            teamArrayList.add(team);
                        }
                    }

                    oteams.get(new Random().nextInt(oteams.size())).addPlayer(player);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + "Team created! Use /team invite <player> to invite a player.");
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (gameManager.getTeamState().equals(TeamState.SOLO) || gameManager.getTeamState().equals(TeamState.SLAVEMARKET) ||gameManager.getTeamState().equals(TeamState.RANDOM) || gameManager.getTeamState().equals(TeamState.RvGvBvY) || gameManager.getTeamState().equals(TeamState.RvB)) {
                        player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                        return true;
                    }

                    if (!gameManager.getGameState().equals(GameState.LOBBY)) {
                        player.sendMessage(ChatColor.RED + "You cannot do this command at the moment.");
                        return true;
                    }

                    Team team = player.getScoreboard().getPlayerTeam(player);

                    if (team == null) {
                        player.sendMessage(ChatColor.RED + "You are not on a team.");
                        return true;
                    }
                    team.removePlayer(player);
                    player.sendMessage(reference.getMain() + "You left your team.");
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (gameManager.getTeamState().equals(TeamState.SOLO) || gameManager.getTeamState().equals(TeamState.SLAVEMARKET)) {
                        player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                        return true;
                    }

                    player.sendMessage(ChatColor.RED + "Usage: /team invite <player>");
                } else if (args[0].equalsIgnoreCase("kick") || gameManager.getTeamState().equals(TeamState.SLAVEMARKET)) {
                    if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                        player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                        return true;
                    }

                    player.sendMessage(ChatColor.RED + "Usage: /team kick <player>");
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (gameManager.getTeamState().equals(TeamState.SOLO) || gameManager.getTeamState().equals(TeamState.SLAVEMARKET)) {
                        player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                        return true;
                    }

                    player.sendMessage(ChatColor.RED + "Usage: /team accept <player>");
                } else if (args[0].equalsIgnoreCase("deny") || gameManager.getTeamState().equals(TeamState.SLAVEMARKET)) {
                    if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                        player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                        return true;
                    }

                    player.sendMessage(ChatColor.RED + "Usage: /team deny <player>");
                } else if (args[0].equalsIgnoreCase("clear") || gameManager.getTeamState().equals(TeamState.SLAVEMARKET)) {
                    if (!player.hasPermission("uhc.teamadmin")) {
                        player.sendMessage(ChatColor.RED + "You cannot clear teams.");
                        return true;
                    }
                    for (Team team : main.getTeams().getTeams()) {
                        for (OfflinePlayer p : team.getPlayers()) {
                            team.removePlayer(p);
                        }
                    }
                    player.sendMessage(reference.getMain() + "Teams cleared.");
                } else if (args[0].equalsIgnoreCase("info")) {
                    Team team = player.getScoreboard().getPlayerTeam(player);

                    if (team == null) {
                        player.sendMessage(ChatColor.RED + "You are not on a team.");
                        return true;
                    }

                    StringBuilder list = new StringBuilder("");

                    for (OfflinePlayer players : team.getPlayers()) {
                        if (list.length() > 0) {
                            list.append("§a, ");
                        }

                        if (players.isOnline()) {
                            list.append(ChatColor.GREEN + players.getName());
                        } else {
                            list.append(ChatColor.RED + players.getName());
                        }
                    }

                    player.sendMessage(reference.getMain() + "Your teammates: (red name = offline)");
                    player.sendMessage(list.toString().trim());
                } else {
                    if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                        player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                        return true;
                    }

                    player.sendMessage(reference.getMain() + "Team help:");
                    player.sendMessage("§7- §a/team create - Create a team.");
                    player.sendMessage("§7- §a/team remove - Remove your team.");
                    player.sendMessage("§7- §a/team invite <player> - Invite a player to your team.");
                    player.sendMessage("§7- §a/team kick <player> - Kick a player from your team.");
                    player.sendMessage("§7- §a/team accept <player> - Accept the players request.");
                    player.sendMessage("§7- §a/team deny <player> - Deny the players request.");
                    player.sendMessage("§7- §a/team info - Display your teammates.");
                }
                return true;
            }


            Player target = Bukkit.getServer().getPlayer(args[1]);

            if (args[0].equalsIgnoreCase("create")) {
                if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                    player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                    return true;
                }

                if (!gameManager.getGameState().equals(GameState.LOBBY)) {
                    player.sendMessage(ChatColor.RED + "You cannot do this command at the moment.");
                    return true;
                }

                if (player.getScoreboard().getPlayerTeam(player) != null) {
                    player.sendMessage(ChatColor.RED + "You are already on a team.");
                    return true;
                }

                ArrayList<Team> oteams = new ArrayList<Team>();

                for (Team team : main.getTeams().getTeams()) {
                    if (team.getSize() == 0) {
                        oteams.add(team);
                    }
                }

                oteams.get(new Random().nextInt(oteams.size())).addPlayer(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',reference.getMain() + "Team created! Use /team invite <player> to invite a player."));
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                    player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                    return true;
                }

                if (!gameManager.getGameState().equals(GameState.LOBBY)) {
                    player.sendMessage(ChatColor.RED + "You cannot do this command at the moment.");
                    return true;
                }

                Team team = player.getScoreboard().getPlayerTeam(player);

                if (team == null) {
                    player.sendMessage(ChatColor.RED + "You are not on a team.");
                    return true;
                }

                team.removePlayer(player);
                player.sendMessage(reference.getMain() + "You left your team.");
                for (OfflinePlayer players : team.getPlayers()) {
                    if (players instanceof Player) {
                        ((Player) players).sendMessage(reference.getMain() + target.getName() + " left your team.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("invite")) {
                if (!gameManager.getGameState().equals(GameState.LOBBY)) {
                    player.sendMessage(ChatColor.RED + "You cannot do this command at the moment.");
                    return true;
                }

                Team team = player.getScoreboard().getPlayerTeam(player);

                if (team == null) {
                    player.sendMessage(ChatColor.RED + "You are not on a team.");
                    return true;
                }

                if (team.getSize() >= gameManager.getTeamSize()) {
                    player.sendMessage(ChatColor.RED + "Your team has reached the max teamsize.");
                    return true;
                }

                if (target == null) {
                    player.sendMessage(ChatColor.RED + "That player is not online.");
                    return true;
                }

                Team team1 = player.getScoreboard().getPlayerTeam(target);

                if (team1 != null) {
                    player.sendMessage(ChatColor.RED + "That player is already on a team.");
                    return true;
                }

                for (OfflinePlayer players : team.getPlayers()) {
                    if (players instanceof Player) {
                        ((Player) players).sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getTeam() + target.getName() + " were invited to your team."));
                    }
                }

                if (!invites.containsKey(player)) {
                    invites.put(player, new ArrayList<Player>());
                }
                invites.get(player).add(target);
                target.sendMessage(ChatColor.translateAlternateColorCodes('&',reference.getTeam() + "&7You have been invited to &3" + player.getName() + "&7's team."));
                target.sendMessage("§7- §aTo accept, type /team accept " + player.getName());
                target.sendMessage("§7- §aTo decline, type /team deny " + player.getName());
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                    player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                    return true;
                }

                if (!gameManager.getGameState().equals(GameState.LOBBY)) {
                    player.sendMessage(ChatColor.RED + "You cannot do this command at the moment.");
                    return true;
                }

                Team team = player.getScoreboard().getPlayerTeam(player);

                if (team == null) {
                    player.sendMessage(ChatColor.RED + "You are not on a team.");
                    return true;
                }

                if (target == null) {
                    player.sendMessage(ChatColor.RED + "That player is not online.");
                    return true;
                }

                Team team1 = player.getScoreboard().getPlayerTeam(target);

                if (team1 == null || team != team1) {
                    player.sendMessage(ChatColor.RED + "That player is not on your team.");
                    return true;
                }

                team.removePlayer(target);
                target.sendMessage(reference.getMain() + "You were kicked from your team.");

                for (OfflinePlayer players : team.getPlayers()) {
                    if (players instanceof Player) {
                        ((Player) players).sendMessage(reference.getMain() + target.getName() + " was kicked from your team.");
                    }
                }

            } else if (args[0].equalsIgnoreCase("accept")) {
                if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                    player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                    return true;
                }

                if (!gameManager.getGameState().equals(GameState.LOBBY)) {
                    player.sendMessage(ChatColor.RED + "You cannot do this command at the moment.");
                    return true;
                }

                if (target == null) {
                    player.sendMessage(ChatColor.RED + "That player is not online.");
                    return true;
                }

                if (player.getScoreboard().getPlayerTeam(player) != null) {
                    player.sendMessage(ChatColor.RED + "You are already on a team.");
                    return true;
                }

                if (invites.containsKey(target) && invites.get(target).contains(player)) {
                    Team team = target.getScoreboard().getPlayerTeam(target);
                    if (team == null) {
                        player.sendMessage(ChatColor.RED + "That player is not on a team.");
                        return true;
                    }

                    if (team.getSize() >= gameManager.getTeamSize()) {
                        player.sendMessage(ChatColor.RED + "That team has reached the max teamsize.");
                        return true;
                    }

                    player.sendMessage(reference.getMain() + "Request accepted.");
                    team.addPlayer(player);
                    for (OfflinePlayer players : team.getPlayers()) {
                        if (players instanceof Player) {
                            ((Player) players).sendMessage(ChatColor.translateAlternateColorCodes('&',reference.getMain() + "&3" + player.getName() + " &7joined your team."));
                        }
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "That player has not sent you any requests.");
                }
            } else if (args[0].equalsIgnoreCase("deny")) {
                if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                    player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                    return true;
                }

                if (!gameManager.getGameState().equals(GameState.LOBBY)) {
                    player.sendMessage(ChatColor.RED + "You cannot do this command at the moment.");
                    return true;
                }

                if (target == null) {
                    player.sendMessage(ChatColor.RED + "That player is not online.");
                    return true;
                }

                if (invites.containsKey(target) && invites.get(target).contains(player)) {
                    Team team = target.getScoreboard().getPlayerTeam(target);
                    if (team == null) {
                        player.sendMessage(ChatColor.RED + "That player is not on a team.");
                        return true;
                    }
                    target.sendMessage(reference.getMain() + player.getName() + " denied your request.");
                    player.sendMessage(reference.getMain() + "Request denied.");
                } else {
                    player.sendMessage(ChatColor.RED + "That player has not sent you any requests.");
                }
            } else if (args[0].equalsIgnoreCase("list")) {

                for (Team team : main.getTeams().getTeams()) {
                    player.sendMessage(team.getName());
                }
                return true;
            } else if (args[0].equalsIgnoreCase("info")) {
                Team team = player.getScoreboard().getPlayerTeam(player);

                if (team == null) {
                    player.sendMessage(ChatColor.RED + "You are not on a team.");
                    return true;
                }

                StringBuilder list = new StringBuilder("");

                for (OfflinePlayer players : team.getPlayers()) {
                    if (list.length() > 0) {
                        list.append("§a, ");
                    }

                    if (players.isOnline()) {
                        list.append(ChatColor.GREEN + players.getName());
                    } else {
                        list.append(ChatColor.RED + players.getName());
                    }
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain() + "Your teammates: (red name = offline)"));
                player.sendMessage(list.toString().trim());
            } else if (args[0].equalsIgnoreCase("clear")) {
                if (!player.hasPermission("uhc.teamadmin")) {
                    player.sendMessage(ChatColor.RED + "You cannot clear teams.");
                    return true;
                }
                for (Team team : main.getTeams().getTeams()) {
                    for (OfflinePlayer p : team.getPlayers()) {
                        team.removePlayer(p);
                    }
                }
                player.sendMessage(reference.getMain() + "Teams cleared.");
            } else {
                if (gameManager.getTeamState().equals(TeamState.SOLO)) {
                    player.sendMessage(ChatColor.RED + "This is an FFA or randomteams game.");
                    return true;
                }

                player.sendMessage(reference.getMain() + "Team help:");
                player.sendMessage("§7- §a/team create - Create a team.");
                player.sendMessage("§7- §a/team remove - Remove your team.");
                player.sendMessage("§7- §a/team invite <player> - Invite a player to your team.");
                player.sendMessage("§7- §a/team kick <player> - Kick a player from your team.");
                player.sendMessage("§7- §a/team accept <player> - Accept the players request.");
                player.sendMessage("§7- §a/team deny <player> - Deny the players request.");
                player.sendMessage("§7- §a/team info - Display your teammates.");
            }
        }
        return true;
    }
}