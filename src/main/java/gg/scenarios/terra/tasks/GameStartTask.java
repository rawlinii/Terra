package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.events.GameStartEvent;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.GameState;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.profiles.PlayerState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.utils.HotBarMessage;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GameStartTask {
    private int taskId;

    private Terra main = Terra.getInstance();
    private Utils utils = main.getUtils();
    private GameManager gameManager = main.getGameManager();
    private Reference reference = main.getReference();

    private boolean s = true;
    public GameStartTask() {
        taskId = Bukkit.getScheduler().runTaskTimer(main, () -> {
/*            int i = 0;
            List<String> localRules = reference.getRules();
            if(localRules.size() != 0) {
                utils.broadcast(ChatColor.translateAlternateColorCodes('&',localRules.get(i)));
                localRules.remove(i);
                i++;
            }*/
            if (s) {
                UHCPlayer.getPlayers().values().stream().filter(UHCPlayer::isPlaying).filter(UHCPlayer::isOnline).forEach(uhcPlayer -> {
                    main.getNms().addVehicle(uhcPlayer.getPlayer());
                });
                s=false;
            }
            UHCPlayer.getPlayers().values().stream().filter(UHCPlayer::isOnline).forEach(e ->{
                try {
                    HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor + "Game starts »" + reference.secColor+" {0}".replace("{0}", Terra.getInstance().getUtils().convertToNice((int) gameManager.getScatterTimeLeft())));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });


            if(gameManager.getScatterTimeLeft() < 6 && gameManager.getScatterTimeLeft() >= 1) {
                if (gameManager.getScatterTimeLeft() == 5){
                    for (Player player : Bukkit.getOnlinePlayers()){
                        player.getWorld().getChunkAt(player.getLocation()).load();
                    }
                }
                utils.broadcast(ChatColor.translateAlternateColorCodes('&', main.getReference().getMain()) + reference.primColor + "The game is starting in " + reference.secColor + gameManager.getScatterTimeLeft() + reference.primColor+ "." .replace("%main", reference.getMain().replace("%second", Integer.toString((int) gameManager.getScatterTimeLeft()))));
            }
            if (gameManager.getScatterTimeLeft() == 1){
                 main.getGameboard().setScore("§a§lPvE", 1);
                 main.getGameboard().setScore("§a§lPvE", 0);
            }

            if(gameManager.getScatterTimeLeft() == 0) {
                utils.broadcast(ChatColor.translateAlternateColorCodes('&', main.getReference().getMain()) + reference.primColor + "The " + reference.secColor + "UHC" + reference.primColor + " has begun " + reference.secColor +".");
                UHCPlayer.getPlayers().values().stream().filter(UHCPlayer::isOnline).filter(UHCPlayer::isScattered).forEach(e->{
                    UHCPlayer.getByName(e.getPlayer().getName()).setPlayerState(PlayerState.INGAME);
                    main.getNms().removeVehicle(e.getPlayer());
                    utils.giveItem(e.getPlayer(), new ItemStack(Material.COOKED_BEEF, 10));
                    e.getPlayer().setFoodLevel(20);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 10, 1);
                });
                new GameStartedTask();
                gameManager.setGameState(GameState.STARTED);
//                Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(gameManager.getGameState()));
                Bukkit.getPluginManager().callEvent(new GameStartEvent((ArrayList<UHCPlayer>) gameManager.getPlayers()));

                stopScheduler();
            }

            gameManager.setScatterTimeLeft(gameManager.getScatterTimeLeft() -1);



        }, 0L, 20L).getTaskId();
    }

    public void stopScheduler(){
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }

}