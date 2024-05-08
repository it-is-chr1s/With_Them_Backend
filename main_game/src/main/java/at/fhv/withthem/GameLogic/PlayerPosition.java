package at.fhv.withthem.GameLogic;

public class PlayerPosition {
    private String _playerId;
    private Position _position;
    private String _color;
    private boolean _isAlive;

    public PlayerPosition(String playerId, Position position, String color, boolean isAlive) {
        _playerId = playerId;
        _position = position;
        _color=color;
        _isAlive=isAlive;
    }

    public boolean isAlive() {
        return _isAlive;
    }

    public void setAlive(boolean alive) {
        _isAlive = alive;
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