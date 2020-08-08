package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.events.KingAddEvent;
import gg.scenarios.terra.events.KingRemoveEvent;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ChatUtil;
import gg.scenarios.terra.utils.ItemCreator;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Kings extends Scenario {

    public static List<UUID> kings;


    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if (event.getEntity().getKiller() != null) {
            Player player = (Player) event.getEntity();
            if (kings.contains(player.getUniqueId())){
                Team team = Terra.getInstance().getTeams().getTeam(player);

                for (OfflinePlayer teammate : team.getPlayers()) {
                    Player online = teammate.getPlayer();
                    if (online == null) {
                        continue;
                    }
                    online.sendMessage(ChatUtil.format("&cYour king died! You are now getting Weakness and Poison!"));
                    online.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 3600, 0));
                    online.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 0));

                }
            }
        }
    }

    @EventHandler
    public void onKingAdd(KingAddEvent event){
        Player player = Bukkit.getPlayer(event.getUuid());
        if (player != null){
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1));
            player.setMaxHealth(20+20);
            player.setHealth(player.getMaxHealth());

        }
    }

    @EventHandler
    public void onKingRemove(KingRemoveEvent event){
        Player player = Bukkit.getPlayer(event.getUuid());
        if (player != null){
            for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
                player.removePotionEffect(activePotionEffect.getType());
            }
            player.setMaxHealth(20);
            player.setHealth(player.getMaxHealth());

        }
    }

    private boolean enabled = false;

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Kings";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.GOLD_HELMET).setName((enabled ? ChatColor.GREEN + "Kings" : ChatColor.RED + "Kings")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Kings";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - 1 person will be King, the King will have Strength, Resistance, Haste, and 20 hearts.");
        explain.add(ChatColor.BLUE + " - if the king dies, the remaining teammates will receive weakness for 3 mins and Poison for 15 seconds.");
        return explain;
    }

    @Override
    public String getState() {
        if (enabled) {
            return ChatColor.GREEN + "Enabled";
        } else {
            return ChatColor.RED + "Disabled";
        }
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemCreator(Material.GOLD_HELMET).setName((enabled ? ChatColor.GREEN + "Kings" : ChatColor.RED + "Kings")).setLore(getScenarioExplanation()).get();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled)
            kings = new ArrayList<>();
        this.enabled = enabled;
    }
}
