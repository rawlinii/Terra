package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import org.json.simple.JSONValue;

import java.net.URL;
import java.util.UUID;

public class WhiteListCommand implements CommandExecutor {

    private Terra uhc = Terra.getInstance();
    public static int count;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("whitelist")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("in game only");
            } else {
                Player player = (Player) sender;

                if (player.hasPermission("uhc.whitelist")) {

                    if (!(args.length > 0)) {
                        player.sendMessage(ChatColor.GOLD + "--------- WHITELIST HELP --------");
                        player.sendMessage(ChatColor.DARK_GREEN + "/wl on|off|all");
                        player.sendMessage(ChatColor.DARK_AQUA + "/wl add <name>");
                        player.sendMessage(ChatColor.DARK_AQUA + "/wl remove <name>");
                        player.sendMessage(ChatColor.GOLD + "--------- WHITELIST HELP --------");
                    } else if (args[0].equals("on")) {
                        uhc.getGameManager().setWhitelistEnabled(true);
                        player.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.LIGHT_PURPLE + "enabled" + ChatColor.YELLOW + " the whitelist");
                    } else if (args[0].equalsIgnoreCase("off")) {
                        uhc.getGameManager().setWhitelistEnabled(false);
                        player.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.LIGHT_PURPLE + "disabled" + ChatColor.YELLOW + " the whitelist");
                    } else if (args[0].equalsIgnoreCase("add")) {
                        if (args[1] == null){
                            player.sendMessage(ChatColor.YELLOW + "please enter a username");

                        }else {
                            uhc.getGameManager().getWhitelist().add(UUID.fromString(insertDashUUID(getUUID(args[1]))));
                            //Utils.getOfflinePlayer(args[1], offlinePlayer -> uhc.getGameManager().getWhitelist().add(offlinePlayer.getUniqueId()));
                            player.sendMessage(ChatColor.YELLOW + "You have added " + ChatColor.LIGHT_PURPLE + args[1] + ChatColor.YELLOW + " to the whitelist");
                        }
                    } else if (args[0].equalsIgnoreCase("all")) {
                        uhc.getGameManager().getWhitelist().removeAll(uhc.getGameManager().getWhitelist());
                        uhc.getGameManager().getPlayers().stream().filter(UHCPlayer::isOnline).forEach(uuid -> {
                            uhc.getGameManager().getWhitelist().add(uuid.getPlayer().getUniqueId());
                            count++;
                        });
                        uhc.getGameManager().getPlayers().removeAll(uhc.getGameManager().getPlayers());
                        uhc.getGameManager().getWhitelist().forEach(uuid -> {
                            uhc.getGameManager().getPlayers().add(UHCPlayer.getByUUID(uuid));
                        });
//                        uhc.getGameManager().getWhitelist().forEach(uuid -> {
//                            if (UHCPlayer.getByUUID(uuid).isOnline()) {
//                                if (UHCPlayer.getByUUID(uuid).isSpectating()) {
//                                    try {
//                                        uhc.getGameManager().getPlayers().remove(UHCPlayer.getByUUID(uuid));
//                                    } catch (Exception e) {
//
//                                    }
//                                    uhc.getGameManager().getPlayers().add(UHCPlayer.getByUUID(uuid));
//                                }
//                            }
//                        });
                        player.sendMessage(ChatColor.YELLOW + "You have added " + ChatColor.LIGHT_PURPLE + uhc.getGameManager().getPlayers().size() + ChatColor.YELLOW + " players to the whitelist");

                    }
                } else {
                    player.sendMessage("no perm kiddo");
                }
            }
        }
        return false;
    }

    public static String insertDashUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(13, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(18, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(23, "-");

        return sb.toString();
    }

    public static String getUUID(String playerName) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://api.mojang.com/users/profiles/minecraft/" + playerName)
                    .header("accept", "application/json")
                    .asJson();

            return response.getBody().getObject().getString("id");
        } catch (Exception e) {
            return "";
        }
    }

}
