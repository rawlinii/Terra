package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.managers.Reference;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.utils.GuiBuilder;
import gg.scenarios.terra.utils.ItemCreator;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class AlertsCommand implements CommandExecutor {

    private Terra uhc = Terra.getInstance();
    private Utils utils = uhc.getUtils();
    private GameManager gameManager = uhc.getGameManager();
    private Reference reference = uhc.getReference();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            UHCPlayer uhcPlayer = UHCPlayer.getByName(sender.getName());
            Player player = (Player) sender;

            GuiBuilder guiBuilder = new GuiBuilder();
            guiBuilder.name("&6Alerts");
            guiBuilder.rows(1);
            guiBuilder.item(0, new ItemCreator(Material.DIAMOND_SWORD).setName("&cPvP Alerts").setLore(Collections.singletonList("&7Currently: &6" + (uhcPlayer.isPvpAlerts() ? "Enabled" : "Disabled"))).get()).onClick(event -> {
                if (uhcPlayer.isPvpAlerts())
                    uhcPlayer.setPvpAlerts(false);
                else
                    uhcPlayer.setPvpAlerts(true);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.5f, 2);
                event.getInventory().setItem(event.getSlot(), new ItemCreator(Material.DIAMOND_SWORD).setName("&cPvP Alerts").setLore(Collections.singletonList("&7Currently: &6" + (uhcPlayer.isPvpAlerts() ? "Enabled" : "Disabled"))).get());
                event.setCancelled(true);
            });
            guiBuilder.item(1, new ItemCreator(Material.DIAMOND).setName("&bXray Alerts").setLore(Collections.singletonList("&7Currently: &6" + (uhcPlayer.isXrayAlerts() ? "Enabled" : "Disabled"))).get()).onClick(event -> {
                if (uhcPlayer.isXrayAlerts())
                    uhcPlayer.setXrayAlerts(false);
                else
                    uhcPlayer.setXrayAlerts(true);

                player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.5f, 2);
                event.getInventory().setItem(event.getSlot(), new ItemCreator(Material.DIAMOND).setName("&bXray Alerts").setLore(Collections.singletonList("&7Currently: &6" + (uhcPlayer.isXrayAlerts() ? "Enabled" : "Disabled"))).get());
                event.setCancelled(true);
            });
            guiBuilder.item(2, new ItemCreator(Material.APPLE).setName("&6Helpop Alerts").setLore(Collections.singletonList("&7Currently: &6" + (uhcPlayer.isHelpOpAlerts() ? "Enabled" : "Disabled"))).get()).onClick(event -> {
                if (uhcPlayer.isHelpOpAlerts())
                    uhcPlayer.setHelpOpAlerts(false);
                else
                    uhcPlayer.setHelpOpAlerts(true);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.5f, 2);
                event.getInventory().setItem(event.getSlot(), new ItemCreator(Material.APPLE).setName("&6Helpop Alerts").setLore(Collections.singletonList("&7Currently: &6" + (uhcPlayer.isHelpOpAlerts() ? "Enabled" : "Disabled"))).get());
                event.setCancelled(true);
                    }
            );

            guiBuilder.item(3, new ItemCreator(Material.DIAMOND_BOOTS).setName("&4PvE Alerts").setLore(Collections.singletonList("&7Currently: &6" + (uhcPlayer.isPveAlerts() ? "Enabled" : "Disabled"))).get()).onClick(event -> {
                if (uhcPlayer.isPveAlerts())
                    uhcPlayer.setPveAlerts(false);
                else
                    uhcPlayer.setPveAlerts(true);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.5f, 2);
                event.getInventory().setItem(event.getSlot(), new ItemCreator(Material.DIAMOND_BOOTS).setName("&4PvE Alerts").setLore(Collections.singletonList("&7Currently: &6" + (uhcPlayer.isPveAlerts() ? "Enabled" : "Disabled"))).get());
                event.setCancelled(true);
            });

            player.openInventory(guiBuilder.make());
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reference.getMain() + "&7Only players can use this command!"));
        }
        return false;
    }
}
