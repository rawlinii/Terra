package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class RandomTeamsTask {

    List<String> names = Bukkit.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    private Terra terra = Terra.getInstance();
    private GameManager gameManager = terra.getGameManager();
    private int count = 0, taskId = -1, teamCount = 0;
    private Queue<String> toBeAssigned = new LinkedList<>();
    private boolean started = false;
    private Team team;

    public RandomTeamsTask() {
        try {
            names.remove(gameManager.getHost().getPlayer().getName());
        } catch (Exception ignored) {
        }
        Collections.shuffle(names);
        toBeAssigned.addAll(names);
        taskId = Bukkit.getScheduler().runTaskTimer(terra, () -> {
            if (toBeAssigned.isEmpty()) {
                terra.getUtils().broadcast(terra.getReference().getTeam() + "&7Random Team Assignments are &6finished");
                stopScheduler();
            } else {
                if (started) {
                    terra.getUtils().broadcast(terra.getReference().getTeam() + "&7Team &6" + teamCount + "&7 has been generated");
                    started = false;
                }
                team = terra.getTeams().getTeams().get(teamCount);
                String player = toBeAssigned.poll();

                System.out.println(player);
                try {
                    addToTeam(team, Bukkit.getPlayer(player));
                }catch (NullPointerException npe){
                    npe.printStackTrace();
                }
                count++;
                if (count == gameManager.getTeamSize()) {
                    count = 0;
                    teamCount++;
                    team = terra.getTeams().getTeams().get(teamCount);
                    terra.getUtils().broadcast(terra.getReference().getTeam() + "&7Team &6" + teamCount + "&7 has been generated");

                }
            }
        }, 0L, 20L).getTaskId();
    }

    private void addToTeam(Team team, Player player) {
        terra.getTeams().joinTeam(team.getName(), player);
        terra.getUtils().broadcast(terra.getReference().getTeam() + "&7- " + team.getPrefix() + player.getName());
    }

    public void stopScheduler() {
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }
}
