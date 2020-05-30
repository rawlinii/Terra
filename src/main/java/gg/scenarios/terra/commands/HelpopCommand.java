package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
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

public class HelpopCommand implements CommandExecutor {


    private Terra terra = Terra.getInstance();

    private static int helpOp =1;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Player command only.");
        } else {
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatColor.DARK_GRAY + "Please specify a " + ChatColor.DARK_RED + "message" + ChatColor.DARK_GRAY + ".");
            } else {
                StringBuilder message = new StringBuilder();
                for (String part : args) {
                    if (!message.toString().equals("")) message.append(" ");
                    message.append(part);
                }
                for (UUID uuid : terra.getGameManager().getMods()) {
                    Player mod = Bukkit.getPlayer(uuid);
                    if (mod != null) {
                        TextComponent msg = new TextComponent( ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED +" " + ChatColor.BOLD +"Helpop #" + helpOp +ChatColor.DARK_GRAY +"] " + ChatColor.WHITE +player.getName() + ChatColor.GOLD + " - " +ChatColor.GRAY + message);
                        msg.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/rhelpop " + player.getName() + " " ) );
                        msg.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Click to reply to this helpop!" ).create()));
                        mod.spigot().sendMessage(msg);
                        helpOp++;
                    }
                }
                sender.sendMessage(ChatColor.YELLOW + "Your request has been sent!");
            }
        }
        return false;
    }
}
