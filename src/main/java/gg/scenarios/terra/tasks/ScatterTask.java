package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.profiles.PlayerState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.utils.HotBarMessage;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import javax.smartcardio.TerminalFactory;
import java.util.List;
import java.util.stream.Collectors;

public class ScatterTask {


    private Terra main = Terra.getInstance();
    private Utils utils = main.getUtils();
    private GameManager gameManager = main.getGameManager();
    private Reference reference = main.getReference();
    private static int i = 0;
    private List<UHCPlayer> players;
    int taskId;

    public ScatterTask() {
        players = UHCPlayer.getPlayers()
                .values()
                .stream()
                .filter(UHCPlayer::isBeingScattered)
                .filter(UHCPlayer::isOnline)
                .collect(Collectors.toList());

        switch (gameManager.getTeamState()) {
            case SOLO:
                players.forEach((solo) -> {
                    solo.setScatter(gameManager.scatterLocations[i]);
                    solo.setPlayerState(PlayerState.SCATTERED);
                    i++;
                    System.out.println("Scatter task");
                });
                break;
/*            case SLAVEMARKET:
                for (Team team : gameManager.getTeamWait().keySet()) {
                    Location location = gameManager.scatterLocations[i];

                    for (OfflinePlayer player : team.getPlayers()) {
                        Player p = Bukkit.getPlayer(player.getName());
                        if (p != null) {
                            UHCPlayer uhcPlayer = UHCPlayer.getByName(p.getName());
                            uhcPlayer.setPlayerState(PlayerState.SCATTERED);
                            uhcPlayer.setScatter(location);
                        }
                    }
                    i++;
                }
                break;*/
            case TEAM:
                if (i < 150) {
                    for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
                        if (team.getPlayers().isEmpty()) {
                        } else {
                            for (OfflinePlayer offlinePlayer : team.getPlayers()) {
                                Player p = Bukkit.getPlayer(offlinePlayer.getName());
                                if (p != null) {
                                    UHCPlayer player = UHCPlayer.getByName(p.getName());
                                    if (player.isOnline()) {
                                        player.setScatter(gameManager.scatterLocations[i]);
                                        player.setPlayerState(PlayerState.SCATTERED);
                                    }
                                }
                            }
                            i++;
                        }
                    }
                }
                break;
        }
        scatter();
    }
    private void scatter() {
        taskId = Bukkit.getServer().getScheduler().runTaskTimer(main, () -> {
            try {
                if (players.isEmpty()) {
                    System.out.println("stopping");
                    new GameStartTask();
                    stopScheduler();
                    return;
                }
                UHCPlayer profile = players.get(players.size() - 1);
                if (profile.isOnline()) {

                    profile.getScatter().getWorld().getChunkAt(profile.getScatter()).load();
                    profile.getPlayer().teleport(profile.getScatter());
                    main.getNms().addVehicle(profile.getPlayer());
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        HotBarMessage.sendHotBarMessage(player, ChatColor.GRAY + "Scattered " + ChatColor.DARK_GRAY + " » " + ChatColor.GREEN + profile.getPlayer().getName());
                    });
                    players.remove(profile);
                }
                else{
                    players.remove(profile);
                }
            } catch (ArrayIndexOutOfBoundsException e) {

                stopScheduler();
                return;
            }
        }, 0, 3L).getTaskId();
    }




    public void stopScheduler() {
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }
}