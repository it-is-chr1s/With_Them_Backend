package at.fhv.withthem.GameLogic;

public class Position {

    private float _x;
    private float _y;

    public Position(float x, float y) {
        _x = x;
        _y = y;
    }

    public float getX() {
        return _x;
    }

    public void setX(float x) {
        _x = x;
    }

    public float getY() {
        return _y;
    }

    public void setY(float y) {
        _y = y;
    }
}
