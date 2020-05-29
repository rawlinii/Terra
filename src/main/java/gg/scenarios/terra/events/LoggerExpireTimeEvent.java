package gg.scenarios.terra.events;

import gg.scenarios.terra.managers.profiles.UHCPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class LoggerExpireTimeEvent extends Event {

    private static HandlerList handlers = new HandlerList();

    private UHCPlayer uhcPlayer;
    private List<ItemStack> drops;

    public LoggerExpireTimeEvent(UHCPlayer uhcPlayer, List<ItemStack> drops) {
        this.uhcPlayer = uhcPlayer;
        this.drops = drops;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}