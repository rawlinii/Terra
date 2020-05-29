package gg.scenarios.terra.commands;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ScenariosCommand implements CommandExecutor {


    private Terra main = Terra.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("scenarios")) {
            Inventory scenarios = Bukkit.createInventory(null, 18, "Active Scenarios");
            int i = 0;
            for (Scenario scenario : main.getScenarioManager().getEnabledScenarios()) {
                scenarios.setItem(i, scenario.getItemStack());
                i++;
            }
            Player player = (Player) sender;
            player.sendMessage(main.getReference().primColor + "Opening the " + main.getReference().secColor + "scenarios" + main.getReference().primColor + " inventory.");
            player.openInventory(scenarios);
        }
        return false;
    }
}