package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.Reference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.swing.text.Document;

public class TpCommand implements CommandExecutor {

    private Terra terra;
    private Reference reference;

    public TpCommand(Terra terra) {
        this.terra = terra;
        reference = terra.getReference();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("uhc.command.tp")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "/tp <player>");
                    player.sendMessage(ChatColor.RED + "/tp <player> <player>");
                    player.sendMessage(ChatColor.RED + "/tp <x> <y> <z>");
                } else {
                    if (args.length == 3) {
                        try {
                            double x = (double) Double.parseDouble(args[0]);
                            double y = (double) Double.parseDouble(args[1]);
                            double z = (double) Double.parseDouble(args[2]);
                            Location location = new Location(player.getWorld(), x, y, z);
                            player.teleport(location);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + reference.primColor + "You teleported to " + reference.primColor + "x" +reference.secColor + x + reference.primColor + " y" +reference.secColor + y+ reference.primColor + " z" +reference.secColor + z);
                        } catch (NumberFormatException nfe) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getError() + "Please use proper coordinates"));
                        }
                    } else if (args.length == 2) {

                        Location plLocation = Bukkit.getPlayer(args[1]).getLocation();
                        Player target = Bukkit.getPlayer(args[0]);
                        if (target == null || Bukkit.getPlayer(args[1]) == null){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getError() + "&cOne of the players is offline"));
                            return false;
                        }
                        target.teleport(plLocation);

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + reference.primColor + "You teleported " + reference.secColor + args[0] + reference.primColor+" to " + reference.secColor + args[1]);
                    }else if (args.length == 1){

                        Location plLocation = Bukkit.getPlayer(args[0]).getLocation();
                        if ( Bukkit.getPlayer(args[0]) == null){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getError() + "&cOne of the players is offline"));
                            return false;
                        }
                        player.teleport(plLocation);

                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain()) + reference.primColor + "You teleported to " + reference.secColor + args[0]);

                    }else{
                        player.sendMessage(ChatColor.RED + "/tp <player>");
                        player.sendMessage(ChatColor.RED + "/tp <player> <player>");
                        player.sendMessage(ChatColor.RED + "/tp <x> <y> <z>");

                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain() + "Only players can use this command."));
        }
        return false;
    }
}
