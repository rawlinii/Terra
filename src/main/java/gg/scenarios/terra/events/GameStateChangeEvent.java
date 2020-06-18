package gg.scenarios.terra.events;

import gg.scenarios.terra.managers.GameState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class GameStateChangeEvent extends Event {


    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private GameState gameState;

    public GameStateChangeEvent(GameState gameState) {
        this.gameState = gameState;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}