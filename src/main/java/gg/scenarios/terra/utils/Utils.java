package gg.scenarios.terra.utils;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.tasks.PregenTask;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.DedicatedServer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Consumer;

public class Utils {


    private Terra terra;

    public Utils(Terra terra) {
        this.terra = terra;
    }

    public void broadcast(String msg) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    @Getter
    private NumberFormat nf = new DecimalFormat("##.##");


    public void setMOTD(String motd) {

        DedicatedServer server = (((CraftServer) Bukkit.getServer()).getHandle().getServer());

        server.setMotd(motd);
    }


    public void getOfflinePlayer(String name, Consumer<OfflinePlayer> callback) {
        for (OfflinePlayer loop : Bukkit.getOfflinePlayers()) {
            if (loop.getName().equalsIgnoreCase(name)) {
                if (Bukkit.isPrimaryThread()) {
                    callback.accept(loop);
                } else {
                    Bukkit.getScheduler().runTask(terra, () -> callback.accept(loop));
                }
                return;
            }
        }
        Runnable run = () -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(name);
            Bukkit.getScheduler().runTask(terra, () -> callback.accept(player));
        };
        if (Bukkit.isPrimaryThread()) {
            new Thread(run).start();
        } else {
            run.run();
        }
    }



    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {

        List<Location> circleBlocks = new ArrayList<Location>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);

                        circleBlocks.add(l);

                    }

                }
            }
        }

        return circleBlocks;
    }



