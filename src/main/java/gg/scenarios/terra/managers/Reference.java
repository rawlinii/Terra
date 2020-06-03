package gg.scenarios.terra.managers;

import gg.scenarios.terra.Terra;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;


@Getter
@Setter
public class Reference {


    private Terra terra;
    private FileConfiguration config;

    public ChatColor primColor = ChatColor.GRAY;
    public ChatColor secColor = ChatColor.GOLD;


    public String arrow = "§8»";

    private String main, pvp, scenario, error, border, team, staff, death;
    private World world = Bukkit.getWorld("uhc");
    public final Location SPAWN = new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ(), -90, 0);
    private String tAccess;
    private String tSecret;


    public Reference(Terra terra) {
        this.terra = terra;
        config = terra.getConfig();
        this.scenario = (getConfig().getString("prefix.scenario"));
        this.main = (getConfig().getString("prefix.main"));
        this.border = (getConfig().getString("prefix.border"));
        this.error = (getConfig().getString("prefix.error"));
        this.staff = (getConfig().getString("prefix.staff"));
        this.team = (getConfig().getString("prefix.team"));
        this.death = (getConfig().getString("prefix.death"));

        this.tAccess = getConfig().getString("twitter.token");
        this.tSecret = getConfig().getString("twitter.secret");


    }
}
