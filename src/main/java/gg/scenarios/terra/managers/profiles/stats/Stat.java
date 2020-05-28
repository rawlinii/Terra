package gg.scenarios.terra.managers.profiles.stats;

import lombok.Data;
import lombok.Setter;

@Data
public class Stat {

    @Setter
    private int amount = 0;

    public void increment() {
        this.amount++;
    }

    public void decrement() {
        this.amount--;
    }
}