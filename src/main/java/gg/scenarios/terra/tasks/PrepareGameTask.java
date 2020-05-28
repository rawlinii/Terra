package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.profiles.PlayerState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.utils.HotBarMessage;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PrepareGameTask {
    private Terra uhc = Terra.getInstance();
    private Utils utils = uhc.getUtils();
    private GameManager gameManager = uhc.getGameManager();
    private Reference reference = uhc.getReference();

    public PrepareGameTask() {
        assert Bukkit.getWorld("uhc") != null;
        final World world = Bukkit.getWorld("uhc");
        world.setTime(0L);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setDifficulty(Difficulty.EASY);

        UHCPlayer.getPlayers()
                .values()
                .stream()
                .filter(UHCPlayer::isOnline)
                .filter(UHCPlayer::isInLobby)
                .forEach(uhcPlayer -> {
                    uhcPlayer.setPlayerState(PlayerState.SCATTERING);
                });

        for (int i = 0; i < Bukkit.getOnlinePlayers().size() + 10; i++) {
            Location location = uhc.getGameManager().findLocation();
            uhc.getGameManager().scatterLocations[i] = location;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                HotBarMessage.sendHotBarMessage(onlinePlayer, ChatColor.AQUA + "Loading Scatter Points " + ChatColor.GRAY + "[" + ChatColor.RED + i + ChatColor.GRAY + "/" + ChatColor.GRAY + Bukkit.getOnlinePlayers().size() + 10 + ChatColor.GRAY + "]");
            }
        }

        double seconds = gameManager.getPlayers().size() / 4;
        gameManager.setScatterTimeLeft((long) seconds);

        gameManager.setGameState(GameState.SCATTERING);


        new ScatterTask();
        new BukkitRunnable() {
            @Override
            public void run() {
                  new GameStartTask();
            }
        }.runTaskLater(uhc, gameManager.getScatterTimeLeft() + 10L);
    }
}
