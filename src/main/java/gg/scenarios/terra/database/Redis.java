package gg.scenarios.terra.database;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameState;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.UUID;

@Getter
public class Redis {

    @Getter private final RedissonClient client = Redisson.create();

    private Terra terra;


    public Redis (Terra terra) {
        this.terra = terra;


        client.getTopic("whitelist").addListener((s, o) -> {
            if (terra.getGameManager().getMatchPost() != null){
                if (terra.getGameManager().getGameState() == GameState.LOBBY){
                    terra.getUtils().broadcast(terra.getReference().getMain() + "&6" + o + " &7has whitelisted themselves in #prewhitelists");
                    terra.getGameManager().getWhitelist().add(UUID.fromString(insertDashUUID(getUUID((String) o))));
                }
            }
        });
    }

    public static String insertDashUUID(String uuid) {
        StringBuilder sb = new StringBuilder(uuid);
        sb.insert(8, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(13, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(18, "-");
        sb = new StringBuilder(sb.toString());
        sb.insert(23, "-");

        return sb.toString();
    }

    public static String getUUID(String playerName) {
        try {
            HttpResponse<JsonNode> response = Unirest.get("https://api.mojang.com/users/profiles/minecraft/" + playerName)
                    .header("accept", "application/json")
                    .asJson();

            return response.getBody().getObject().getString("id");
        } catch (Exception e) {
            return "";
        }
    }
}
