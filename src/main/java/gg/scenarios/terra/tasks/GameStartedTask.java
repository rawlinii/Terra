package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.events.LoggerExpireTimeEvent;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.profiles.Logger;
import gg.scenarios.terra.managers.profiles.PlayerState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.HotBarMessage;
import gg.scenarios.terra.utils.Utils;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameStartedTask {


    private Terra main = Terra.getInstance();
    private Utils utils = main.getUtils();
    private GameManager gameManager = main.getGameManager();
    private Reference reference = main.getReference();
    private int taskId;
    private World world = Bukkit.getWorld("uhc");

    private String createScenarios(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Scenario scenario : main.getScenarioManager().getScenarios()){
            if (scenario.isEnabled()) {
                stringBuilder.append(scenario.getName());
                stringBuilder.append(", ");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        return stringBuilder.toString();
    }

    public GameStartedTask() {
        taskId =Bukkit.getScheduler().runTaskTimer(main, () -> {

            if(gameManager.getHealTimeInSeconds() == gameManager.getTimer()) {
                utils.broadcast(ChatColor.translateAlternateColorCodes('&', main.getReference().getMain()) + reference.primColor + "Final heal has been" + reference.secColor + " given" + reference.primColor + "!");
                UHCPlayer.getPlayers().values().stream().filter(UHCPlayer::isPlaying).filter(UHCPlayer::isOnline).forEach( (p) -> {
                    p.getPlayer().setHealth(p.getPlayer().getMaxHealth());
                    p.getPlayer().setSaturation(20.00f);
                });
            }

            Bukkit.getOnlinePlayers().forEach(p ->{
                main.getNms().sendTablist(p, ChatColor.RED + "" + ChatColor.BOLD + "Cupid.gg" + ChatColor.RESET + ChatColor.GRAY + " - " + ChatColor.RED + ChatColor.ITALIC +"@CupidGameFeed \n" +
                        ChatColor.GRAY + "Follow our UHC calender on twitter \n" +
                        ChatColor.GRAY + "Ping: " + ChatColor.RED + ((CraftPlayer) p).getHandle().ping + "ms \n", "\n"+ChatColor.RED + "" + ChatColor.BOLD + "Cupid.gg" + ChatColor.RESET + ChatColor.GRAY + " \n " + ChatColor.GRAY + "Scenarios: " + ChatColor.RED +
                        createScenarios());
            });

            if(gameManager.getPvpTimeInSeconds() == gameManager.getTimer()) {
                utils.broadcast(ChatColor.translateAlternateColorCodes('&', main.getReference().getMain()) + reference.primColor + "PVP has been" + reference.secColor + " enabled" + reference.primColor + "!");
                gameManager.setPVP(true);
            }

            if (gameManager.getBorderTimeInSeconds() == gameManager.getTimer()) {

                int difference = gameManager.getBorderTimeInSeconds() - gameManager.getTimer();
                if (difference <4 && difference >=1){
                    utils.broadcast(ChatColor.translateAlternateColorCodes('&',reference.getMain()) +  reference.primColor+"Border will shrink to" +reference.secColor + "750" + reference.primColor+"in " + reference.secColor + difference + reference.primColor+  " seconds.");
                }
                World world = Bukkit.getWorld("uhc");
                world.setGameRuleValue("doDaylightCycle", "false");
                world.setTime(0);
                //world.getWorldBorder().setSize(200, (gameManager.getMeetupTime() - gameManager.getBorderTime())*60);
                gameManager.updateBorder(750);
            }

            if (gameManager.getBorderTimeInSeconds()+300 == gameManager.getTimer()) {
                int difference = gameManager.getBorderTimeInSeconds()+300 - gameManager.getTimer();
                if (difference <4 && difference >=1){
                    utils.broadcast(ChatColor.translateAlternateColorCodes('&',reference.getMain()) +  reference.primColor+"Border will shrink to" +reference.secColor + "500" + reference.primColor+"in " + reference.secColor + difference + reference.primColor+  " seconds.");
                }
                World world = Bukkit.getWorld("uhc");
                //world.getWorldBorder().setSize(200, (gameManager.getMeetupTime() - gameManager.getBorderTime())*60);
                gameManager.updateBorder(500);
            }
            if (gameManager.getBorderTimeInSeconds()+600 == gameManager.getTimer()) {
                int difference = gameManager.getBorderTimeInSeconds()+600 - gameManager.getTimer();
                if (difference <4 && difference >=1){
                    utils.broadcast(ChatColor.translateAlternateColorCodes('&',reference.getMain()) +  reference.primColor+"Border will shrink to" +reference.secColor + "100" + reference.primColor+"in " + reference.secColor + difference + reference.primColor+  " seconds.");
                }
                World world = Bukkit.getWorld("uhc");
                //world.getWorldBorder().setSize(200, (gameManager.getMeetupTime() - gameManager.getBorderTime())*60);
                gameManager.updateBorder(100);
            }
            if (gameManager.getBorderTimeInSeconds()+900 == gameManager.getTimer()) {
                int difference = gameManager.getBorderTimeInSeconds()+900 - gameManager.getTimer();
                if (difference <4 && difference >=1){
                    utils.broadcast(ChatColor.translateAlternateColorCodes('&',reference.getMain()) +  reference.primColor+"Border will shrink to" +reference.secColor + "50" + reference.primColor+"in " + reference.secColor + difference + reference.primColor+  " seconds.");
                }
                World world = Bukkit.getWorld("uhc");
                //world.getWorldBorder().setSize(200, (gameManager.getMeetupTime() - gameManager.getBorderTime())*60);
                gameManager.updateBorder(50);
            }

            if (5 == gameManager.getTimer()){
                UHCPlayer.getPlayers().values().stream().filter(UHCPlayer::isPlaying).filter(UHCPlayer::isOnline).forEach( (p) -> {
                    p.getPlayer().setHealth(p.getPlayer().getMaxHealth());
                    p.getPlayer().setSaturation(20.00f);
                });
                gameManager.setStartingFall(false);
                world.setGameRuleValue("doMobSpawning", "true");
                world.setDifficulty(Difficulty.EASY);
            }

            String healTime = utils.convertToNice(gameManager.getHealTimeInSeconds() - gameManager.getTimer());
            String pvpTime = utils.convertToNice(gameManager.getPvpTimeInSeconds() - gameManager.getTimer());
            String borderTime = utils.convertToNice(gameManager.getBorderTimeInSeconds() - gameManager.getTimer());
            String meetuptime = utils.convertToNice(gameManager.getMeetupTimeInSeconds() - gameManager.getTimer());

            int heal = (gameManager.getHealTimeInSeconds() - gameManager.getTimer());
            int pvp = (gameManager.getPvpTimeInSeconds() - gameManager.getTimer());
            int border = (gameManager.getBorderTimeInSeconds() - gameManager.getTimer());
            int border1 = (gameManager.getBorderTimeInSeconds()+300 - gameManager.getTimer());
            int border2 = (gameManager.getBorderTimeInSeconds()+600 - gameManager.getTimer());
            int border3 = (gameManager.getBorderTimeInSeconds()+900 - gameManager.getTimer());

            if(heal < 6 && heal>= 1) {
                utils.broadcast(ChatColor.translateAlternateColorCodes('&', main.getReference().getMain()) +  reference.primColor+"Final heal is in " + reference.secColor + heal + reference.primColor+  " seconds.");
                UHCPlayer.getPlayers().values().forEach(uhcPlayer -> {
                    if (uhcPlayer.getPlayerState().equals(PlayerState.INGAME)) {
                        if (uhcPlayer.isOnline() && uhcPlayer.getLogger() == null) {
                            uhcPlayer.getPlayer().setHealth(uhcPlayer.getPlayer().getMaxHealth());
                        } else {
                            Logger logger = uhcPlayer.getLogger();
                            if (!logger.getZombie().isDead())
                                logger.getZombie().setHealth(logger.getZombie().getMaxHealth());
                        }
                    }
                });
            }

            UHCPlayer.getPlayers().values().forEach(uhcPlayer -> {
                if (uhcPlayer.getLogger() != null) {
                    Logger logger = uhcPlayer.getLogger();
                    if (logger.getCooldown().hasExpired()) {
                        Bukkit.getPluginManager().callEvent(new LoggerExpireTimeEvent(uhcPlayer, logger.getDrops()));
                        logger.handleDeath();
                    }
                }
            });

            if(pvp < 6 && pvp >= 1) {
                utils.broadcast(ChatColor.translateAlternateColorCodes('&', reference.getMain()) +  reference.primColor+"PvP is enabled in " + reference.secColor + pvp + reference.primColor+  " seconds.");
            }


            if (border < 11 &&border >= 1){
                utils.broadcast(ChatColor.translateAlternateColorCodes('&',reference.getMain()) +  reference.primColor+"Border will shrink in " + reference.secColor + border + reference.primColor+  " seconds.");
            }
            if (border1 < 11 &&border1 >= 1){
                utils.broadcast(ChatColor.translateAlternateColorCodes('&',reference.getMain()) +  reference.primColor+"Border will shrink in " + reference.secColor + border1 + reference.primColor+  " seconds.");
            }
            if (border2 < 11 &&border2 >= 1){
                utils.broadcast(ChatColor.translateAlternateColorCodes('&',reference.getMain()) +  reference.primColor+"Border will shrink in " + reference.secColor + border2 + reference.primColor+  " seconds.");
            }
            if (border3 < 11 &&border3 >= 1){
                utils.broadcast(ChatColor.translateAlternateColorCodes('&',reference.getMain()) +  reference.primColor+"Border will shrink in " + reference.secColor + border3 + reference.primColor+  " seconds.");
            }

            UHCPlayer.getPlayers().values().stream().filter(UHCPlayer::isOnline).forEach(e ->{
                try {

                    if (gameManager.getTimer() < gameManager.getHealTimeInSeconds()) {

                        HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor+ "Heal » " + reference.secColor+"{0}"
                                .replace("{1}", pvpTime).replace("{0}", healTime));
                    }else if (gameManager.getTimer() < gameManager.getPvpTimeInSeconds()){
                        HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor + "Pvp » " + reference.secColor+"{0}"
                                .replace("{0}", pvpTime).replace("{1}", borderTime));
                    }else if (gameManager.getTimer() < gameManager.getBorderTimeInSeconds()){
                        HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor + "Border » " +  reference.secColor+"{0}"
                                .replace("{0}", (String.valueOf(gameManager.getBorderRadius()))) + reference.primColor+ " Next Shrink »" + reference.secColor + "{1}"
                                .replace("{1}", utils.convertToNice(gameManager.getBorderTimeInSeconds() - gameManager.getTimer())));
                    }else if (gameManager.getTimer() < gameManager.getBorderTimeInSeconds()+300){

                        HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor + "Border » " +  reference.secColor+"{0}"
                                .replace("{0}", (String.valueOf(gameManager.getBorderRadius()))) + reference.primColor+ " Next Shrink »" + reference.secColor + "{1}"
                                .replace("{1}", utils.convertToNice(gameManager.getBorderTimeInSeconds()+300 - gameManager.getTimer())));
                    }else if (gameManager.getTimer() < gameManager.getBorderTimeInSeconds()+600){

                        HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor + "Border » " +  reference.secColor+"{0}"
                                .replace("{0}", (String.valueOf(gameManager.getBorderRadius()))) + reference.primColor+ " Next Shrink »" + reference.secColor + "{1}"
                                .replace("{1}", utils.convertToNice(gameManager.getBorderTimeInSeconds()+600- gameManager.getTimer())));
                    }else if (gameManager.getTimer() < gameManager.getBorderTimeInSeconds()+900){

                        HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor + "Meetup is now | Border » " +  reference.secColor+"{0}"
                                .replace("{0}", (String.valueOf(gameManager.getBorderRadius()))) + reference.primColor);

                    }else{
                        HotBarMessage.sendHotBarMessage(e.getPlayer(), reference.primColor + "Meetup has started " +  reference.secColor+ (main.getScenarioManager().getScenarioByName("SkyHigh").isEnabled() ? " " : "No Skybasing or Mining!"));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            if (Utils.checkWinners()){
                Bukkit.broadcastMessage(ChatColor.GREEN + "Congratulations to " + main.getGameManager().getPlayers().get(Terra.getInstance().getGameManager().getPlayers().size() -1).getPlayer().getName());
                Bukkit.broadcastMessage(ChatColor.GREEN + "Congratulations to " + main.getGameManager().getPlayers().get(Terra.getInstance().getGameManager().getPlayers().size() -1).getPlayer().getName());
                Bukkit.broadcastMessage(ChatColor.GREEN + "Congratulations to " + main.getGameManager().getPlayers().get(Terra.getInstance().getGameManager().getPlayers().size() -1).getPlayer().getName());
                new GameOverTask();
                stopScheduler();
            }

            gameManager.setTimer(gameManager.getTimer()+1);

        }, 0L, 20L).getTaskId();
    }
    public void stopScheduler(){
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }

}