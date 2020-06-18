package gg.scenarios.terra.managers.profiles;
import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.profiles.stats.Statistics;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.utils.Utils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class UHCPlayer {


    private Terra main = Terra.getInstance();
    private Utils utils = main.getUtils();
    private GameManager gameManager = main.getGameManager();
    private Reference reference = main.getReference();


    private UUID uuid = null;
    private Logger logger = null;

    @Getter
    static Map<UUID, UHCPlayer> players = new HashMap<>();
    @Getter
    static Map<String, UHCPlayer> playersNames = new HashMap<>();

    private PlayerState playerState = PlayerState.LOBBY;
    private Location scatter = null;
    private boolean host = false;

    private ItemStack[] armor;
    private ItemStack[] inventory;
    private int levels = 0;
    private Location deathLocation = null;


    private UUID combatSkeletonUUID;

    private boolean xrayAlerts = true;
    private boolean pvpAlerts = true;
    private boolean helpOpAlerts = true;
    private boolean pveAlerts = true;
    private Statistics statistics = new Statistics();

    private boolean teamChat = true;

    public PlayerState getPlayerState() {
        return playerState;
    }

    public boolean isSpectating() {
        return playerState != PlayerState.SCATTERING && playerState != PlayerState.INGAME;
    }

    public boolean isPlaying() {
        return playerState != PlayerState.HOST &&
                playerState != PlayerState.MODERATOR &&
                playerState != PlayerState.SPECTATOR &&
                playerState != PlayerState.LOBBY;
    }

    public boolean isInLobby() {
        return playerState == PlayerState.LOBBY;
    }

    public boolean isBeingScattered() {
        return playerState == PlayerState.SCATTERING || playerState == PlayerState.SOLD;
    }

    public boolean isScattered() {
        return playerState == PlayerState.SCATTERED;
    }

    public boolean isAlive() {
        return playerState == PlayerState.INGAME;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean isOnline() {
        return Bukkit.getPlayer(uuid) != null;
    }

    public static UHCPlayer getByName(String name) {
        return playersNames.get(name);
    }

    public static UHCPlayer getByUUID(UUID uuid) {
        return players.get(uuid);
    }

 /*   public void addToDatabase(){
        UHCPlayer profile = this;
        Document document = new org.bson.Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("name", profile.getPlayer().getName());
                document.put("wins", profile.getStatistics().getWins().getAmount());
                document.put("loses", profile.getStatistics().getGames().getAmount());
                document.put("kdr", profile.getStatistics().getKdr().getAmount());
                document.put("deaths", profile.getStatistics().getDeaths().getAmount());
                document.put("kills", profile.getStatistics().getKills().getAmount());
                document.put("ip", profile.getStatistics().getLapisMined().getAmount());
                Thorn.getInstance().getProfiles().insertOne(document);
    }

    public void statsSaveByName(String name) {
        UUID uuid = Bukkit.getPlayer(name).getUniqueId();
        UHCPlayer profile = UHCPlayer.getByName(name);
        terra.getProfiles().updateMany(
                Filters.eq("uuid", uuid.toString()),
                Updates.combine(
                        Updates.set("name", profile.getPlayer().getName()),
                        Updates.set("wins", profile.getStatistics().getWins().getAmount()),
                        Updates.set("loses", profile.getStatistics().getGames().getAmount()),
                        Updates.set("kdr", profile.getStatistics().getKdr().getAmount()),
                        Updates.set("deaths", profile.getStatistics().getDeaths().getAmount()),
                        Updates.set("kills", profile.getStatistics().getKills().getAmount()),
                        Updates.set("ip", profile.getStatistics().getLapisMined().getAmount())));
    }
    */
    public UHCPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        playersNames.put(name, this);
        players.put(uuid, this);
        main.getGameManager().getPlayers().add(this);
    }



   /* public static void updateAll(){
        players.forEach((s, profile) ->
                Thorn.getInstance().getProfiles().updateMany(
                        Filters.eq("uuid", s.toString()),
                        Updates.combine(
                                Updates.set("name", profile.getPlayer().getName()),
                                Updates.set("wins", profile.getStatistics().getWins().getAmount()),
                                Updates.set("loses", profile.getStatistics().getGames().getAmount()),
                                Updates.set("kdr", profile.getStatistics().getKdr().getAmount()),
                                Updates.set("deaths", profile.getStatistics().getDeaths().getAmount()),
                                Updates.set("kills", profile.getStatistics().getKills().getAmount()),
                                Updates.set("ip", profile.getStatistics().getLapisMined().getAmount()))));
    }
*/

    @Override
    public String toString() {
        return "UHCPlayer{" +
                "uuid=" + uuid +
                ", logger=" + logger +
                ", playerState=" + playerState +
                ", scatter=" + scatter +
                ", host=" + host +
                ", armor=" + Arrays.toString(armor) +
                ", inventory=" + Arrays.toString(inventory) +
                ", levels=" + levels +
                ", deathLocation=" + deathLocation +
                ", combatSkeletonUUID=" + combatSkeletonUUID +
                ", xrayAlerts=" + xrayAlerts +
                '}';
    }
}
