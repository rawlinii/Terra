package gg.scenarios.terra.scenarios.type;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.scenarios.Scenario;
import gg.scenarios.terra.scenarios.ScenarioManager;
import gg.scenarios.terra.utils.ItemCreator;
import gg.scenarios.terra.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gg.scenarios.terra.utils.Utils.giveItem;

public class VeinMiner extends Scenario {


    private ScenarioManager scenarioManager ;

    @EventHandler
    public void on(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block block = e.getBlock();


        if(!p.isSneaking()){
            return;
        }

        if(block.getType() == Material.COAL_ORE){
            ArrayList<Block> blocks = getBlocks(block, 2, Material.COAL_ORE);
            for (Block b : blocks){
                b.setType(Material.AIR);
            }

            if(blocks.size() == 0) return;
            giveItem(p, new ItemStack(Material.COAL, blocks.size()));
            ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(2*blocks.size());
        }

        if(block.getType() == Material.IRON_ORE){
            ArrayList<Block> blocks = getBlocks(block, 2, Material.IRON_ORE);
            for (Block b : blocks){
                b.setType(Material.AIR);
            }

            if(blocks.size() == 0) return;
            if(Terra.getInstance().getScenarioManager().getScenarioByName("CutClean").isEnabled()){
                giveItem(p,new ItemStack(Material.IRON_INGOT, blocks.size()));
            }else{
                giveItem(p, new ItemStack(Material.IRON_ORE, blocks.size()));
            }

            ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(2*blocks.size());
        }

        if(block.getType() == Material.GOLD_ORE){
            ArrayList<Block> blocks = getBlocks(block, 2, Material.GOLD_ORE);
            for (Block b : blocks){
                b.setType(Material.AIR);
            }

            if(blocks.size() == 0) return;

            if(Terra.getInstance().getScenarioManager().getScenarioByName("CutClean").isEnabled()){
                giveItem(p,new ItemStack(Material.GOLD_INGOT, blocks.size()));
            }else{
                giveItem(p,new ItemStack(Material.GOLD_ORE, blocks.size()));
            }

            ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(2*blocks.size());
        }

        if(block.getType() == Material.DIAMOND_ORE){
            ArrayList<Block> blocks = getBlocks(block, 2, Material.DIAMOND_ORE);
            for (Block b : blocks){
                b.setType(Material.AIR);
            }

            if(blocks.size() == 0) return;
            giveItem(p,new ItemStack(Material.DIAMOND, blocks.size()));
            ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(2*blocks.size());
        }

        if (block.getType() == Material.REDSTONE_ORE || block.getType() == Material.GLOWING_REDSTONE_ORE) {
            ArrayList<Block> blocks = getBlocks(block, 2, Material.REDSTONE_ORE);
            blocks.addAll(getBlocks(block, 2, Material.GLOWING_REDSTONE_ORE));
            for (Block b : blocks){
                b.setType(Material.AIR);
            }

            if(blocks.size() == 0) return;
            giveItem(p, new ItemStack(Material.REDSTONE, blocks.size()*4));
            ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(2*blocks.size());
        }

        if (block.getType() == Material.LAPIS_ORE) {
            ArrayList<Block> blocks = getBlocks(block, 2, Material.LAPIS_ORE);
            for (Block b : blocks) {
                b.setType(Material.AIR);
            }

            if (blocks.size() == 0) return;
            giveItem(p, new ItemStack(Material.INK_SACK, blocks.size() * 4, DyeColor.BLUE.getDyeData()));
            ((ExperienceOrb) block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(2 * blocks.size());
        }

        if(block.getType() == Material.EMERALD_ORE){
            ArrayList<Block> blocks = getBlocks(block, 2, Material.EMERALD_ORE);
            for (Block b : blocks){
                b.setType(Material.AIR);
                p.playSound(p.getLocation(), Sound.DIG_STONE, 10, 1);
            }

            if(blocks.size() == 0) return;
            giveItem(p, new ItemStack(Material.EMERALD, blocks.size()));
            ((ExperienceOrb)block.getWorld().spawn(block.getLocation(), ExperienceOrb.class)).setExperience(2*blocks.size());
        }

    }


    private ArrayList<Block> getBlocks(Block start, int radius, Material filter){
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(double x = start.getLocation().getX() - radius; x <= start.getLocation().getX() + radius; x++){
            for(double y = start.getLocation().getY() - radius; y <= start.getLocation().getY() + radius; y++){
                for(double z = start.getLocation().getZ() - radius; z <= start.getLocation().getZ() + radius; z++){
                    Location loc = new Location(start.getWorld(), x, y, z);
                    if(loc.getBlock().getType() == filter)
                        blocks.add(loc.getBlock());
                }
            }
        }
        return blocks;

    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Cow) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.COOKED_BEEF,3));
            e.getDrops().add(new ItemStack(Material.LEATHER,1));
        } else if (e.getEntity() instanceof Chicken) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.COOKED_CHICKEN,3));
            e.getDrops().add(new ItemStack(Material.FEATHER, 1));
        } else if (e.getEntity() instanceof Horse) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.LEATHER,1));
        } else if (e.getEntity() instanceof Pig || e.getEntity() instanceof Sheep) {
            e.getDrops().clear();
            e.getDrops().add(new ItemStack(Material.GRILLED_PORK,3));
        }
    }



    private boolean enabled = false;


    @Override
    public boolean isCompatibleWith(Class<? extends Scenario> clazz) {
        return true;
    }

    @Override
    public String getDefaultName() {
        return "VeinMiner";
    }

    @Override
    public ItemStack getAdminItemStack() {
        return new ItemCreator(Material.IRON_PICKAXE).setName((enabled ? ChatColor.GREEN + "Vein Miner" : ChatColor.RED + "Vein Miner")).setLore(Arrays.asList(getState())).get();

    }

    @Override
    public String getName() {
        return "VeinMiner";
    }

    @Override
    public List<String> getScenarioExplanation() {
        List<String> explain = new ArrayList<>();
        explain.add("");
        explain.add(ChatColor.YELLOW + "Explanation: ");
        explain.add(ChatColor.BLUE + " - Mining one ore will mine the entire vein");
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
        return new ItemCreator(Material.IRON_PICKAXE).setName((enabled ? ChatColor.GREEN + "Vein Miner" : ChatColor.RED + "Vein Miner")).setLore(getScenarioExplanation()).get();
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