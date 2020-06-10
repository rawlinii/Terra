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
        scenarios.add(new TimeBomb());
        scenarios.add(new CutClean());
        scenarios.add(new Bombers());
        scenarios.add(new GoldenRetriever());
        scenarios.add(new Siphon());
        scenarios.add(new WebCage());
        scenarios.add(new Timber());
        scenarios.add(new VeinMiner());
        scenarios.add(new Bowless());
        scenarios.add(new EnchantedDeath());
        scenarios.add(new InfiniteEnchanter());
        scenarios.add(new SpeedShot());
        scenarios.add(new BleedingSweets());
        scenarios.add(new SkyHigh());
        scenarios.add(new Superheros());
        scenarios.add(new NoFall());

    }


    public List<Scenario> getEnabledScenarios(){
        List<Scenario> tempScenarios = new ArrayList<>();
        scenarios.stream().filter(Scenario::isEnabled).forEach(tempScenarios::add);
        return tempScenarios;
    }

    public Scenario getScenarioByName(String name) {
        return scenarios.stream().filter(scenario -> scenario.getDefaultName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Scenario getScenarioItemStack(ItemStack itemStack) {
        return scenarios.stream().filter(scenario -> scenario.getAdminItemStack().equals(itemStack)).findFirst().orElse(null);
    }

}
