package gg.scenarios.terra.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

@Getter
@Setter
public class KingRemoveEvent extends Event {


    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    private UUID uuid;

    public KingRemoveEvent(UUID uuid) {
        this.uuid = uuid;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }
}
