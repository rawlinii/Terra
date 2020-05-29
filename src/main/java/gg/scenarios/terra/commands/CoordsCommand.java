package gg.scenarios.terra.commands;


import gg.scenarios.terra.Terra;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class CoordsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can send their coords.");
            return true;
        }

        Player player = (Player) sender;
        Team team = Terra.getInstance().getTeams().getTeam(player);

        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You are not on a team.");
            return true;
        }


        Location loc = player.getLocation();
        String format = "&7X: &a{x} &7Y: &a{y} &7Z: &a{z} &8(&c{dimention}&8)";

        format = format.replace("{name}", player.getName());
        format = format.replace("{x}", "" + loc.getBlockX());
        format = format.replace("{y}", "" + loc.getBlockY());
        format = format.replace("{z}", "" + loc.getBlockZ());
        format = format.replace("{dimention}", environment(player.getWorld()));

        Utils.broadcastToTeam(team, Terra.getInstance().getReference().getTeam() + player.getPlayer().getName() + ": " + format);
        return true;
    }


    private String environment(World world) {
        switch (world.getEnvironment()) {
            case NORMAL:
                return "Overworld";
            case NETHER:
                return "Nether";
            case THE_END:
                return "The End";
            default:
                return "Unknown";
        }
    }
}