package gg.scenarios.terra.managers;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.tasks.WhitelistOff;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

import javax.swing.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class Matchpost {

    private String[] matchPost;
    private List<Object> scenarios;
    private Terra terra = Terra.getInstance();
    ZonedDateTime matchTime;

    public Matchpost(String matchPost) {
        terra.getGameManager().setMatchPost(matchPost);
        terra.getGameManager().setMatch(this);
        this.matchPost = matchPost.split("/");
        this.scenarios = new ArrayList<>();
        terra.getServer().broadcastMessage(ChatColor.GREEN + "Loading settings from matchpost, server might lag");

        HttpResponse<JsonNode> response = Unirest.get("https://hosts.uhc.gg/api/matches/" + this.matchPost[4])
                .header("accept", "application/json")
                .asJson();


        int pvpEnabledAt = response.getBody().getObject().getInt("pvpEnabledAt");
        terra.getGameManager().setPvpTime(pvpEnabledAt);

        if (response.getBody().getObject().getString("teams").equalsIgnoreCase("chosen")){
            terra.getGameManager().setTeamState(TeamState.TEAM);
            terra.getGameManager().setTeamSize(response.getBody().getObject().getInt("size"));
        }

        terra.getGameManager().setBorderTime(response.getBody().getObject().getInt("length") - 15);

        scenarios =  response.getBody().getObject().getJSONArray("scenarios").toList();

        String dateTime = response.getBody().getObject().getString("opens");
        ZonedDateTime serverTime = ZonedDateTime.now(ZoneId.of("UTC"));
         matchTime = ZonedDateTime.of(LocalDateTime.parse(dateTime
                .replace("Z", "").trim()), ZoneId.of("UTC"));

        Long diff = ChronoUnit.SECONDS.between(serverTime, matchTime);
        new WhitelistOff(diff.intValue());

        scenarios.forEach(obj -> {
            try {
                terra.scenarioManager.getScenarioByName(obj.toString()).enable();
                }catch (Exception e){
                terra.getServer().broadcastMessage(terra.getReference().getSecColor() + obj.toString() + " "+ terra.getReference().getPrimColor() + "Could not enable, please enable from /uhc scenarios");
            }
        });

        for (Team team : Terra.getInstance().getTeams().getTeams()) {
            for (OfflinePlayer p : team.getPlayers()) {
                team.removePlayer(p);
            }
        }
        Terra.getInstance().getTeams().setupTeams();
        terra.getServer().broadcastMessage(ChatColor.GREEN + "All settings loaded from matchpost");

    }
}
