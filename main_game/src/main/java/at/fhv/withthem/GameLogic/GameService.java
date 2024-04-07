package at.fhv.withthem.GameLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    @Autowired
    private final GameMap map;
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public GameService(GameMap map) {
        this.map = map;
    }

    public synchronized void updatePlayerDirection(String playerId, Direction direction) {
        Player player = players.get(playerId);
        if (player != null) {
            player.setDirection(direction);
            player.setHasMoved(!direction.equals(Direction.NONE));
        }
    }
    public synchronized void updatePlayerColor(String playerId, Colors colors) {
        Player player = players.get(playerId);
        if (player != null) {
            player.setColor(colors);
        }
        System.out.println("Color changed");
    }

    @Scheduled(fixedRate = 1)
    public void gameLoop() {
        players.forEach((id, player) -> {
            if (player.hasMoved()) {
                Position currentPosition = player.getPosition();
                Direction direction = player.getDirection();
                float speed = 0.01f;

                Position newPosition = calculateNewPosition(currentPosition, direction, speed);

                if (canMoveTo(newPosition)) {
                    player.setPosition(newPosition);
                    messagingTemplate.convertAndSend("/topic/position", new PlayerPosition(id, newPosition));

                    messagingTemplate.convertAndSend("/topic/player/" + id + "/controlsEnabled/task", canDoTask(player.getPosition()));
                }
            }
        });
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

    private boolean canMoveTo(Position position) {
        return map.isWithinBounds((int)position.getX(), (int)position.getY())
                && !map.isWall((int)position.getX(), (int)position.getY());
    }

    private boolean canDoTask(Position position){
        return map.isTask((int)position.getX(), (int)position.getY());
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

    public void registerPlayer(String playerId, Position startPosition, Colors color) {
        players.put(playerId, new Player(playerId, startPosition, color));
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

    public List<TaskPosition> getTaskPositions(){
        List<TaskPosition> taskPositions = new ArrayList<>();
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.isTask(x, y)) {
                    taskPositions.add(new TaskPosition(x, y, map.getContent(x, y)));
                }
            }
        }
        return taskPositions;
    }

    public GameMap getMap() {
        return map;
    }
}
