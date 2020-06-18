package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ListCommand implements CommandExecutor {

    private Terra uhc;

    public ListCommand(Terra uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("list")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Player command only.");
            } else {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.WHITE +"" + ChatColor.STRIKETHROUGH + "----------------------");
                player.sendMessage(ChatColor.RED +" ");
                player.sendMessage(ChatColor.BLUE +"Host: " +(uhc.getGameManager().getHost() != null ? ChatColor.YELLOW + uhc.getGameManager().getHost().getPlayer().getName() :  ChatColor.YELLOW +"N/A"));
                player.sendMessage(ChatColor.GREEN +"Mods: " +ChatColor.YELLOW + mods());
                player.sendMessage(ChatColor.AQUA +"Players: " +ChatColor.YELLOW + Bukkit.getOnlinePlayers().size());
                player.sendMessage(ChatColor.RED +" ");
                player.sendMessage(ChatColor.WHITE +"" + ChatColor.STRIKETHROUGH + "----------------------");

            }
        }
        return false;
    }

    private String mods(){
        StringBuilder stringBuilder = new StringBuilder();
        if (uhc.getGameManager().getMods().isEmpty()) return "N/A";
        for(UUID uuid : uhc.getGameManager().getMods()){
            if (Bukkit.getPlayer(uuid) != null) {
                stringBuilder.append(Bukkit.getPlayer(uuid).getName());
                stringBuilder.append(", ");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }
}