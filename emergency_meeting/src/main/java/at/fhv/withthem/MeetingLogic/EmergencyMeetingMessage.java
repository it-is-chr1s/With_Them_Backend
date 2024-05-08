package at.fhv.withthem.MeetingLogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = EmergencyMeetingMessage.class
)
public class EmergencyMeetingMessage {
    private String _gameId;
    private List<String>_names;

    @JsonCreator
    public EmergencyMeetingMessage(String lobby, List<String>names) {
        _gameId = lobby;
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
