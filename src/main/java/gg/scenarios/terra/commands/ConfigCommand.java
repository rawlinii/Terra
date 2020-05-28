package gg.scenarios.terra.commands;

import gg.scenarios.terra.utils.ConfigOptions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConfigCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player command only.");
        } else {
            Player player = (Player) sender;

            player.openInventory(ConfigOptions.config("Config"));
        }

        return false;
    }
}