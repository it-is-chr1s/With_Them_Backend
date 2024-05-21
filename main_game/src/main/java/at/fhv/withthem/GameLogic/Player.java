package at.fhv.withthem.GameLogic;

public class Player {

    private String _id;
    private String _name;
    private Colors _color;
    private Position _position;
    private Position _deathPosition;
    private Direction _direction;

    private int _role = 0;

    private boolean _isAlive = true;
    private boolean _hasMoved = false;

    private long _lastKillTime = 0L;
    public Player(String id, Position position, Colors color) {
        this._id = id;
        this._name=id;
        this._position = position;
        this._direction = Direction.NONE;
        this._color=color;
        this._deathPosition = new Position(-1, -1);
    }

    public boolean canKillAgain() {
        return System.currentTimeMillis() - _lastKillTime > 20_000;
    }

    public void recordKill() {
        _lastKillTime = System.currentTimeMillis();
    }

    public int getRole() {return _role;}
    public void setRole(int role) {_role = role;}

    public boolean isAlive() {return _isAlive;}
    public void kill() {
        _isAlive = false;
        _deathPosition = new Position(_position.getX(), _position.getY());
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }
    public void setName(String _name) {this._name = _name;}
    public String getName() {return _name;}

    public Colors getColor() {return _color;}
    public void setColor(Colors _color) {this._color = _color;  }
    public Position getPosition() {
        return _position;
    }

    public void setPosition(Position position) {
        _position = position;
    }

    public Direction getDirection() {
        return _direction;
    }

    public void setDirection(Direction direction) {
        _direction = direction;
    }

    public boolean hasMoved() {
        return _hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this._hasMoved = hasMoved;
    }

    public void setAlive(boolean alive) {
        _isAlive = alive;
    }

    public Position getDeathPosition() {
        return _deathPosition;
    }

    public void setDeathPosition(Position deathPosition) {
        _deathPosition = deathPosition;
    }
}
