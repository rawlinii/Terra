package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.Reference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class HealCommand implements CommandExecutor{

    private Reference reference = Terra.getInstance().getReference();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("heal")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Player command only.");
            } else {
                Player player = (Player) sender;
                if (player.hasPermission("uhc.heal")) {
                    if (args.length < 1) {
                        player.sendMessage(ChatColor.YELLOW + "Please specify a " + ChatColor.LIGHT_PURPLE + "player/all" + ChatColor.YELLOW + ".");
                    } else {
                        if (args[0].equalsIgnoreCase("all")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + ChatColor.YELLOW + "You have healed " + ChatColor.LIGHT_PURPLE + "everyone" + ChatColor.YELLOW + ".");

                            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                onlinePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + ChatColor.YELLOW + "You have been set to full health by " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.YELLOW + ".");
                                onlinePlayer.setHealth(20.0);
                                onlinePlayer.setFireTicks(0);
                                onlinePlayer.setSaturation(10.0f);
                                onlinePlayer.setFoodLevel(20);
                            }
                        }else {
                            Player target = Bukkit.getPlayer(args[0]);
                            target.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + ChatColor.YELLOW + "You have been set to full health by " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.YELLOW + ".");
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + ChatColor.YELLOW + "You have healed " + ChatColor.LIGHT_PURPLE + target.getName() + ChatColor.YELLOW + ".");
                            target.setHealth(20.0);
                            target.setFireTicks(0);
                            target.setSaturation(10.0f);
                            target.setFoodLevel(20);
                        }
                    }
                }
            }
        }
        return false;
    }
}
