package gg.scenarios.terra.scenarios;

import gg.scenarios.terra.scenarios.type.*;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ScenarioManager {

    private List<Scenario> scenarios = new ArrayList<>();


    public ScenarioManager() {
        setupAllScenarios();

    }

    private void setupAllScenarios() {
        scenarios.add(new Backpacks());
        scenarios.add(new Barebones());
        scenarios.add(new Bats());
        scenarios.add(new HasteyBoys());
        scenarios.add(new CutClean());
    }


    public List<Scenario> getEnabledScenarios(){
        List<Scenario> tempscenarios = new ArrayList<>();
        scenarios.stream().filter(Scenario::isEnabled).forEach(tempscenarios::add);
        return tempscenarios;
    }

    public Scenario getScenarioByName(String name) {
        return scenarios.stream().filter(scenario -> scenario.getDefaultName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Scenario getScenarioItemStack(ItemStack itemStack) {
        return scenarios.stream().filter(scenario -> scenario.getAdminItemStack().equals(itemStack)).findFirst().orElse(null);
    }

}
