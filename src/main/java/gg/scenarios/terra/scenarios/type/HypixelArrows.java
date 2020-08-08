package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HypixelArrows extends Scenario {

    private boolean enabled = false;

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Hypixel Arrows";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.ARROW).setName((enabled ? ChatColor.GREEN + "Hypixel Arrows" : ChatColor.RED + "Hypixel Arrows")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Hypixel Arrows";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Placing Flint across the top, sticks in the middle, and feathers on the bottom will craft 16 arrows");
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


    public void setupArrows() {
        ItemStack goldenHead = new ItemStack(Material.ARROW, 16);

        ShapedRecipe goldenHeadRecipe = new ShapedRecipe(goldenHead);
        goldenHeadRecipe.shape(
                "@@@",
                "###",
                "%%%");
        goldenHeadRecipe.setIngredient('@', Material.FLINT);
        goldenHeadRecipe.setIngredient('#', Material.STICK);
        goldenHeadRecipe.setIngredient('%', Material.FEATHER);
        Bukkit.getServer().addRecipe(goldenHeadRecipe);
    }
    @Override
    public ItemStack getItemStack() {
        return new ItemCreator(Material.ARROW).setName((enabled ? ChatColor.GREEN + "Hypixel Arrows" : ChatColor.RED + "Hypixel Arrows")).setLore(getScenarioExplanation()).get();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled){
            setupArrows();
        }else{
            Bukkit.getServer().resetRecipes();
            Terra.getInstance().setupHeads();
        }
        this.enabled = enabled;
    }
}