package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.events.GameStateChangeEvent;
import gg.scenarios.terra.events.KingAddEvent;
import gg.scenarios.terra.events.KingRemoveEvent;
import gg.scenarios.terra.scenarios.type.Kings;
import gg.scenarios.terra.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KingsCommand implements CommandExecutor {

    private final Terra uhc;

    public KingsCommand(Terra uhc) {
        this.uhc = uhc;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("uhc.host")) {
                if (uhc.getScenarioManager().getScenarioByName("Kings").isEnabled()) {
                    if (args.length == 0) {
                        player.sendMessage(ChatUtil.format("&7&m---------------------"));
                        player.sendMessage(ChatUtil.format("&7/kings addking <ign> - sets/adds king"));
                        player.sendMessage(ChatUtil.format("&7/kings rmking <ign> - removes a hosts"));
                        player.sendMessage(ChatUtil.format("&7&m---------------------"));
                    } else {
                        if (args[0].equals("addking")) {
                            if (args[1] == null) {
                                player.sendMessage("Please specify a player");
                            } else {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target == null) {
                                    player.sendMessage(ChatUtil.format("Player is offline!"));
                                } else {
                                    if (Kings.kings.contains(target.getUniqueId())) {
                                        player.sendMessage(ChatUtil.format("&cPlayer is already a king"));
                                        return false;
                                    }
                                    Kings.kings.add(target.getUniqueId());
                                    ChatUtil.broadcast("&d" + target.getName() + " &7has been added as a king!");
                                    Bukkit.getPluginManager().callEvent(new KingAddEvent(target.getUniqueId()));
                                }
                            }
                        } else if (args[0].equals("rmking")) {
                            if (args[1] == null) {
                                player.sendMessage("Please specify a player");
                            } else {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target == null) {
                                    player.sendMessage(ChatUtil.format("Player is offline!"));
                                } else {
                                    if (!Kings.kings.contains(target.getUniqueId())) {
                                        player.sendMessage(ChatUtil.format("&cPlayer is not a host"));
                                        return false;
                                    }
                                    Kings.kings.remove(target.getUniqueId());
                                    ChatUtil.broadcast("&d" + target.getName() + " &7has been removed as a king!");
                                    Bukkit.getPluginManager().callEvent(new KingRemoveEvent(target.getUniqueId()));

                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
