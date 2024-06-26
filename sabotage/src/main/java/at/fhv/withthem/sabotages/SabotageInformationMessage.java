package at.fhv.withthem.sabotages;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.HashMap;
import java.util.List;

public class SabotageInformationMessage {
    private List<HashMap<Integer, String>> _availableSabotages;
    private int _currentSabotageID;
    private int _timer;
    private int _cooldown;
    private String _status;

    @JsonCreator
    public SabotageInformationMessage(List<HashMap<Integer, String>> availableSabotages, int currentSabotageID, int timer, int cooldown, String status) {
        _availableSabotages = availableSabotages;
        _currentSabotageID = currentSabotageID;
        this._timer = timer;
        this._cooldown = cooldown;
        this._status = status;
    }

    public List<HashMap<Integer, String>> getAvailableSabotages() {
        return _availableSabotages;
    }

    public int getCurrentSabotageID() {
        return _currentSabotageID;
    }

    public int getTimer() {
        return _timer;
    }

    public int getCooldown() {
        return _cooldown;
    }

    public String getStatus() {
        return _status;
    }
}
