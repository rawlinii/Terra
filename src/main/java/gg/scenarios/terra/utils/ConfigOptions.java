package gg.scenarios.terra.utils;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.TeamState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class ConfigOptions {

    private static Terra uhc = Terra.getInstance();
    private static Reference reference = uhc.getReference();
    static ArrayList<ItemStack> items = new ArrayList<>();


    private static String teamSizeToString() {
        if (uhc.getGameManager().getTeamState() == TeamState.SOLO) {
            return "FFA";
        } else {
            return "To" + uhc.getGameManager().getTeamSize();
        }
    }

    public static Inventory config(String name) {
        Inventory inventory = Bukkit.createInventory(null, 54, name);
        String[] editLore =new  String[]{ChatColor.GOLD + "For more info /helpop" };
        ItemStack glass = new ItemCreator(Material.STAINED_GLASS_PANE).setName(" ").get();


        ItemStack toggleables = new ItemCreator(Material.BOAT).setName("§cToggleables ").get();
        ItemStack gameSettings = new ItemCreator(Material.BOOK).setName("§cGame Settings").get();
        ItemStack misc = new ItemCreator(Material.APPLE).setName("§cMisc").get();
        ItemStack d = new ItemCreator(Material.ANVIL).setName("§cMeetup Time " + reference.getArrow() + "§e" + uhc.getGameManager().getMeetupTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack d1 = new ItemCreator(Material.BEACON).setName("§cTeam Size " + reference.getArrow() + "§e" + teamSizeToString()).setLore(Arrays.asList(editLore)).get();
        ItemStack d2 = new ItemCreator(Material.SHEARS).setName("§cApple Rates " + reference.getArrow() + "§e" + uhc.getGameManager().getAppleRates()).setLore(Arrays.asList(editLore)).get();


        ItemStack pvp = new ItemCreator(Material.DIAMOND_SWORD).setName("§cPVP Time " + reference.getArrow() + " §e" + uhc.getGameManager().getPvpTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack border = new ItemCreator(Material.ENDER_PORTAL_FRAME).setName("§cBorder Time " + reference.getArrow() + " §e" + uhc.getGameManager().getBorderTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack heal = new ItemCreator(Material.APPLE).setName("§cHeal Time " + reference.getArrow() + "§e" + uhc.getGameManager().getHealTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack meetup = new ItemCreator(Material.ANVIL).setName("§cMeetup Time " + reference.getArrow() + "§e" + uhc.getGameManager().getMeetupTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack team = new ItemCreator(Material.BEACON).setName("§cTeam Size " + reference.getArrow() + "§e" + teamSizeToString()).setLore(Arrays.asList(editLore)).get();
        ItemStack apple = new ItemCreator(Material.GOLDEN_APPLE).setName("§cApple Rates " + reference.getArrow() + "§e" + uhc.getGameManager().getAppleRates() + "%").setLore(Arrays.asList(editLore)).get();
        ItemStack flint = new ItemCreator(Material.GRAVEL).setName("§cFlint Rates " + reference.getArrow() + " §c " + uhc.getGameManager().getFlintRates() + "%").setLore(Arrays.asList(editLore)).get();
        ItemStack nether = new ItemCreator(Material.OBSIDIAN).setName("§cNether " + reference.getArrow() + " §c " + uhc.getGameManager().isNether()).setLore(Arrays.asList(editLore)).get();
        ItemStack shears = new ItemCreator(Material.SHEARS).setName("§cShears " + reference.getArrow() + " §c " +uhc.getGameManager().isShears()).setLore(Arrays.asList(editLore)).get();

        ItemStack absorption = new ItemCreator(Material.POTION).setName("§cAbsorption " + reference.getArrow() + " §c " + uhc.getGameManager().isAbsorption()).setLore(Arrays.asList(editLore)).get();
        ItemStack notches = new ItemCreator(Material.GOLD_BLOCK).setName("§cNotches " + reference.getArrow() + " §c " +uhc.getGameManager().isNotch()).setLore(Arrays.asList(editLore)).get();


        inventory.setItem(0, toggleables);
        inventory.setItem(1, glass);
        inventory.setItem(2, shears);
        inventory.setItem(3, nether);
        inventory.setItem(4, absorption);
        inventory.setItem(5, notches);
        inventory.setItem(18, misc);
        inventory.setItem(21, apple);
        inventory.setItem(20, flint);
        inventory.setItem(9, gameSettings);
        inventory.setItem(10, glass);
        inventory.setItem(11, pvp);
        inventory.setItem(12, border);
        inventory.setItem(13, heal);
        inventory.setItem(14, meetup);
        inventory.setItem(15, team);
        inventory.setItem(19, glass);
        inventory.setItem(28, glass);
        inventory.setItem(37, glass);
        inventory.setItem(46, glass);

        return inventory;
    }


    public static Inventory config() {
        Inventory inventory = Bukkit.createInventory(null, 54, "Game Editor");

        String[] editLore =new  String[]{"Right Click to subtract one","Left Click to add one" };

        ItemStack glass = new ItemCreator(Material.STAINED_GLASS_PANE).setName(" ").get();


        ItemStack toggleables = new ItemCreator(Material.BOAT).setName("§cToggleables ").get();
        ItemStack gameSettings = new ItemCreator(Material.BOOK).setName("§cGame Settings").get();
        ItemStack misc = new ItemCreator(Material.APPLE).setName("§cMisc").get();
        ItemStack d = new ItemCreator(Material.ANVIL).setName("§cMeetup Time " + reference.getArrow() + "§e" + uhc.getGameManager().getMeetupTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack d1 = new ItemCreator(Material.BEACON).setName("§cTeam Size " + reference.getArrow() + "§e" + teamSizeToString()).setLore(Arrays.asList(editLore)).get();
        ItemStack d2 = new ItemCreator(Material.GOLDEN_APPLE).setName("§cApple Rates" + reference.getArrow() + "§e" + uhc.getGameManager().getAppleRates()).setLore(Arrays.asList(editLore)).get();


        ItemStack pvp = new ItemCreator(Material.DIAMOND_SWORD).setName("§cPVP Time " + reference.getArrow() + " §e" + uhc.getGameManager().getPvpTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack border = new ItemCreator(Material.ENDER_PORTAL_FRAME).setName("§cBorder Time " + reference.getArrow() + " §e" + uhc.getGameManager().getBorderTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack heal = new ItemCreator(Material.APPLE).setName("§cHeal Time " + reference.getArrow() + "§e" + uhc.getGameManager().getHealTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack meetup = new ItemCreator(Material.ANVIL).setName("§cMeetup Time " + reference.getArrow() + "§e" + uhc.getGameManager().getMeetupTime()).setLore(Arrays.asList(editLore)).get();
        ItemStack team = new ItemCreator(Material.BEACON).setName("§cTeam Size " + reference.getArrow() + "§e" + teamSizeToString()).setLore(Arrays.asList(editLore)).get();
        ItemStack apple = new ItemCreator(Material.GOLDEN_APPLE).setName("§cApple Rates " + reference.getArrow() + "§e" + uhc.getGameManager().getAppleRates()+"%").setLore(Arrays.asList(editLore)).get();
        ItemStack flint = new ItemCreator(Material.GRAVEL).setName("§cFlint Rates " + reference.getArrow() + " §c " + uhc.getGameManager().getFlintRates() +"%").setLore(Arrays.asList(editLore)).get();
        ItemStack nether = new ItemCreator(Material.OBSIDIAN).setName("§cNether " + reference.getArrow() + " §c " + uhc.getGameManager().isNether()).setLore(Arrays.asList(editLore)).get();
        ItemStack shears = new ItemCreator(Material.SHEARS).setName("§cShears " + reference.getArrow() + " §c " +uhc.getGameManager().isShears()).setLore(Arrays.asList(editLore)).get();

        ItemStack absorption = new ItemCreator(Material.POTION).setName("§cAbsorption " + reference.getArrow() + " §c " + uhc.getGameManager().isAbsorption()).setLore(Arrays.asList(editLore)).get();
        ItemStack notches = new ItemCreator(Material.GOLD_BLOCK).setName("§cNotches " + reference.getArrow() + " §c " +uhc.getGameManager().isNotch()).setLore(Arrays.asList(editLore)).get();


        inventory.setItem(0, toggleables);
        inventory.setItem(1, glass);
        inventory.setItem(2, shears);
        inventory.setItem(3, nether);
        inventory.setItem(4, absorption);
        inventory.setItem(5, notches);
        inventory.setItem(18, misc);
        inventory.setItem(21, apple);
        inventory.setItem(20, flint);
        inventory.setItem(9, gameSettings);
        inventory.setItem(10, glass);
        inventory.setItem(11, pvp);
        inventory.setItem(12, border);
        inventory.setItem(13, heal);
        inventory.setItem(14, meetup);
        inventory.setItem(15, team);
        inventory.setItem(19, glass);
        inventory.setItem(28, glass);
        inventory.setItem(37, glass);
        inventory.setItem(46, glass);

        return inventory;
    }
}