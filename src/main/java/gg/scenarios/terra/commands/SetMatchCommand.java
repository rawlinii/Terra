package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.Matchpost;
import gg.scenarios.terra.teams.Teams;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class SetMatchCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission("uhc.command.setmatch")) {
                if (args.length == 1){
                    try{
                        if (args[0].equalsIgnoreCase("pog")){
                            System.out.println(Terra.getInstance().getGameManager().toString());
                        }else {
                            String post = args[0];
                            new Matchpost(post);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        player.sendMessage(ChatColor.RED +"Could not validate matchpost");
                    }
                }else if (args.length == 0){
                    player.sendMessage("§a/setmatch <matchPost>");
                }else{
                    player.sendMessage("§a/setmatch <matchPost>");
                }
            }else{
                player.sendMessage(ChatColor.RED + "No permission.");
            }
        }else{
            sender.sendMessage(ChatColor.RED + "Im game command only");
        }
        return false;
    }
}
