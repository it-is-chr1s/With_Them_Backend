package at.fhv.withthem.sabotages;

import java.util.HashMap;
import java.util.List;

public class SabotageInformationMessage {
    private List<HashMap<Integer, String>> _availableSabotages;
    private int _currentSabotageID;
    private int timer;
    private int cooldown;

    public SabotageInformationMessage(List<HashMap<Integer, String>> availableSabotages, int currentSabotageID, int timer, int cooldown) {
        _availableSabotages = availableSabotages;
        _currentSabotageID = currentSabotageID;
        this.timer = timer;
        this.cooldown = cooldown;
    }

    public List<HashMap<Integer, String>> getAvailableSabotages() {
        return _availableSabotages;
    }

    public int getCurrentSabotageID() {
        return _currentSabotageID;
    }

    public int getTimer() {
        return timer;
    }

    public int getCooldown() {
        return cooldown;
    }
}
