package at.fhv.withthem.MeetingLogic;

import java.util.List;

public class LoadEmergencyMeetingMessage {
    private String _gameId;
    private List<String> _names;

    public LoadEmergencyMeetingMessage(String gameId, List<String> names) {
        _gameId = gameId;
        _names = names;
    }

    public String get_gameId() {
        return _gameId;
    }

    public void set_gameId(String _gameId) {
        this._gameId = _gameId;
    }

    public List<String> get_names() {
        return _names;
    }

    public void set_names(List<String> _names) {
        this._names = _names;
    }
}
