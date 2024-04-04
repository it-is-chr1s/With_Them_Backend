package at.fhv.withthem.GameLogic;

public class GameMap {
    private final String[][] _grid;
    private final int _height;
    private final int _width;

    public GameMap(int width, int height) {
        this._grid = new String[height][width];
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
        setWall(9, 5);
        setWall(9, 6);
        setWall(9, 7);
        setWall(9, 8);
        setWall(9, 9);
        setWall(8, 9);
        setWall(7, 9);
        setWall(6, 9);
        setWall(5, 9);
        setWall(5, 8);

        setTask(7, 7, "Connecting Wires");
        setTask(11, 5, "File Download");
        setTask(11, 9, "File Upload");
    }

    public void setTask(int x, int y, String type){
        _grid[y][x] = type;
    }

    public boolean isTask(int x, int y){
        return (_grid[y][x] != null && !_grid[y][x].equals("Wall"));
    }

    public String getContent(int x, int y) {
        return _grid[y][x];
    }


    public void setWall(int x, int y) {
        _grid[y][x] = "Wall";
    }

    public boolean isWall(int x, int y) {
        return (_grid[y][x] != null && _grid[y][x].equals("Wall"));
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
