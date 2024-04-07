package at.fhv.withthem.GameLogic;

public class PlayerPosition {
    private String _playerId;
    private Position _position;
    private String _color;

    public PlayerPosition(String playerId, Position position, String color) {
        _playerId = playerId;
        _position = position;
        _color=color;
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
    public String get_color() {
        return _color;
    }

    public void set_color(String _color) {
        this._color = _color;
    }
}