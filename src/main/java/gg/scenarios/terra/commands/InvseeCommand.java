package gg.scenarios.terra.commands;

import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class InvseeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("essentials.invsee")) {
                if (args.length == 1) {
                    try {
                        Player target = Bukkit.getPlayer(args[0]);
                        openInv(player, target);
                    } catch (NullPointerException e) {
                        player.sendMessage("Player not found");
                    }
                } else if (args.length == 0) {
                    player.sendMessage("§c/invsee <PLayer>");
                } else {
                    player.sendMessage("§c/invsee <PLayer>");
                }
            } else {
                player.sendMessage("no perm");
            }
        } else {
            sender.sendMessage("no perm");
        }
        return true;
    }

    public void openInv(Player player, final Player target) {
        final Inventory inv = Bukkit.getServer().createInventory(target, 45, "Player Inventory");
        final Damageable dmg = target;

        ArrayList<String> lore2 = new ArrayList<String>();
        lore2.add("§cThis is a placeholder, the player is naked");
        ItemStack armorPlaceHolder = new ItemCreator(Material.STAINED_GLASS_PANE).setName(ChatColor.RED + "Armor Place Holder").get();

        inv.setItem(0, target.getInventory().getHelmet());
        inv.setItem(1, target.getInventory().getChestplate());
        inv.setItem(2, target.getInventory().getLeggings());
        inv.setItem(3, target.getInventory().getBoots());

        ItemStack n = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = n.getItemMeta();
        infoMeta.setDisplayName("§cPlayer information");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§aName: §7" + target.getName());
        lore.add(" ");
        lore.add("§aHearts: §7" + (((int) dmg.getHealth()) / 2));
        lore.add("§aHunger: §7" + (target.getFoodLevel() / 2));
        lore.add("§aXp level: §7" + target.getLevel());
        lore.add("§aLocation: §7" + target.getWorld().getEnvironment().name().replaceAll("_", "").toLowerCase().replaceAll("normal", "overworld") + ", x:" + target.getLocation().getBlockX() + ", y:" + target.getLocation().getBlockY() + ", z:" + target.getLocation().getBlockZ());
        lore.add(" ");

        ItemStack info = new ItemCreator(Material.SKULL_ITEM).setOwner(target.getName()).setName(ChatColor.GREEN + "Player Information").setLore(lore).get();
        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);
        inv.setItem(8, info);

        int i = 9;
        for (ItemStack item : target.getInventory().getContents()) {
            inv.setItem(i, item);
            i++;
        }
        player.openInventory(inv);

    }

}
