package gg.scenarios.terra.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class rHelpopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player command only.");
        } else {

            Player player = (Player) sender;
            if (!player.hasPermission("help.view")) return true;

            if (args.length < 2) {
                player.sendMessage(ChatColor.DARK_GRAY + "Please specify a " + ChatColor.DARK_RED + "message" + ChatColor.DARK_GRAY + ".");
            } else {
                Player target = Bukkit.getPlayer(args[0]);

                StringBuilder message = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                target.sendMessage(ChatColor.GRAY + "Helpop reply from: " +ChatColor.DARK_RED + sender.getName() + ChatColor.DARK_GRAY +":" + ChatColor.GRAY + message);
            }
        }
        return false;
    }
}
