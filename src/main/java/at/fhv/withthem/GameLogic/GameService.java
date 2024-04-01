package at.fhv.withthem.GameLogic;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final GameMap map = new GameMap(20, 20);
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

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

    private boolean canMoveTo(Position position) {
        return map.isWithinBounds((int)position.getX(), (int)position.getY()) && !map.isWall((int)position.getX(), (int)position.getY());
    }

    private Position calculateNewPosition(Position currentPosition, Direction direction, float speed) {
        float newX = currentPosition.getX() + direction.getDx() * speed;
        float newY = currentPosition.getY() + direction.getDy() * speed;
        return new Position(newX, newY);
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

}
