package gg.scenarios.terra.scenarios;

import gg.scenarios.terra.Terra;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Scenario implements Listener {

    protected Terra main = Terra.getInstance();


    protected boolean enabled;

    public abstract boolean isCompatibleWith(Class<? extends Scenario> clazz);

    public abstract String getDefaultName();


    public abstract ItemStack getAdminItemStack();

    /**
     * The name of the scenario
     * The difference between #getName and #getDefaultName is that we're gonna use ChatColors for this aswell
     * @return the name
     */

    public abstract String getName();

    /**
     * Scenario explanation
     * Used in /scenarios to explain how the scenario will work
     * @return List of string
     */

    public abstract List<String> getScenarioExplanation();

    /**
     * The state of the scenario we're looking at
     * Will use this to return like Enabled/Disabled
     * @return the state
     */

    public abstract String getState();

    /**
     * Material of the scenario
     * Using this for when one does /scenarios or /editscenarios
     * @return the Material
     */

    public abstract ItemStack getItemStack();

    /**
     * Returns whether a scenario is enabled or not
     * @return true for enabled false for not
     */

    public boolean isEnabled(){
        return enabled;
    }

    /**
     * Sets the scenario either enabled or disabled
     * #setScenario(false) it disabled and #setScenario(true) if want to enable
     * @param enabled the boolean
     */

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }
    /**
     * Increases the votes with a specific amount
     * For example #increaseVotes(1) to increase it by 1 and #increaseVotes(-1) to decrease by 1
     *
     */


    public void enable() {
        setEnabled(true);
        Bukkit.getPluginManager().registerEvents(this, Terra.getPlugin(Terra.class));
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', main.getReference().getScenario()) + getName() + main.getReference().primColor + " has been " +main.getReference().secColor + "enabled" +main.getReference().primColor+".");
    }

    public void disable() {
        setEnabled(false);
        HandlerList.unregisterAll(this);
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', main.getReference().getScenario()) + getName() + main.getReference().primColor+ " has been " + main.getReference().secColor  + "disabled" + main.getReference().primColor +".");

    }



}