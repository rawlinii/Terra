package gg.scenarios.terra.managers.profiles.stats;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics {
    private boolean loaded;

    private Stat kills = new Stat(), deaths = new Stat(), wins = new Stat(), kdr = new Stat(), games = new Stat();
    private Stat goldenHeadsEaten = new Stat(), goldenApplesEaten = new Stat();
    private Stat coalMined = new Stat(), ironMined = new Stat(), goldMined = new Stat(), lapisMined = new Stat(), redstoneMined = new Stat(), diamondMined = new Stat(), emeraldMined = new Stat();
}
