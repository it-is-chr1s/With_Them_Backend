package at.fhv.withthem.GameLogic;

import at.fhv.withthem.GameSession.GameSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    //private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, GameSession> gameSessions = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public synchronized void updatePlayerDirection(String playerId, Direction direction) {
        Player player = players.get(playerId);
        if (player != null) {
            player.setDirection(direction);
            player.setHasMoved(!direction.equals(Direction.NONE));
        }
    }

    @Scheduled(fixedRate = 1)
    public void gameLoop() {
        gameSessions.forEach((id, session) -> {
            ConcurrentHashMap<String, Player> players = session.getPlayers();
            players.forEach((playerId, player) -> {
                if (player.hasMoved()) {
                    Position currentPosition = player.getPosition();
                    Direction direction = player.getDirection();
                    float speed = 0.01f;

                    Position newPosition = session.calculateNewPosition(currentPosition, direction, speed);

                    if (session.canMoveTo(newPosition)) {
                        player.setPosition(newPosition);
                        messagingTemplate.convertAndSend("/topic/position", new PlayerPosition(id, newPosition));
                    }
                }
            });
        });

    }

    private void broadcastSessionState(String sessionId, GameSession session) {
        Map<String, Object> state = new HashMap<>();
        state.put("players", session.getPlayers());
        state.put("walls", session.getWallPositions());

        messagingTemplate.convertAndSend("/topic/gameState/" + sessionId, state);
    }

    public GameSession createSession(int width, int height) {
        String sessionId = UUID.randomUUID().toString();
        GameSession session = new GameSession(sessionId, width, height);
        gameSessions.put(sessionId, session);
        return session;
    }

    public GameSession getSession(String sessionId) {
        return gameSessions.get(sessionId);
    }

    public void removeSession(String sessionId) {
        gameSessions.remove(sessionId);
    }

    public boolean movePlayerInSession(String sessionId, String playerId, Direction direction, float speed) {
        GameSession session = getSession(sessionId);
        if (session != null) {
            return session.movePlayer(playerId, direction, speed);
        }
        return false;
    }

}
