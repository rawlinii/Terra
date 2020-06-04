package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.events.GameStartEvent;
import gg.scenarios.terra.managers.TeamState;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.tasks.GameStartTask;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.redisson.misc.Hash;

import java.util.*;

public class Superheros extends Scenario {

    private Boolean enabled = false;

    private ArrayList<Powers> types = new ArrayList<>();

    private HashMap<UUID, Powers> powersMap = new HashMap<>();

    private Terra terra = Terra.getInstance();

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.MILK_BUCKET) {
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', terra.getReference().getMain() + ChatColor.GOLD +"You cannot drink milk in superheros."));
            event.setCancelled(true);
            event.setItem(new ItemStack(Material.AIR));
        }
    }


    @EventHandler
    public void onGame(GameStartEvent event){
        event.getPlayers().stream().map(UHCPlayer::getUuid).forEach(uuid -> {
            powersMap.put(uuid, types.get(terra.getGameManager().getRandom().nextInt(types.size())));
        });
        createPowers();
    }

    private void createPowers() {
        powersMap.keySet().forEach(uuid -> {
            try{
                Player player = Bukkit.getPlayer(uuid);
                switch (powersMap.get(uuid)){
                    case JUMP:
                        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 3));
                        break;
                    case RESISTANCE:
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
                        break;
                    case STRENGTH:
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));
                        break;
                    case HEALTH:
                        player.setMaxHealth(20+20);
                        player.setHealth(player.getMaxHealth());
                        break;
                    case SPEED:
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                        break;
                    case INVIS:
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2));
                        break;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', terra.getReference().getMain()) + terra.getReference().getPrimColor() +" You have gotten the " + terra.getReference().getSecColor() + powersMap.get(uuid).name().toLowerCase() + terra.getReference().getPrimColor() + " power.");
            }catch (NullPointerException ignored){

            }
        });
    }

    public enum Powers {
        SPEED, STRENGTH, HEALTH, JUMP, INVIS, RESISTANCE;
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Superheros";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.CLAY_BALL).setName((enabled ? ChatColor.GREEN + "Superheros" : ChatColor.RED + "Superheros")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Superheros";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Everybody on a team will receive a superpower that will last for the entire game. ");
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
        return new ItemCreator(Material.CLAY_BALL).setName((enabled ? ChatColor.GREEN + "Superheros" : ChatColor.RED + "Superheros")).setLore(getScenarioExplanation()).get();

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled){
            if (terra.getGameManager().getTeamState() == TeamState.SOLO){
                types.add(Powers.SPEED);
                types.add(Powers.STRENGTH);
                types.add(Powers.HEALTH);
                types.add(Powers.RESISTANCE);
            }else{
                if (terra.getGameManager().getTeamSize() == 5){
                    types.add(Powers.SPEED);
                    types.add(Powers.STRENGTH);
                    types.add(Powers.HEALTH);
                    types.add(Powers.RESISTANCE);
                    types.add(Powers.JUMP);
                }else if (terra.getGameManager().getTeamSize() > 5){
                    types.add(Powers.SPEED);
                    types.add(Powers.STRENGTH);
                    types.add(Powers.HEALTH);
                    types.add(Powers.RESISTANCE);
                    types.add(Powers.JUMP);
                    types.add(Powers.INVIS);
                }else{
                    types.add(Powers.SPEED);
                    types.add(Powers.STRENGTH);
                    types.add(Powers.HEALTH);
                    types.add(Powers.RESISTANCE);
                }
            }
            types.forEach(System.out::println);
        }
        this.enabled = enabled;
    }
}
