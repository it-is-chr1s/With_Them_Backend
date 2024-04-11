package at.fhv.withthem.GameLogic.Requests;

import at.fhv.withthem.GameLogic.Colors;
import com.fasterxml.jackson.annotation.JsonCreator;

public class ChangeColorRequest {
    private String _gameId;

    private Colors _color;
    private String _name;

    public String getGameId() {
        return _gameId;
    }

    public Colors getColor() {
        return _color;
    }

    public void set_color(Colors _color) {
        this._color = _color;
    }

    public String getName() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public ChangeColorRequest() {
    }

    @JsonCreator
    public ChangeColorRequest(String gameId,String name, String color) {
        _gameId=gameId;
        _name = name;
        _color = Colors.fromHex(color);
    }
}
