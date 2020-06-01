package gg.scenarios.terra.tasks;

import gg.scenarios.terra.Terra;
import gg.scenarios.terra.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PregenTask {

    public static boolean RUNNING = false;

    private Terra terra = Terra.getInstance();

    public PregenTask() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + terra.getReference().getWorld().getName() + " set " + 1000 + " " + 1000 + " 0 0");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb " + terra.getReference().getWorld().getName() + " fill 250");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb fill confirm");
        ;
        RUNNING = true;
    }
}
