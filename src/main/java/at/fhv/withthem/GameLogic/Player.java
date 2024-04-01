package at.fhv.withthem.GameLogic;

public class Player {

    private String _id;
    private Position _position;
    private Direction _direction;
    public Player(String id, Position position) {
        this._id = id;
        this._position = position;
        this._direction = Direction.NONE;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

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
}