//    public void giveItem(Player player, ItemStack... stacks){
//        Validate.notNull(player, "Player cannot be null.");
//        Validate.notNull(stacks, "ItemStack cannot be null.");
//
//        HashMap<Integer, ItemStack> leftOvers = player.getInventory().addItem(stacks);
//
//        if (leftOvers.isEmpty()) {
//            return;
//        }
//        player.sendMessage(ChatColor.translateAlternateColorCodes('&', terra.getReference().getMain() + "&6Item(s) were dropped on the ground!"));
//
//        for (ItemStack leftOver : leftOvers.values()){
//            player.getWorld().dropItemNaturally(player.getLocation(), leftOver);
//        }
//
//    }
    public static void giveItem(Player player, ItemStack... stacks){
        Validate.notNull(player, "Player cannot be null.");
        Validate.notNull(stacks, "ItemStack cannot be null.");

        HashMap<Integer, ItemStack> leftOvers = player.getInventory().addItem(stacks);

        if (leftOvers.isEmpty()) {
            return;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Terra.getInstance().getReference().getMain() + "&6Item(s) were dropped on the ground!"));

        for (ItemStack leftOver : leftOvers.values()){
            player.getWorld().dropItemNaturally(player.getLocation(), leftOver);
        }

    }

    public void prepareSpawn() {
        final int[] i = {0};
        final Queue<Location> locationQueue = new ArrayDeque<>();
        final Queue<Material> materialQueue = new ArrayDeque<>();
        final World world = Bukkit.getWorld("uhc");
        final Location max = new Location(world, 125, 160, 125);
        final Location min = new Location(world, -125, 50, -125);
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    final Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();
                    if (block.getType() == Material.GRASS) {
                        locationQueue.add(block.getLocation());
                        materialQueue.add(Material.STAINED_CLAY);
                    } else if (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2
                            || block.getType() == Material.VINE || block.getType() == Material.SNOW || block.getType() == Material.DOUBLE_PLANT
                            || block.getType() == Material.YELLOW_FLOWER || block.getType() == Material.RED_MUSHROOM || block.getType() == Material.BROWN_MUSHROOM) {
                        locationQueue.add(block.getLocation());
                        materialQueue.add(Material.AIR);
                    }
                }
            }
        }
        final int blocks = locationQueue.size();
        new BukkitRunnable() {

            @Override
            public void run() {
                for (int x = 0; x < 150; x++) {
                    if (!locationQueue.isEmpty()) {
                        Location location = locationQueue.poll();
                        Material material = materialQueue.poll();
                        if (material == Material.STAINED_CLAY) {
                            int rand = (int) ((Math.random() * 2) + 1);
                            int data;
                            if (rand == 1) {
                                data = 1;
                            } else {
                                data = 4;
                            }
                            location.getBlock().setType(Material.STAINED_CLAY);
                            location.getBlock().setData((byte) data);
                        } else {
                            location.getBlock().setType(Material.AIR);
                        }
                    } else {
                        this.cancel();
                        if (x == 149) {
                            Bukkit.broadcastMessage("§fFinished clearing§7 " + world.getName() + "§f by changing§7 " + blocks + " blocks");
                        }
                    }
                }
            }
        }.runTaskTimer(terra, 0L, 1L);
    }

    public void loadWorld(final World world, final int radius, final int speed) {
        final String name = world.getName();
        Bukkit.broadcastMessage("§bStarted clearing the world " + world.getName());
        prepareSpawn();
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.broadcastMessage("§bStarted loading the world " + world.getName());
                new PregenTask();
            }
        }.runTaskLater(terra, 15 * 20L);
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(1000 * 2);
    }

    public void unloadWorld(World world) {
        if (world != null) {
            for (Player p : world.getPlayers()) {
                p.teleport(Bukkit.getWorld("world").getSpawnLocation());
            }
            Bukkit.broadcastMessage("§fThe world§7 " + world.getName() + "§f has begun§7 unloading");
            Bukkit.broadcastMessage("§fWill begin deleting in§7 30 seconds");
            Bukkit.getServer().unloadWorld(world, false);
        }
    }

    public void deleteWorld(String world) {
        File filePath = new File("/home/container", world);
        deleteFiles(filePath);
        Bukkit.broadcastMessage("§fThe world§7 " + world + "§f has begun§7 deleting");
        Bukkit.broadcastMessage("§fWill finish deleting in§7 30 seconds");
        Bukkit.getScheduler().runTaskLater(terra, () -> {
            Bukkit.broadcastMessage(ChatColor.GREEN + "World has been deleted");
        }, 20 * 30);
    }

    public static void broadcastToTeam(Team team, String message) {
        for (OfflinePlayer teammate : team.getPlayers()) {
            Player online = teammate.getPlayer();

            if (online == null) {
                continue;
            }

            online.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public boolean deleteFiles(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFiles(file);
                } else {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        }
        return (path.delete());
    }

    public static String getUUID(String playerName) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + playerName;

            String UUIDJson = IOUtils.toString(new URL(url));

            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);

            return UUIDObject.get("id").toString();
        } catch (Exception e) {
            return "";
        }
    }

    public void kickPlayer(Player player) {
        if (player.hasPermission("uhc.stay.ondeath")) {
            player.sendMessage(ChatColor.RED + "Thanks for playing, you are able to stay in the lobby meanwhile");

        } else {
            player.sendMessage(ChatColor.RED + "Thanks for playing, You have 30 seconds before you are kicked");

            Bukkit.getScheduler().runTaskLater(terra, () -> {
                player.kickPlayer("Thanks for playing \n Join our discord at https://discord.gg/qvbZFKa");

            }, 20 * 30);
        }
    }


    public String scoreboard(int sec) {
        int second = sec % 60;
        int minute = sec / 60;
        if (minute >= 60) {
            int hour = minute / 60;
            minute %= 60;
            return hour + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
        }
        if (sec >= 60) {
            return minute + ":" + (second < 10 ? "0" + second : second) + "";
        }
        return (second < 10 ? "" + second : second) + "";
    }

    public String convertToNice(int sec) {
        int second = sec % 60;
        int minute = sec / 60;
        if (minute >= 60) {
            int hour = minute / 60;
            minute %= 60;
            return hour + "h" + (minute < 10 ? "0" + minute : minute) + "m" + (second < 10 ? "0" + second : second);
        }
        if (sec >= 60) {
            return minute + "m" + (second < 10 ? "" + second : second) + "s";
        }
        return (second < 10 ? "" + second : second) + "s";
    }


    public void hideAll(Player player) {
        for (UUID uuid : terra.getGameManager().getMods()) {
            if (Bukkit.getPlayer(uuid) != null) {
                player.hidePlayer(Bukkit.getPlayer(uuid));
            }
        }
    }


    public void set(Player player, boolean type) {
        if (!type) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', terra.getReference().getStaff() + "You are no longer spectating."));
            player.setGameMode(GameMode.SURVIVAL);
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setFlySpeed((float) 0.1);

            if (terra.getTeams().getTeam(player) != null) {
                terra.getTeams().leaveTeam(player);
            }
            try {
                terra.getGameManager().getMods().remove(player.getUniqueId());
            }catch (NullPointerException e){
            }
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.getInventory().remove(Material.COMPASS);
            player.getInventory().remove(Material.INK_SACK);
            player.getInventory().remove(Material.FEATHER);
            player.getInventory().remove(Material.BOOK);


            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!terra.getGameManager().getMods().contains(online.getUniqueId())) {
                    player.showPlayer(online);
                } else {
                    player.hidePlayer(online);
                }
                online.showPlayer(player);
                hideAll(online);
            }
        }

        if (type) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', terra.getReference().getStaff() + "You are now spectating, Don't spoil ANYTHING."));

            player.getInventory().remove(Material.COMPASS);
            player.getInventory().remove(Material.INK_SACK);
            player.getInventory().remove(Material.FEATHER);
            player.getInventory().remove(Material.BOOK);

            ItemStack[] contents = player.getInventory().getContents();
            for (ItemStack content : contents) {
                if (content != null) {
                    player.getWorld().dropItemNaturally(player.getLocation(), content);
                    player.getInventory().remove(content);
                }
            }

            ItemStack[] armorContents = player.getInventory().getArmorContents();
            for (ItemStack armorContent : armorContents) {
                if (armorContent.getAmount() != 0) {
                    player.getWorld().dropItemNaturally(player.getLocation(), armorContent);
                    player.getInventory().setArmorContents(null);
                }
            }

            player.setAllowFlight(true);
            player.setFlying(true);
            player.setFlySpeed((float) 0.1);
            terra.getGameManager().getMods().add(player.getUniqueId());
            terra.getTeams().joinTeam("spec", player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000000, 0));
            ItemStack compass = new ItemStack(Material.COMPASS);
            ItemMeta compassMeta = compass.getItemMeta();
            compassMeta.setDisplayName(ChatColor.GREEN + "Teleporter");
            compassMeta.setLore(Arrays.asList(ChatColor.GRAY + "Left click to teleport to a random player.", ChatColor.GRAY + "Right click to open a player teleporter."));
            compass.setItemMeta(compassMeta);
            ItemStack night = new ItemStack(Material.INK_SACK, 1, (short) 12);
            ItemMeta nightMeta = night.getItemMeta();
            nightMeta.setDisplayName(ChatColor.GREEN + "Toggle Night Vision");
            nightMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right click to toggle the night vision effect."));
            night.setItemMeta(nightMeta);
            ItemStack tp = new ItemStack(Material.FEATHER);
            ItemMeta tpMeta = tp.getItemMeta();
            tpMeta.setDisplayName(ChatColor.GREEN + "Teleport to 0,0");
            tpMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right click to teleport to 0,0."));
            tp.setItemMeta(tpMeta);
            ItemStack invsee = new ItemStack(Material.BOOK);
            ItemMeta invseeMeta = tp.getItemMeta();
            invseeMeta.setDisplayName(ChatColor.GREEN + "Inventory Viewer");
            invseeMeta.setLore(Arrays.asList(ChatColor.GRAY + "Right click on a player to view their inventory"));
            invsee.setItemMeta(invseeMeta);

            player.getInventory().setItem(0, tp);
            player.getInventory().setItem(1, compass);
            player.getInventory().setItem(2, night);
            player.getInventory().setItem(8, invsee);


            for (Player online : Bukkit.getOnlinePlayers()) {
                if (!terra.getGameManager().getMods().contains(online.getUniqueId())) {
                    online.hidePlayer(player);
                }
                player.showPlayer(online);
            }
        }
    }



    public void toggle(Player player) {
        if (terra.getGameManager().getMods().contains(player.getUniqueId())) {
            this.set(player, false);
        } else {
            this.set(player, true);
        }
    }

}
