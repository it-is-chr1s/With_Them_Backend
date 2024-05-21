package at.fhv.withthem.GameLogic;

public class PlayerPosition {
    private String _playerId;
    private Position _position;
    private Position _deathPosition;
    private String _color;
    private boolean _isAlive;

    public PlayerPosition(String playerId, Position position, String color, boolean isAlive, Position deathPosition) {
        _playerId = playerId;
        _position = position;
        _color=color;
        _isAlive=isAlive;
        _deathPosition = deathPosition;
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

    public Position getDeathPosition() {
        return _deathPosition;
    }

    public void setDeathPosition(Position deathPosition) {
        _deathPosition = deathPosition;
    }

    public String getColor() {
        return _color;
    }

    public void setColor(String color) {
        _color = color;
    }
}