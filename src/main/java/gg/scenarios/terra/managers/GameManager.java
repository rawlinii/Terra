package gg.scenarios.terra.managers;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;

import java.util.*;

@Getter
@Setter
public class GameManager {

    private Terra terra;

    private String matchPost = null;
    private Random random = new Random();

    private List<Chunk> whitelistedChunks = new ArrayList<>();
    public Location[] scatterLocations = new Location[150];

    private UHCPlayer host = null;
    private boolean startingFall = true;
    private GameState gameState = GameState.LOBBY;
    private TeamState teamState = TeamState.SOLO;
    private int teamSize = 1;
    private List<UHCPlayer> players = new ArrayList<>();
    public List<UHCPlayer> slaves = new ArrayList<>();
    private List<UUID> whitelist = new ArrayList<>();
    private List<UUID> mods = new ArrayList<>();
    private List<UUID> hosts = new ArrayList<>();
    private boolean pvp = false;
    private boolean donatorWhitelistEnabled = true;
    private boolean whitelistEnabled = true;
    private int maxPlayers = 250;
    private boolean nether = false;
    private int timer = 0;
    private int healTime = 5;
    private int endTime = 5;
    private int pvpTime = 15;
    private int meetupTime = 60;
    private int borderTime = 45;
    private int borderRadius = 1000;
    private int flintRates = 100;
    private double appleRates = 2.50;
    private boolean absorption = true;
    private boolean notch = false;
    private boolean shears = true;
    private int whitelistOffTime = 0;
    private long scatterTimeLeft;
    public GameManager(Terra terra) {
        this.terra = terra;

    }

    public Location findLocation() {
        World world = (terra.getReference().getWorld());
        int randomX = random.nextInt(borderRadius);
        int randomZ = random.nextInt(borderRadius);
        int randomXX = random.nextBoolean() ? randomX : -randomX;
        int randomZZ = random.nextBoolean() ? randomZ : -randomZ;
        Location location = new Location(Bukkit.getWorld(world.getName()), randomXX, world.getHighestBlockYAt(randomXX, randomZZ), randomZZ);
        Material block = location.getBlock().getType();
        if (block == Material.LAVA ||
                block == Material.WATER ||
                block == Material.STATIONARY_LAVA ||
                block == Material.STATIONARY_WATER ||
                block == Material.WATER_LILY) {
            return findLocation();
        }
        return location;
    }

    public void setUpScatterLocations() {
        for (int i = 0; i < scatterLocations.length; i++) {
            scatterLocations[i] = findLocation();
        }
    }

    public void setPVP(boolean PVP) {
        this.pvp = PVP;
        World world = Bukkit.getWorld("uhc");
        world.setPVP(true);
    }


    public int getPvpTimeInSeconds() {
        return pvpTime * 60;
    }

    public int getBorderTimeInSeconds() {
        return borderTime * 60;
    }

    public int getHealTimeInSeconds() {
        return healTime * 60;
    }

    public int getMeetupTimeInSeconds() {
        return meetupTime * 60;
    }

    @Override
    public String toString() {
        return "GameManager{" +
                "terra=" + terra +
                ", matchPost='" + matchPost + '\'' +
                ", random=" + random +
                ", whitelistedChunks=" + whitelistedChunks +
                ", scatterLocations=" + Arrays.toString(scatterLocations) +
                ", host=" + host +
                ", startingFall=" + startingFall +
                ", gameState=" + gameState +
                ", teamState=" + teamState +
                ", teamSize=" + teamSize +
                ", players=" + players +
                ", slaves=" + slaves +
                ", whitelist=" + whitelist +
                ", mods=" + mods +
                ", hosts=" + hosts +
                ", pvp=" + pvp +
                ", donatorWhitelistEnabled=" + donatorWhitelistEnabled +
                ", whitelistEnabled=" + whitelistEnabled +
                ", maxPlayers=" + maxPlayers +
                ", nether=" + nether +
                ", timer=" + timer +
                ", healTime=" + healTime +
                ", endTime=" + endTime +
                ", pvpTime=" + pvpTime +
                ", meetupTime=" + meetupTime +
                ", borderTime=" + borderTime +
                ", borderRadius=" + borderRadius +
                ", flintRates=" + flintRates +
                ", appleRates=" + appleRates +
                ", absorption=" + absorption +
                ", notch=" + notch +
                ", shears=" + shears +
                '}';
    }

    public void setWhitelistOffTime(int whitelistOffTime) {
        this.whitelistOffTime = whitelistOffTime;
    }

    public int getWhitelistOffTime() {
        return whitelistOffTime;
    }

    public long getScatterTimeLeft() {
        return scatterTimeLeft;
    }

    public void setScatterTimeLeft(long scatterTimeLeft) {
        this.scatterTimeLeft = (int) scatterTimeLeft;
    }
}
