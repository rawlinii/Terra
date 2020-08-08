package gg.scenarios.terra.events;

import gg.scenarios.terra.managers.GameState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@Getter
@Setter
public class KingAddEvent extends Event {


    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private UUID uuid;

    public KingAddEvent(UUID uuid) {
        this.uuid = uuid;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
