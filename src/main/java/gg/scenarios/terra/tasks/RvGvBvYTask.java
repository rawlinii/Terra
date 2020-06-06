package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class RvGvBvYTask {

    List<String> names = Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    private Terra terra = Terra.getInstance();
    private GameManager gameManager = terra.getGameManager();
    private int count = 1, taskId = -1;
    private Queue<String> toBeAssigned = new LinkedList<>();

    public RvGvBvYTask() {
        try {
            names.remove(gameManager.getHost().getPlayer().getName());
        } catch (Exception ignored) {
        }
        Collections.shuffle(names);
        toBeAssigned.addAll(names);
        taskId = Bukkit.getScheduler().runTaskTimer(terra, () -> {
            if (toBeAssigned.isEmpty()) {
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&cR&7v&aG&7v&bB&7v&eY &7Assignments are &6finished");
                stopScheduler();
            } else {
                String player = toBeAssigned.poll();
                addToTeam(player, count);
                count++;
                if (count == 5) {
                    count = 1;
                }
            }
        }, 7L, 0L).getTaskId();
    }

    private void addToTeam(String player, int count) {
        if (count == 1) {
            try {
                Team red = terra.getTeams().getTeamFromColor(ChatColor.RED);
                terra.getTeams().joinTeam(red.getName(), Bukkit.getPlayer(player));
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&c" + player);
            } catch (Exception ignored) {
            }
        } else if (count == 2) {
            try {
                Team green = terra.getTeams().getTeamFromColor(ChatColor.GREEN);
                terra.getTeams().joinTeam(green.getName(), Bukkit.getPlayer(player));
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&a" + player);
            } catch (Exception ignored) {
            }
        }else if (count == 3) {
            try {
                Team aqua = terra.getTeams().getTeamFromColor(ChatColor.AQUA);
                terra.getTeams().joinTeam(aqua.getName(), Bukkit.getPlayer(player));
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&b" + player);
            } catch (Exception ignored) {
            }
        }else if (count == 4) {
            try {
                Team yellow = terra.getTeams().getTeamFromColor(ChatColor.YELLOW);
                terra.getTeams().joinTeam(yellow.getName(), Bukkit.getPlayer(player));
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&e" + player);
            } catch (Exception ignored) {
            }
        }
    }

    public void stopScheduler() {
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }

}
