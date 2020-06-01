package gg.scenarios.terra.managers;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.profiles.PlayerState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.logging.Level;

@Getter
@Setter
public class GameManager {

    private Terra terra;

    private String matchPost = null;
    private Random random = new Random();
    private Matchpost match;
    private List<Chunk> whitelistedChunks = new ArrayList<>();
    public Location[] scatterLocations = new Location[150];

    private UHCPlayer host = null;
    private String hostingName;
    private boolean startingFall = true;
    private GameState gameState = GameState.LOBBY;
    private TeamState teamState = TeamState.SOLO;
    private int teamSize = 1;
    private List<UHCPlayer> players = new ArrayList<>();
    public List<UHCPlayer> slaves = new ArrayList<>();
    private List<UUID> whitelist = new ArrayList<>();
    private List<UUID> mods = new ArrayList<>();
    public List<UUID> hosts = new ArrayList<>();
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
    private int scatterTimeLeft = 60;

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

    public Location shrink(Player player, int radius) {
        Location loc = player.getLocation();
        //900 750
        int x = (int) loc.getX(), z = (int) loc.getZ();
        if (loc.getX() > radius) {
            int diff = (int) loc.getX() - radius;
            x = (int) loc.getX() - diff - 4;
        } else if (loc.getX() < -radius) {
            int diff = (int) loc.getX() + radius;
            //  / x = (int) loc.getX() + diff + 4;
            x = (int) loc.getX() + (-diff) + 4;
        }
        if (loc.getZ() > radius) {
            int diff = (int) loc.getZ() - radius;
            z = (int) loc.getZ() - diff - 4;
        } else if (loc.getZ() < -radius) {
            int diff = (int) loc.getZ() + radius;
            z = (int) loc.getZ() + (-diff) + 4;
            // -1000 +-249
        }

        return Bukkit.getWorld("uhc").getHighestBlockAt(x, z).getLocation();
    }

    private int times = 0;

    public Location getLocation() {
        World uhc = Bukkit.getWorld("uhc");
        int z = random.nextInt(getBorderRadius() + 1 - (getBorderRadius() * -1)) + (getBorderRadius() * -1);
        int x = random.nextInt(getBorderRadius() + 1 - (getBorderRadius() * -1)) + (getBorderRadius() * -1);
        Location location = new Location(uhc, x, uhc.getHighestBlockYAt(x, z), z);
        if (location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.WATER &&
                location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.STATIONARY_WATER &&
                location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.LAVA &&
                location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.STATIONARY_LAVA &&
                location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.CACTUS) {
            times = 0;
            return location.add(0, 5, 0);
        }
        if (times == 10) {
            location.getBlock().setType(Material.GRASS);
            Bukkit.getLogger().log(Level.INFO, "Failed to get a location after 10 attempts, this location will look fake");
            times = 0;
            return location.add(0, 5, 0);
        }
        times++;
        return getLocation();
    }

    private HashMap<Team, Location> teamRandomScatterLoc = new HashMap<>();

    public void updateBorder(int radius) {
        World world = Bukkit.getWorld("uhc");

        if (radius == 500 || radius == 250 || radius == 100 || radius == 50 || radius == 750) {
            borderRadius = radius;

            if (teamState == TeamState.SOLO) {
                for (Player p : Bukkit.getWorld("uhc").getPlayers()) {
                    UHCPlayer uhcPlayer = UHCPlayer.getByUUID(p.getUniqueId());
                    if (uhcPlayer.getPlayerState() == PlayerState.INGAME) {
                        Location location = p.getLocation();
                        if (Math.abs(location.getX()) > radius || Math.abs(location.getZ()) > radius) {
                            if (radius == 750) {
                                shrink(p, 750);
                                p.teleport(shrink(p, 750));

                            } else {
                                Location location1 = getLocation();
                                p.teleport(world.getHighestBlockAt(location1.getBlockX(), location1.getBlockZ()).getLocation());

                            }
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 1);

                        }
                    }
                }
            } else {
                for (Player p : Bukkit.getWorld("uhc").getPlayers()) {
                    UHCPlayer uhcPlayer = UHCPlayer.getByUUID(p.getUniqueId());
                    if (uhcPlayer.getPlayerState() == PlayerState.INGAME) {
                        Location location = p.getLocation();
                        if (Math.abs(location.getX()) > radius || Math.abs(location.getZ()) > radius) {
                            p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 1);

                            if (teamRandomScatterLoc.containsKey(terra.getTeams().getTeam(p))) {
                                Location location1 = teamRandomScatterLoc.get(terra.getTeams().getTeam(p));
                                p.teleport(world.getHighestBlockAt(location1.getBlockX(), location1.getBlockZ()).getLocation());

                            } else {
                                if (radius == 750) {
                                    shrink(p, 750);
                                    p.teleport(shrink(p, 750));
                                } else {
                                    Location location1 = getLocation();
                                    p.teleport(world.getHighestBlockAt(location1.getBlockX(), location1.getBlockZ()).getLocation());
                                    p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10, 1);

                                    teamRandomScatterLoc.put(terra.getTeams().getTeam(p), location1);
                                }

                            }
                        }

                    }
                }
            }
            world.getWorldBorder().setSize(borderRadius * 2, 2);

        }
        teamRandomScatterLoc.clear();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "worldborder set " + radius * radius);
        if (radius == 500) {
            if (isNether()) {
                if (Bukkit.getWorld("uhc_nether") != null) {
                    if (Bukkit.getWorld("uhc_nether").getPlayers().size() != 0) {
                        for (Player p : Bukkit.getWorld("uhc_nether").getPlayers()) {
                            Location location1 = getLocation();
                            p.teleport(world.getHighestBlockAt(location1.getBlockX(), location1.getBlockZ()).getLocation());
                        }
                    }
                }
            }
        }
    }

}
