package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bats extends Scenario {

    GameManager manager = Terra.getInstance().getGameManager();
    private boolean enabled = false;

    @EventHandler
    public void onBats(EntityDeathEvent event){
        if (!(event.getEntity() instanceof Bat)) return;
        if (!(event.getEntity().getKiller() instanceof Player)) return;;
        int num = manager.getRandom().nextInt(100);
        if (num < 94){
            event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
        }else{
            event.getEntity().getKiller().setHealth(0.0D);
        }
    }

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Bats";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.GOLDEN_APPLE).setName((enabled ? ChatColor.GREEN + "Bats" : ChatColor.RED + "Bats")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Bats";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Killing a bat has a 95% chance of dropping a golden apple and 5% of killing you");
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
        return new ItemCreator(Material.GOLDEN_APPLE).setName((enabled ? ChatColor.GREEN + "Bats" : ChatColor.RED + "Bats")).setLore(getScenarioExplanation()).get();
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
