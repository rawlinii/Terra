package gg.scenarios.terra;


import gg.scenarios.terra.commands.ConfigCommand;
import gg.scenarios.terra.commands.SetMatchCommand;
import gg.scenarios.terra.commands.UHCCommand;
import gg.scenarios.terra.commands.WhiteListCommand;
import gg.scenarios.terra.listeners.LobbyListener;
import gg.scenarios.terra.listeners.PlayerListener;
import gg.scenarios.terra.listeners.ScenarioInventoryEvent;
import gg.scenarios.terra.listeners.SpecListener;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.nms.NMS;
import gg.scenarios.terra.nms.verisons.v1_8_R3;
import gg.scenarios.terra.scenarios.ScenarioManager;
import gg.scenarios.terra.teams.Teams;
import gg.scenarios.terra.utils.Utils;
import gg.scenarios.terra.world.BiomeSwap;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.event.Listener;
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

    @Override
    public void onEnable(){
        instance = this;
        saveDefaultConfig();
        saveConfig();
        reloadConfig();
        utils = new Utils(this);
        new BiomeSwap().startWorldGen();
        createWorld();
        reference = new Reference(this);
        gameManager = new GameManager(this);
        teams = new Teams();
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


    }

    private void regListeners() {
        getServer().getPluginManager().registerEvents(new SpecListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new ScenarioInventoryEvent(), this);
        getServer().getPluginManager().registerEvents(new LobbyListener(), this);
    }


    public void createWorld() {
        World uhc = Bukkit.createWorld(new WorldCreator("uhc").environment(World.Environment.NORMAL).type(WorldType.NORMAL));
        uhc.setGameRuleValue("doDaylightCycle", "false");
        uhc.setTime(0);
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
    }
}
