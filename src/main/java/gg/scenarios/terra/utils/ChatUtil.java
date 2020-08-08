package gg.scenarios.terra.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@UtilityClass
public class ChatUtil {

    @Getter
    private final String prefix = ChatUtil.format("&8[&cUHC&8] ");

    public String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String formatWithPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', prefix + message);
    }
    public void broadcast(String message){
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(formatWithPrefix(message)));
    }
    public void broadcast(String message, String permssion){
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permssion)).forEach(player -> player.sendMessage(formatWithPrefix(message)));
    }
}
