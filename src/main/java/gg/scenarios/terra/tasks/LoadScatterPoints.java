package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.utils.HotBarMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LoadScatterPoints {

    private Terra terra = Terra.getInstance();
    int pointsToLoad = Bukkit.getServer().getOnlinePlayers().size() + 10;
    private int taskId;
    int count = 0;
    public LoadScatterPoints(){
        taskId = Bukkit.getScheduler().runTaskTimer(terra, () -> {

            Location location = terra.getGameManager().findLocation();
            terra.getGameManager().scatterLocations[count] = location;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                HotBarMessage.sendHotBarMessage(onlinePlayer, ChatColor.AQUA + "Loading Scatter Points... " + ChatColor.GRAY + "[" + ChatColor.RED + count + ChatColor.GRAY + "/" + ChatColor.GREEN + (Bukkit.getOnlinePlayers().size() + 10) + ChatColor.GRAY + "]");
            }

         if (count == pointsToLoad){
             Bukkit.getScheduler().runTaskLater(terra, ScatterTask::new, 20*30L);
             terra.getUtils().broadcast(terra.getReference().getMain() + "Scattering in 30 seconds...");
             this.cancel();
         }else{
             count ++;

         }
        }, 4L, 0L).getTaskId();
    }

    private void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
