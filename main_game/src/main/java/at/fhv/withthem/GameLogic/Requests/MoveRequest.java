package at.fhv.withthem.GameLogic.Requests;

import at.fhv.withthem.GameLogic.Colors;
import at.fhv.withthem.GameLogic.Direction;
import com.fasterxml.jackson.annotation.JsonCreator;

public class MoveRequest {
    private Direction _direction;
    private String _name;
    public MoveRequest() {
    }

    @JsonCreator
    public MoveRequest(Direction direction, String name, Colors color) {
        _direction = direction;
        _name = name;
    }

    public Direction getDirection() {
        return _direction;
    }

    public void setDirection(Direction direction) {
        _direction = direction;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
}