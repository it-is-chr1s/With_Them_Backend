package at.fhv.withthem.GameLogic;

public class GameMap {
    private final boolean[][] grid;

    public GameMap(int width, int height) {
        this.grid = new boolean[height][width];
    }

    public void setWall(int x, int y) {
        grid[y][x] = true;
    }

    public boolean isWall(int x, int y) {
        return grid[y][x];
    }

    public boolean isWithinBounds(int x, int y) {
        return y >= 0 && y < grid.length && x >= 0 && x < grid[0].length;
    }
}
