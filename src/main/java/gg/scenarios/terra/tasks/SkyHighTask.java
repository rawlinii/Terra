package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class SkyHighTask {
    public SkyHighTask() {
        Bukkit.getScheduler().runTaskTimer(Terra.getInstance(), () -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getLocation().getY() <= 101) {
                            if (player.getGameMode() == GameMode.SURVIVAL) {
                                player.sendMessage(ChatColor.RED + "You have taken 1 heart for being under y101 in a SkyHigh game, please build up");
                                player.damage(2);
                            }
                        }
                    }
                }
                , 0, 20 * 30L);
    }

}
