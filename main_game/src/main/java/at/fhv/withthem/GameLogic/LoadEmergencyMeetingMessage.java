package at.fhv.withthem.GameLogic;

import java.util.List;

public class LoadEmergencyMeetingMessage {
    private String _gameId;
    private List<String> _name;

    public LoadEmergencyMeetingMessage(String gameId, List<String> name) {
        _gameId = gameId;
        _name = name;
    }

    public String get_gameId() {
        return _gameId;
    }

    public void set_gameId(String _gameId) {
        this._gameId = _gameId;
    }

    public List<String> get_name() {
        return _name;
    }

    public void set_name(List<String> _name) {
        this._name = _name;
    }
}
