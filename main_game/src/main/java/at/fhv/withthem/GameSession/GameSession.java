package at.fhv.withthem.GameSession;
import at.fhv.withthem.GameLogic.Direction;
import at.fhv.withthem.GameLogic.GameMap;
import at.fhv.withthem.GameLogic.Player;
import at.fhv.withthem.GameLogic.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GameSession {
    private final String sessionId;
    private final GameMap map;
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    public GameSession(String sessionId, int width, int height) {
        this.sessionId = sessionId;
        this.map = new GameMap(width, height);
    }


    public String getSessionId() {
        return sessionId;
    }

    public GameMap getMap() {
        return map;
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public boolean playerExists(String playerId) {
        return players.containsKey(playerId);
    }

    public void registerPlayer(String playerId, Position startPosition) {
        players.put(playerId, new Player(playerId, startPosition));
    }

    public List<Position> getWallPositions() {
        List<Position> wallPositions = new ArrayList<>();
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.isWall(x, y)) {
                    wallPositions.add(new Position(x, y));
                }
            }
        }
        return wallPositions;
    }

    public boolean canMoveTo(Position position) {
        return map.isWithinBounds((int)position.getX(), (int)position.getY()) && !map.isWall((int)position.getX(), (int)position.getY());
    }

    public synchronized boolean movePlayer(String playerId, Direction direction, float speed) {
        Player player = players.get(playerId);
        Position currentPosition = player.getPosition();

        Position newPosition = calculateNewPosition(currentPosition, direction, speed);

        if (canMoveTo(newPosition)) {
            player.setPosition(newPosition);
            return true;
        }
        return false;
    }

    public Position calculateNewPosition(Position currentPosition, Direction direction, float speed) {
        float newX = currentPosition.getX() + direction.getDx() * speed;
        float newY = currentPosition.getY() + direction.getDy() * speed;
        return new Position(newX, newY);
    }

    public ConcurrentHashMap<String, Player> getPlayers() {
        return players;
    }

}
