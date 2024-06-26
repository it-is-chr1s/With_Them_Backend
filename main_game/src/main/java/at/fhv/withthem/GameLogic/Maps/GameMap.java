package at.fhv.withthem.GameLogic.Maps;

import org.springframework.stereotype.Component;


public abstract class GameMap {
    protected final String[][] _grid;

    private int _height;
    private int _width;
    //TODO: Constructor for lobby

    public GameMap(int height, int width) {
        _grid = new String[height][width];
        _height = height;
        _width = width;
    }



    public void setTask(int x, int y, String type, int id, boolean isSabotage){
        _grid[y][x] = type + "%id=" + id + "%sabotage=" + isSabotage;
    }

    public boolean isTask(int x, int y){
        return (_grid[y][x] != null && !_grid[y][x].equals("Wall")&& !_grid[y][x].equals("Meeting"));
    }

    public String getContent(int x, int y) {
        return _grid[y][x];
    }

    public void setMeetingPoint(int x, int y) {
        _grid[y][x] = "Meeting";
    }

    public void setWall(int x, int y) {
        _grid[y][x] = "Wall";
    }

    public void setWalls(int x, int y, int width, int height) {
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                setWall(j, i);
            }
        }
    }

    public boolean isWall(int x, int y) {
        return (_grid[y][x] != null && _grid[y][x].equals("Wall"));
    }
    public boolean isMeetingPoint(int x, int y) {
        return (_grid[y][x] != null && _grid[y][x].equals("Meeting"));
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
