package at.fhv.withthem.GameLogic;

public class Player {

    private String _id;
    private Position _position;

    public Player(String id, Position position) {
        this._id = id;
        this._position = position;
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
}
