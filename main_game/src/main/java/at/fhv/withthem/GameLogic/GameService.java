package at.fhv.withthem.GameLogic;

import at.fhv.withthem.GameLogic.Maps.GameMap;
import at.fhv.withthem.GameLogic.Maps.LobbyMap;
import at.fhv.withthem.GameLogic.Maps.PolusMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final ConcurrentHashMap<String, Game> games = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public synchronized void updatePlayerDirection(String gameId, String playerId, Direction direction) {
        Player player = getPlayers(gameId).get(playerId);
        if (player != null) {
            player.setDirection(direction);
            player.setHasMoved(!direction.equals(Direction.NONE));
        }
    }

    public synchronized void updatePlayerColor(String gameId, String playerId, Colors colors) {
        Player player = getPlayers(gameId).get(playerId);
        if (player != null && colors!=player.getColor()) {
            player.setColor(colors);
            messagingTemplate.convertAndSend("/topic/"+gameId+"/position", new PlayerPosition(playerId, player.getPosition(), colors.getHexValue()));
        }
    }

    @Scheduled(fixedRate = 1)
    public void gameLoop() {
        games.forEach((gameId, game) -> {
            ConcurrentHashMap<String, Player> players = game.getPlayers();
            players.forEach((playerId, player) -> {
                if (player.hasMoved()) {
                    Position currentPosition = player.getPosition();
                    Direction direction = player.getDirection();
                    float speed = 0.01f;

                    Position newPosition = calculateNewPosition(currentPosition, direction, speed);

                    if (canMoveTo(gameId, newPosition)) {
                        player.setPosition(newPosition);
                        messagingTemplate.convertAndSend("/topic/" +gameId+"/position", new PlayerPosition(playerId, newPosition, player.getColor().getHexValue()));
                        messagingTemplate.convertAndSend("/topic/" +gameId+"/player/" + playerId + "/controlsEnabled/task", canDoTask(gameId, player.getPosition()));
                        messagingTemplate.convertAndSend("/topic/" +gameId+"/player/" + playerId + "/controlsEnabled/emergencyMeeting", canCallEmergencyMeeting(gameId, player.getPosition()));
                    }
                }
            });
        });
    }

    public synchronized boolean movePlayer(String gameId, String playerId, Direction direction, float speed) {
        Player player = getPlayers(gameId).get(playerId);
        Position currentPosition = player.getPosition();

        Position newPosition = calculateNewPosition(currentPosition, direction, speed);

        if (canMoveTo(gameId, newPosition)) {
            player.setPosition(newPosition);
            return true;
        }
        return false;
    }

    private boolean canMoveTo(String gameId, Position position) {
        GameMap map =getMap(gameId);
        return map.isWithinBounds((int)position.getX(), (int)position.getY())
                && !map.isWall((int)position.getX(), (int)position.getY());
    }

    private boolean canDoTask(String gameId, Position position){
        return getMap(gameId).isTask((int)position.getX(), (int)position.getY());
    }

    private boolean canCallEmergencyMeeting(String gameId, Position position){
        return getMap(gameId).isMeetingPoint((int)position.getX(),(int)position.getY());
    }
    private Position calculateNewPosition(Position currentPosition, Direction direction, float speed) {
        float newX = currentPosition.getX() + direction.getDx() * speed;
        float newY = currentPosition.getY() + direction.getDy() * speed;
        return new Position(newX, newY);
    }

    public Player getPlayer(String gameId,String playerId) {
        return getPlayers(gameId).get(playerId);
    }

    public ConcurrentHashMap<String, Player> getPlayers(String gameId){
        return getGame(gameId).getPlayers();
    }

    public boolean playerExists(String gameId, String playerId) {
        return getPlayers(gameId).containsKey(playerId);
    }

    public void registerPlayer(String gameID, String playerId, Position startPosition, Colors color) {
        getPlayers(gameID).put(playerId, new Player(playerId, startPosition, color));
        //Draws the player on the map as soon as they enter the game
        // TODO: loop through all players here to draw them all
        messagingTemplate.convertAndSend("/topic/" +gameID+"/position", new PlayerPosition(playerId, startPosition, color.getHexValue()));

    }

    public String registerGame(String hostName) {
        String gameId=generateGameId();
        GameMap map=new LobbyMap(); //TODO:how to create/find/get map???
        games.put(gameId, new Game(gameId, map, hostName));
        return gameId;
    }

    private String generateGameId() {
        // unique game ID
        //TODO:find better method
        return UUID.randomUUID().toString();
    }

    public List<Position> getWallPositions(String gameId) {
        GameMap map =getMap(gameId);
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
    public Position getMeetingPositions(String gameId) {
        GameMap map =getMap(gameId);
        Position meetingPosition=new Position(-1,-1);
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.isMeetingPoint(x, y)) {
                    meetingPosition=new Position(x,y);
                }
            }
        }
        if(meetingPosition.getX()==-1)
            System.out.println("IS MEETING POINT IN MAP DOES NOT WORK");
        return meetingPosition;
    }

    public List<TaskPosition> getTaskPositions(String gameId){
        GameMap map = getMap(gameId);
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

    public Game getGame(String gameId) {
        return games.get(gameId);
    }
    public GameMap getMap(String gameId) {
        return getGame(gameId).getMap();
    }

    public boolean killPlayer(String gameId, String playerId) {;

        return true;
    }

    public boolean isAlive(String gameId, String payerId) {
        return games.get(gameId).getPlayers().get(payerId).isAlive();
    }

    public boolean isCrewmate(String gameId, String payerId) {

        return true; //TODO:check if player is Crewmate and return it
    }

    public void startGame(String gameId) {
        if(getGame(gameId).isRunning()) {
            return;
        }
        Settings gameSettings = getGame(gameId).getSettings();
        Set<String> playerKeys = getGame(gameId).getPlayers().keySet();
        HashMap<Integer, Integer> roles = gameSettings .getRoles();
        for(Integer key : roles.keySet()) {
             int i = 0;
            while(i < roles.get(key)) {
                int randomId = new Random().nextInt(playerKeys.size());
                String randomKey = playerKeys.toArray()[randomId].toString();
                if(getGame(gameId).getPlayers().get(randomKey).getRole() == 0) {
                    getGame(gameId).getPlayers().get(randomKey).setRole(key);
                    i++;
                }
            }
        }
        for(Player player : getGame(gameId).getPlayers().values()) {
            messagingTemplate.convertAndSend("/topic/" +gameId+ "/" + player.getId(), player.getRole());
            messagingTemplate.convertAndSend("/topic/" +gameId+"/position", new PlayerPosition(player.getId(), player.getPosition(), player.getColor().getHexValue()));
        }
        getGame(gameId).setGameMap(new PolusMap());
        getGame(gameId).setRunning(true);
    }
}
