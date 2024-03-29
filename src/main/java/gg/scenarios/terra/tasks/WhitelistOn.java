package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.utils.HotBarMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

public class WhitelistOn {
    int taskId;
    private Terra uhc = Terra.getInstance();
    int count;

    public WhitelistOn(int diff) {
        count = diff;
        taskId = Bukkit.getServer().getScheduler().runTaskTimer(uhc, () -> {

            Bukkit.getOnlinePlayers().forEach(p -> {
                uhc.getNms().sendTablist(p, ChatColor.RED + "" + ChatColor.BOLD + "Viszla.cc" + ChatColor.RESET + ChatColor.GRAY + " - " + ChatColor.RED + ChatColor.ITALIC + "@ViszlaUHC \n" +
                        ChatColor.GRAY + "Follow our UHC calender on twitter \n" +
                        ChatColor.GRAY + "Ping: " + ChatColor.RED + ((CraftPlayer) p).getHandle().ping + "ms \n", "\n" + ChatColor.RED + "" + ChatColor.BOLD + "Viszla.cc" + ChatColor.RESET + ChatColor.GRAY + " \n " + ChatColor.GRAY + "" +
                        "Host: " + ChatColor.RED + uhc.getGameManager().getHostingName() + "\n" + ChatColor.GRAY + "MatchPost: " + ChatColor.RED + uhc.getGameManager().getMatchPost());

            });

            count--;
            String countDown = uhc.getUtils().convertToNice(count);
            Bukkit.getOnlinePlayers().forEach(p -> {
                HotBarMessage.sendHotBarMessage(p, ChatColor.GRAY + "Whitelist is off for " + ChatColor.DARK_GRAY + "» " + ChatColor.GOLD + countDown);
            });

            if (count == 1) {
                uhc.getGameManager().setWhitelistEnabled(true);
                uhc.getUtils().broadcast(ChatColor.DARK_GRAY + "the whitelist has been " + ChatColor.DARK_RED + "enabled.");
            }
            if (count == -1) {
                stopScheduler();
            }
        }, 0L, 20L).getTaskId();
    }

    public void stopScheduler() {
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }
}
