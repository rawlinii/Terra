package gg.scenarios.terra.events;

import gg.scenarios.terra.managers.profiles.UHCPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;

public class GameStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private ArrayList<UHCPlayer> players;

    public GameStartEvent(ArrayList<UHCPlayer> players) {
        this.players = players;
    }

    public ArrayList<UHCPlayer> getPlayers() {
        return players;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}