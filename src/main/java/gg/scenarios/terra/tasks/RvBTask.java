package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.io.StringBufferInputStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class RvBTask {

    List<String> names = Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    private Terra terra = Terra.getInstance();
    private GameManager gameManager = terra.getGameManager();
    private int count = 1, taskId = -1;
    private Queue<String> toBeAssigned = new LinkedList<>();

    public RvBTask() {
        try {
            names.remove(gameManager.getHost().getPlayer().getName());
        } catch (Exception ignored) {
        }
        Collections.shuffle(names);
        toBeAssigned.addAll(names);
        taskId = Bukkit.getScheduler().runTaskTimer(terra, () -> {
            if (toBeAssigned.isEmpty()) {
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&cR&7v&bB &7Assignments are &6finished");
                stopScheduler();
            } else {
                String player = toBeAssigned.poll();
                addToTeam(player, count);
                count++;
                if (count == 3) {
                    count = 1;
                }
            }
        }, 7L, 0L).getTaskId();
    }

    private void addToTeam(String player, int count) {
        if (count % 2 == 0) {
            try {
                Team red = terra.getTeams().getTeamFromColor(ChatColor.RED);
                terra.getTeams().joinTeam(red.getName(), Bukkit.getPlayer(player));
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&c" + player);
            } catch (Exception ignored) {
            }
        } else {
            try {
                Team aqua = terra.getTeams().getTeamFromColor(ChatColor.AQUA);
                terra.getTeams().joinTeam(aqua.getName(), Bukkit.getPlayer(player));
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&b" + player);
            } catch (Exception ignored) {
            }
        }
    }

    public void stopScheduler() {
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }

}
