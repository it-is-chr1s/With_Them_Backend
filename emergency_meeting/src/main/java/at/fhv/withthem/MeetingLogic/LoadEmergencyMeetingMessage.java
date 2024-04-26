package at.fhv.withthem.MeetingLogic;

public class LoadEmergencyMeetingMessage {
    private String _gameId;
    private String _name;
    private boolean _isAlive;

    public LoadEmergencyMeetingMessage(String gameId, String name, boolean live) {
        _gameId = gameId;
        _name = name;
        _isAlive = live;
    }

    public String getGameId() {
        return _gameId;
    }

    public String getName() {
        return _name;
    }

    public boolean getIsAlive() {
        return _isAlive;
    }
}
