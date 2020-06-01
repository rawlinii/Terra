package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.utils.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Siphon extends Scenario {


    Random random = Terra.getInstance().getGameManager().getRandom();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (enabled) {
            if (event.getEntity().getKiller() == null) return;
            event.getDrops().add(createBook());
            event.getEntity().getKiller().setLevel(event.getEntity().getKiller().getLevel() +2);
            event.getEntity().getKiller().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5, 1));
        }
    }

    private ItemStack createBook() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta esm = (EnchantmentStorageMeta) book.getItemMeta();
        esm.addStoredEnchant(Enchantment.values()[random.nextInt(Enchantment.values().length)], 1, true);
        book.setItemMeta(esm);
        return book;
    }


    private boolean enabled = false;

    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "Siphon";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.ENCHANTED_BOOK).setName((enabled ? ChatColor.GREEN + "Siphon" : ChatColor.RED + "Siphon")).setLore(Arrays.asList(getState())).get();
    }

    @Override
    public String getName() {
        return "Siphon";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Whenever you get a kill, you will regenerate 2 hearts, gain 2 levels and get a random tier 1 enchanted book");
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
        return new ItemCreator(Material.ENCHANTED_BOOK).setName((enabled ? ChatColor.GREEN + "Siphon" : ChatColor.RED + "Siphon")).setLore(getScenarioExplanation()).get();
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
