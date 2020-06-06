package gg.scenarios.terra;


import com.google.gson.Gson;
import gg.scenarios.terra.commands.*;
import gg.scenarios.terra.database.Redis;
import gg.scenarios.terra.listeners.LobbyListener;
import gg.scenarios.terra.listeners.PlayerListener;
import gg.scenarios.terra.listeners.ScenarioInventoryEvent;
import gg.scenarios.terra.listeners.SpecListener;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.Gameboard;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.nms.NMS;
import gg.scenarios.terra.nms.verisons.v1_8_R3;
import gg.scenarios.terra.scenarios.ScenarioManager;
import gg.scenarios.terra.managers.teams.Teams;
import gg.scenarios.terra.utils.Utils;
import gg.scenarios.terra.world.BiomeSwap;
import gg.scenarios.terra.world.IncreasedCaneRates;
import gg.scenarios.terra.world.OrePopulator;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import java.util.Arrays;

public class Terra extends JavaPlugin implements Listener {

    @Getter private static Terra instance;

    @Getter
    private Utils utils;
    @Getter
    private Reference reference;
    @Getter
    private GameManager gameManager;
    @Getter
    private NMS nms = new v1_8_R3();
    @Getter
    private Teams teams;
    @Getter
    public ScenarioManager scenarioManager;
    @Getter
    private Gameboard gameboard;

    @Getter
    private Redis redis;

    @Getter
    private Gson gson;
    @Override
    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        saveConfig();
        reloadConfig();
        utils = new Utils(this);
        gson = new Gson();
        new BiomeSwap().startWorldGen();
        createWorld();
        World world = Bukkit.getWorld("uhc");
        utils.loadWorld(world, 1000, 10);
        reference = new Reference(this);
        gameManager = new GameManager(this);
        teams = new Teams();
        redis = new Redis(this);
        scenarioManager = new ScenarioManager();
        for (Team team : teams.getTeams()) {
            for (OfflinePlayer p : team.getPlayers()) {
                team.removePlayer(p);
            }
        }
        teams.setupTeams();
        Bukkit.getScoreboardManager().getMainScoreboard().getObjectives().forEach(objective -> Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective.getName()).unregister());
        setupHeads();
        teams.setupTeams();
        regCommands();
        regListeners();
        utils.setMOTD(ChatColor.translateAlternateColorCodes('&', "       &8&l - / / &4&lScenarios&e&lUHC &7(&a1.8.X)&8&l \\ \\ -                 &7@ScenariosUHC "));
        gameboard = new Gameboard();
        Bukkit.getScheduler().runTaskLater(this, () -> {
            joinable = true;
        }, 20 * 30L);
    }

    private boolean joinable = false;

    @EventHandler
    public void onJoin(PlayerLoginEvent event) {
        if (!joinable) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "UHC SERVER IS STILL BEING SETUP");
        }
    }
    private void regListeners() {
        getServer().getPluginManager().registerEvents(new SpecListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new ScenarioInventoryEvent(), this);
        getServer().getPluginManager().registerEvents(new LobbyListener(), this);
    }


    public void createWorld() {
        World uhc = Bukkit.createWorld(new WorldCreator("uhc").environment(World.Environment.NORMAL).type(WorldType.NORMAL));
        uhc.setGameRuleValue("doDaylightCycle", "true");
        uhc.setGameRuleValue("doMobSpawning", "false");

        uhc.setTime(0);
        uhc.getPopulators().add(new IncreasedCaneRates());
        uhc.getPopulators().add(new OrePopulator());
        uhc.setGameRuleValue("naturalRegeneration", "false");
        uhc.setPVP(false);


        World uhcNether = Bukkit.createWorld(new WorldCreator("uhc_nether").environment(World.Environment.NETHER).type(WorldType.NORMAL));
        uhcNether.setGameRuleValue("doDaylightCycle", "false");
        uhcNether.setTime(0);
        uhcNether.setGameRuleValue("naturalRegeneration", "false");
        uhcNether.setPVP(false);
    }


    public void setupHeads() {
        ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta gMeta = goldenHead.getItemMeta();
        gMeta.setDisplayName(ChatColor.GOLD + "Golden Head");
        gMeta.setLore(Arrays.asList("You've crafted a Golden Head!", "Consuming this will grant you even greater effects", "than a normal Golden Apple!"));
        goldenHead.setItemMeta(gMeta);

        ShapedRecipe goldenHeadRecipe = new ShapedRecipe(goldenHead);
        goldenHeadRecipe.shape(
                "@@@",
                "@#@",
                "@@@");
        goldenHeadRecipe.setIngredient('@', Material.GOLD_INGOT);
        goldenHeadRecipe.setIngredient('#', Material.SKULL_ITEM, (short) 3);
        Bukkit.getServer().addRecipe(goldenHeadRecipe);
    }

    public void regCommands(){
        getCommand("setmatch").setExecutor(new SetMatchCommand());
        getCommand("uhc").setExecutor(new UHCCommand());
        getCommand("whitelist").setExecutor(new WhiteListCommand());
        getCommand("config").setExecutor(new ConfigCommand());
        getCommand("helpop").setExecutor(new HelpopCommand());
        getCommand("rhelpop").setExecutor(new rHelpopCommand());
        getCommand("team").setExecutor(new TeamCommand());
        getCommand("scenarios").setExecutor(new ScenariosCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("coords").setExecutor(new CoordsCommand());
        getCommand("tc").setExecutor(new TeamChatCommand());
        getCommand("backpacks").setExecutor(new BackpacksCommand());
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("tp").setExecutor(new TpCommand(this));
    }
}
