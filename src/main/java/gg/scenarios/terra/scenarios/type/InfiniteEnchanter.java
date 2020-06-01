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

public class InfiniteEnchanter extends Scenario {


    private boolean enabled= false;
    @EventHandler
    public void onGameStart(GameStartEvent event){
        event.getPlayers().stream().filter(UHCPlayer::isOnline).map(UHCPlayer::getPlayer).forEach(player -> {
            player.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 128));
            player.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
            player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, 64));
            player.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
            player.setLevel(10000);
        });
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Infinite Enchanter";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.ANVIL).setName((enabled ? ChatColor.GREEN + "Infinite Enchanter" : ChatColor.RED + "Infinite Enchanter")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Infinite Enchanter";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Players will be given 2 stacks of books and infinite levels");
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
        return new ItemCreator(Material.ANVIL).setName((enabled ? ChatColor.GREEN + "Infinite Enchanter" : ChatColor.RED + "Infinite Enchanter")).setLore(getScenarioExplanation()).get();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
