package gg.scenarios.terra.managers.profiles;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.utils.Cooldown;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import javax.smartcardio.TerminalFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public class Logger {

    private Terra terra = Terra.getInstance();

    private String name;
    private UUID uuid;

    private Zombie zombie;

    private int experience;

    private ItemStack[] armor;
    private ItemStack[] inventory;

    private Cooldown cooldown;


    public Logger(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();

        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.cooldown = new Cooldown(600_000);

        this.zombie = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        this.zombie.setCustomName(player.getName());
        this.zombie.setCustomNameVisible(true);
        this.zombie.setMaxHealth(player.getMaxHealth());
        this.zombie.setHealth(player.getHealth());
        this.zombie.setBaby(false);
        this.zombie.setVillager(false);
        this.zombie.setRemoveWhenFarAway(false);

        this.armor = player.getInventory().getArmorContents();
        this.inventory = player.getInventory().getContents();
        this.experience = player.getTotalExperience();

        this.zombie.getEquipment().setArmorContents(this.armor);
        this.zombie.getEquipment().setItemInHand(player.getItemInHand());
        this.zombie.setCanPickupItems(false);

        terra.getGameManager().getWhitelistedChunks().add(this.zombie.getLocation().getChunk());
        terra.getNms().removeMovement(this.zombie);

        uhcPlayer.setLogger(this);
    }

    public List<ItemStack> getDrops() {
        List<ItemStack> drops = new ArrayList<>();
        drops.addAll(Arrays.asList(this.inventory));
        drops.addAll(Arrays.asList(this.armor));
        return drops;
    }

    private void drop() {
        World world = this.zombie.getWorld();
        this.getDrops().forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                world.dropItemNaturally(this.zombie.getLocation(), itemStack);
            }
        });
    }

    public void remove() {
        // Get the required info
        GameManager game = terra.getGameManager();
        UHCPlayer uhcPlayer = UHCPlayer.getByName(this.name);
        // Kill it
        uhcPlayer.setPlayerState(PlayerState.DEAD);
        uhcPlayer.setLogger(null);
        if (!this.zombie.isDead()) this.zombie.remove();
        // Displayed it died and handle some things
        // game.getGameManager().format(uhcPlayer, "%1" + this.name  + " §7(Combat Logger) §ewas removed.");
        //game.getGameManager().checkWinners();
        game.getWhitelistedChunks().remove(this.zombie.getLocation().getChunk());
        // Check if player was assigned then end it
       /* if (!uhcPlayer.isAssigned()) return;
        Assignation assignation = uhcPlayer.getAssignation();
        if (assignation.canEnd()) assignation.handleEnd();*/
    }

    public void handleDeath() {
        this.handleDeath(null);
    }

    public void handleDeath(Player killer) {
        try {
       //     terra.getGameboard().resetScore(this.name);
            GameManager game = terra.getGameManager();

            UHCPlayer uhcPlayer = UHCPlayer.getByName(this.name);
            uhcPlayer.getStatistics().getDeaths().increment();
            uhcPlayer.setPlayerState(PlayerState.DEAD);

            String kills = "§7[§f" + uhcPlayer.getStatistics().getKills().getAmount() + "§7]";
            game.getPlayers().remove(uhcPlayer);

            if (killer == null) {
                uhcPlayer.setLogger(null);
                game.getWhitelistedChunks().remove(this.zombie.getLocation().getChunk());

                if (!this.zombie.isDead()) this.zombie.remove();

                // if (!(Gamemode.getByName("Time Bomb").isEnabled() && Gamemode.getByName("Safe Loot").isEnabled())) this.drop();

                terra.getUtils().broadcast(terra.getReference().getDeath() + "&4 " + name + "&8(Logger) was disconnect too long and died!");
                this.drop();
                // game.getGameManager().checkWinners();

            /*if (!uhcPlayer.isAssigned()) return;

            Assignation assignation = uhcPlayer.getAssignation();
            if (assignation.canEnd()) assignation.handleEnd();
            return;*/
            }

            assert killer != null;
            UHCPlayer uhcPlayerTarget = UHCPlayer.getByName(killer.getName());
            uhcPlayerTarget.getStatistics().getKills().increment();
          // uhc.getGameboard().setScore(killer.getName(), uhc.getGameboard().getScore(killer.getName()) + 1);

            terra.getUtils().broadcast(terra.getReference().getDeath() + "&c" + this.name + " §8(Logger) was killed by &4" + killer.getName());
            // game.getGameManager().checkWinners();
            game.getWhitelistedChunks().remove(this.zombie.getLocation().getChunk());
            uhcPlayer.setLogger(null);

            String targetKills = "§7[§f" + uhcPlayerTarget.getStatistics().getKills().getAmount() + "§7]";
            this.drop();
        /*f (!(Gamemode.getByName("Time Bomb").isEnabled() && Gamemode.getByName("Safe Loot").isEnabled())) this.drop();

        game.getGameManager().format(uhcPlayer, uhcPlayerTarget, "%1" + this.name + kills + " §7(Combat Logger) §ewas slain by %2" + uhcPlayerTarget.getName() + targetKills);
        game.getGameManager().checkWinners();
        game.getWhitelistedChunks().remove(this.zombie.getLocation().getChunk());
        uhcPlayer.setLogger(null);

        if (!uhcPlayerTarget.isAssigned()) return;

        Assignation assignation = uhcPlayerTarget.getAssignation();
        if (!assignation.getUhcPlayers().contains(uhcPlayer)) return;
        if (assignation.canEnd()) assignation.handleEnd();*/
        } catch (NullPointerException e) {

        }
    }

}
