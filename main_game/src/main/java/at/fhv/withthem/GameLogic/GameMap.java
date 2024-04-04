package at.fhv.withthem.GameLogic;

public class GameMap {
    private final boolean[][] _grid;
    private final int _height;
    private final int _width;

    public GameMap(int width, int height) {
        this._grid = new boolean[height][width];
        this._height = height;
        this._width = width;

        initializeMapLayout();
    }

    private void initializeMapLayout() {
        setWall(5, 6);
        setWall(5, 5);
        setWall(6, 5);
        setWall(7, 5);
        setWall(8, 5);
        setWall(8, 6);
        setWall(8, 7);
        setWall(8, 8);
        setWall(8, 9);
        setWall(7, 9);
        setWall(6, 9);
        setWall(5, 9);
        setWall(5, 8);
    }


    public void setWall(int x, int y) {
        _grid[y][x] = true;
    }

    public boolean isWall(int x, int y) {
        return _grid[y][x];
    }

    public boolean isWithinBounds(int x, int y) {
        return y >= 0 && y < _grid.length && x >= 0 && x < _grid[0].length;
    }

    public int getHeight() {
        return _height;
    }

    public int getWidth() {
        return _width;
    }
}
