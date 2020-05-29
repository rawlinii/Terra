package gg.scenarios.terra.scenarios.type;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoldenRetriever extends Scenario {


    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta gMeta = goldenHead.getItemMeta();
        gMeta.setDisplayName(ChatColor.AQUA + "Golden Head");
        gMeta.setLore(Arrays.asList("You've crafted a Golden Head!", "Consuming this will grant you even greater effects", "than a normal Golden Apple!"));
        goldenHead.setItemMeta(gMeta);
        event.getDrops().add(goldenHead);
    }

    private boolean enabled = false;

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Golden Retriever";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.GOLDEN_CARROT).setName((enabled ? ChatColor.GREEN + "Golden Retriever" : ChatColor.RED + "Golden Retriever")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Golden Retriever";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Players drop golden heads");
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
        return new ItemCreator(Material.GOLDEN_CARROT).setName((enabled ? ChatColor.GREEN + "Golden Retriever" : ChatColor.RED + "Golden Retriever")).setLore(getScenarioExplanation()).get();
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