package at.fhv.withthem.GameLogic;

import org.springframework.stereotype.Component;

@Component
public class GameMap {
    private final String[][] _grid;
    private final int _height = 20;
    private final int _width = 40;
    public GameMap() {
        this._grid = new String[_height][_width];

        //initializeMapLayout();
    }

    public void initializeMapLayout() {
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

        //type should not include String %id
        setTask(7, 7, "Connecting Wires", 1);
        setTask(11, 5, "File Download", 2);
        setTask(11, 9, "File Upload", 2);
    }

    public void setTask(int x, int y, String type, int id){
        _grid[y][x] = type + "%id" + id;
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
