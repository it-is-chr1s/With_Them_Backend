package at.fhv.withthem.GameLogic;

public class PlayerPosition {
    private String _playerId;
    private Position _position;

    public PlayerPosition(String playerId, Position position) {
        _playerId = playerId;
        _position = position;
    }

    public String getPlayerId() {
        return _playerId;
    }

    public void setPlayerId(String playerId) {
        _playerId = playerId;
    }

    public Position getPosition() {
        return _position;
    }

    public void setPosition(Position position) {
        _position = position;
    }
}