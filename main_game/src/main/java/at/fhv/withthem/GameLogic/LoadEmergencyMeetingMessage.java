package at.fhv.withthem.GameLogic;

public class LoadEmergencyMeetingMessage {
    private String _gameId;
    private String _name;
    private boolean _isAlive;

    public LoadEmergencyMeetingMessage(String gameId, String name, boolean isAlive) {
        _gameId = gameId;
        _name = name;
        _isAlive = isAlive;
    }
}
