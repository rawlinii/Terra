package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.Game;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.managers.twitter.PostTweet;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.HotBarMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import twitter4j.TwitterException;

import java.util.ArrayList;
import java.util.List;

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

            if (count == 900){
                try {
                    PostTweet tweet = new PostTweet(15);
                    uhc.getUtils().broadcast(uhc.getReference().getMain() + ChatColor.DARK_GRAY + "Posted to twitter");
                    uhc.getUtils().broadcast(tweet.getLinkUrl());


                    Game game = new Game(uhc.getGameManager().getHostingName(), uhc.getGameManager().getMatchPost(),teamSizeToString(), (uhc.getGameManager().getMatch().getMatchTime().getHour() + ":" + uhc.getGameManager().getMatch().getMatchTime().getMinute()), "na.scenarios.gg",
                            getScenarios());
                    uhc.getRedis().getClient().getTopic("gameServer").publishAsync(uhc.getGson().toJson(game));
                } catch (Exception e) {
                    e.printStackTrace();
                    uhc.getUtils().broadcast(uhc.getReference().getError() + ChatColor.DARK_GRAY + "Error posting to twitter, contact RJ asap.");
                }
            }

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

    private List<String> getScenarios() {

        List<String> scenarios = new ArrayList<>();
        uhc.getScenarioManager().getEnabledScenarios().stream().map(Scenario::getName).forEach(scenarios::add);
        return scenarios;
    }

    public void stopScheduler(){
        Bukkit.getServer().getScheduler().cancelTask(this.taskId);
    }
    private String teamSizeToString() {
        if (uhc.getGameManager().getTeamState() == TeamState.SOLO) {
            return "FFA";
        }else if (uhc.getGameManager().getTeamState() == TeamState.SLAVEMARKET){
            return "Slave Market";
        }else {
            return "To" + uhc.getGameManager().getTeamSize();
        }
    }
}
