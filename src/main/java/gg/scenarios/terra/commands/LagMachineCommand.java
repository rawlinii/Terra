package gg.scenarios.terra.commands;


import net.minecraft.server.v1_8_R3.ChatClickable;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LagMachineCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "In game only");
        } else {
            Player player = (Player) sender;
            if (player.hasPermission("uhc.senior")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.RED + "Please use /lagmachine <1-50>");
                } else {
                    try {
                        int radius = Integer.parseInt(args[0]);
                        if (radius <= 50) {
                            player.sendMessage(ChatColor.GREEN+"Started lagmachine fix");
                            ArrayList<Block> blocks = getBlocks(player.getLocation().getBlock(), radius);
                            for (Block block : blocks) {
                                if (block.getType() == Material.STATIONARY_WATER || block.getType() == Material.WATER  ||block.getType() == Material.LAVA  ||block.getType() == Material.STATIONARY_LAVA  ||block.getType() == Material.REDSTONE_WIRE){
                                    block.setType(Material.AIR);
                                }
                            }
                            player.sendMessage(ChatColor.GREEN + "Removed all water, lava, and red stone in a " + args[0] +" radius");
                        }else{
                            player.sendMessage(ChatColor.RED+"Please enter a number less then or equal to 50");
                        }

                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Please enter a valid number");
                    }
                }
            }
        }
        return false;
    }


    private ArrayList<Block> getBlocks(Block start, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for (double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++) {
            for (double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++) {
                for (double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++) {
                    Location loc = new Location(start.getWorld(), x, y, z);
                    blocks.add(loc.getBlock());
                }
            }
        }
        return blocks;
    }
}