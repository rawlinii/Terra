package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.utils.HotBarMessage;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.Bukkit;

public class GameOverTask {


    private Terra main = Terra.getInstance();
    private Utils utils = main.getUtils();
    private GameManager gameManager = main.getGameManager();
    private Reference reference = main.getReference();

    private int taskId;

    public GameOverTask() {
        taskId = Bukkit.getScheduler().runTaskTimer(main, () -> {
            String endTime = utils.convertToNice((gameManager.getEndTime() * 60) - gameManager.getGameOverTimer());

            UHCPlayer.getPlayers().values().stream().filter(UHCPlayer::isOnline).forEach(e -> {
                try {
                    HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor + "Game Ends » " + reference.secColor + "{0}".replace("{0}", endTime));
                }catch (Exception ex){

                }
            });

            gameManager.setGameOverTimer(gameManager.getGameOverTimer()+ 1);
        }, 0L, 20L).getTaskId();
    }

    public void stopScheduler(){
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }
}
