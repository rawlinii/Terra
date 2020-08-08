package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.events.GameStartEvent;
import gg.scenarios.terra.managers.profiles.UHCPlayer;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomStarterItems extends Scenario {

    ItemStack[] items = new ItemStack[9];
    private boolean enabled = false;

    List<String> materialNames = Stream.of(Material.values()).map(Material::name).collect(Collectors.toList());


    @EventHandler
    public void onGameStart(GameStartEvent event) {
        for (int i = 0; i < items.length; i++) {
            ItemStack itemStack = null;
            itemStack = new ItemStack(Material.getMaterial(materialNames.get(main.getGameManager().getRandom().nextInt(materialNames.size() - 1))), main.getGameManager().getRandom().nextInt(63 + 1));
            items[i] = itemStack;
        }
        event.getPlayers().stream().filter(UHCPlayer::isOnline).forEach(uhcPlayer -> {
            Player player = uhcPlayer.getPlayer();
            for (ItemStack item : items) {
                player.getInventory().addItem(item);
            }
        });
    }

    @Override
    public boolean isCompatibleWith (Class < ? extends Scenario > clazz){
        return true;
    }

    @Override
    public String getDefaultName () {
        return "Random Starter Items";
    }

    @Override
    public ItemStack getAdminItemStack () {
        return new ItemCreator(Material.BEDROCK).setName((enabled ? ChatColor.GREEN + "Random Starter Items" : ChatColor.RED + "Random Starter Items")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName () {
        return "Random Starter Items";
    }

    @Override
    public List<String> getScenarioExplanation () {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Every player will receive 9 random items");
        explain.add(ChatColor.BLUE + " - Every player will same items");

        return explain;
    }

    @Override
    public String getState () {
        if (enabled) {
            return ChatColor.GREEN + "Enabled";
        } else {
            return ChatColor.RED + "Disabled";
        }
    }

    @Override
    public ItemStack getItemStack () {
        return new ItemCreator(Material.BEDROCK).setName((enabled ? ChatColor.GREEN + "Random Starter Items" : ChatColor.RED + "Random Starter Items")).setLore(getScenarioExplanation()).get();
    }

    @Override
    public boolean isEnabled () {
        return enabled;
    }

    @Override
    public void setEnabled ( boolean enabled){
        this.enabled = enabled;
    }
}