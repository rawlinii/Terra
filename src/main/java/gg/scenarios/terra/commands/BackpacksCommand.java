package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.scenarios.type.Backpacks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;

public class BackpacksCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (Terra.getInstance().getScenarioManager().getScenarioByName("Backpacks").isEnabled()) {
                if (Terra.getInstance().getGameManager().getGameState() == GameState.STARTED) {
                    if (Terra.getInstance().getGameManager().getTeamState() == TeamState.SOLO){
                        player.sendMessage(ChatColor.RED + "This is not a teamgame.");
                    }else {
                        if (args.length == 0) {
                            Team team = Terra.getInstance().getTeams().getTeam(player);
                            Inventory teamInv = Backpacks.backpacks.get(team);
                            if (teamInv == null) {
                                Inventory ti = Bukkit.createInventory(null, 27, ChatColor.RED + "Backpack");
                                Backpacks.backpacks.put(team, ti);
                                player.openInventory(ti);
                            } else {
                                player.openInventory(teamInv);
                            }
                        } else {
                            if (player.hasPermission("backpack.open.other")) {
                                Team team = Terra.getInstance().getTeams().getTeam(Bukkit.getPlayer(args[0]));
                                Inventory teamInv = Backpacks.backpacks.get(team);
                                if (teamInv == null) {
                                    player.sendMessage(ChatColor.RED + "That player has no backpack!");
                                } else {
                                    player.openInventory(teamInv);
                                }
                            }
                        }
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "Backpacks is not enabled.");
            }
        } else {
            sender.sendMessage("In game only");
        }
        return false;
    }
}