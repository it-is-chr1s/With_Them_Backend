package at.fhv.withthem.GameLogic.Requests;

import at.fhv.withthem.GameLogic.Colors;
import at.fhv.withthem.GameLogic.Direction;
import com.fasterxml.jackson.annotation.JsonCreator;

public class ChangeColorRequest {
    private Colors _color;

    public Colors get_color() {
        return _color;
    }

    public void set_color(Colors _color) {
        this._color = _color;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    private String _name;
    public ChangeColorRequest() {
    }

    @JsonCreator
    public ChangeColorRequest(String name, String color) {
        _name = name;
        _color = Colors.fromHex(color);
    }
}
