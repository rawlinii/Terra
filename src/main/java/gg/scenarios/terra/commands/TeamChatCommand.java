package gg.scenarios.terra.commands;

import com.google.common.base.Joiner;
import gg.scenarios.terra.Terra;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class TeamChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can send private messages.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <message>");
            return true;
        }

        Team team = Terra.getInstance().getTeams().getTeam(player);

        if (team == null) {
            sender.sendMessage(ChatColor.RED + "You are not on a team.");
            return true;
        }

        String format = "";
        String message = Joiner.on(' ').join(args);

        format = format.replace("{name}", player.getName());
        format = format.replace("{message}", message);

        Utils.broadcastToTeam(team, Terra.getInstance().getReference().getTeam() + player.getPlayer().getName() + ": " + message);
        return true;
    }
}
