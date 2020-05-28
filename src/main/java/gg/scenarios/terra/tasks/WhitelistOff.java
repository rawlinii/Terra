package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.utils.HotBarMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class WhitelistOff {
    int taskId;
    private Terra uhc = Terra.getInstance();
    int count;
    public WhitelistOff(int diff) {
        count = diff;
        taskId = Bukkit.getServer().getScheduler().runTaskTimer(uhc, () -> {

            count--;
            String countDown = uhc.getUtils().convertToNice(count);
            Bukkit.getOnlinePlayers().forEach(p ->{
                HotBarMessage.sendHotBarMessage(p, ChatColor.GRAY + "Whitelist is off in " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN +countDown);
            });

            if (count == 1){
                uhc.getGameManager().setWhitelistEnabled(false);
                uhc.getUtils().broadcast(ChatColor.DARK_GRAY + "the whitelist has been " + ChatColor.DARK_RED + "disabled.");

            }
            if (count == -1) {

                if (uhc.getGameManager().getTeamState() == TeamState.SOLO){
                    new WhitelistOn(180);
                }else{
                    new WhitelistOn(420);
                }
                stopScheduler();
            }
        }, 0L, 20L).getTaskId();
    }
    public void stopScheduler(){
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }
}
