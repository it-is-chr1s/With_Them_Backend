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
import java.util.stream.Collectors;

@Service
public class GameService {
    private final ConcurrentHashMap<String, Game> _games = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public synchronized void updatePlayerDirection(String gameId, String playerId, Direction direction) {
        Player player = getPlayers(gameId).get(playerId);
        if (player != null) {
            player.setDirection(direction);
            player.setHasMoved(!direction.equals(Direction.NONE));
        }
    }

    public List<String> getOccupiedColors(String gameId) {
        Game game = _games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game with ID " + gameId + " not found.");
        }
        return game.getOccupiedColors().stream()
                .map(Colors::getHexValue)
                .collect(Collectors.toList());
    }
    public synchronized void updatePlayerColor(String gameId, String playerId, Colors color) {
        Player player = getPlayers(gameId).get(playerId);
        if (player != null && color!=player.getColor() && _games.get(gameId).icColorAvailable(color)) {
            player.setColor(color);
            messagingTemplate.convertAndSend("/topic/"+gameId+"/position", new PlayerPosition(playerId, player.getPosition(), color.getHexValue(), player.isAlive(), player.getDeathPosition()));
            messagingTemplate.convertAndSend("/topic/"+gameId+"/occupiedColors", getOccupiedColors(gameId));
        }
    }

    @Scheduled(fixedRate = 1)
    public void gameLoop() {

        _games.forEach((gameId, game) -> {
            ConcurrentHashMap<String, Player> players = game.getPlayers();
            players.forEach((playerId, player) -> {
                if (player.hasMoved()) {
                    Position currentPosition = player.getPosition();
                    Direction direction = player.getDirection();
                    float speed = 0.01f;

                    Position newPosition = calculateNewPosition(currentPosition, direction, speed);

                    if ((player.isAlive() && canMoveTo(gameId, newPosition)) || !player.isAlive() && canGhostMoveTo(gameId, newPosition)) {
                        player.setPosition(newPosition);
                        messagingTemplate.convertAndSend("/topic/" +gameId+"/position", new PlayerPosition(playerId, newPosition, player.getColor().getHexValue(), player.isAlive(), player.getDeathPosition()));
                        messagingTemplate.convertAndSend("/topic/" +gameId+"/player/" + playerId + "/controlsEnabled/task", canDoTask(gameId, player.getPosition()));
                        messagingTemplate.convertAndSend("/topic/" +gameId+"/player/" + playerId + "/controlsEnabled/emergencyMeeting", canCallEmergencyMeeting(gameId, player.getPosition()));
                        messagingTemplate.convertAndSend("/topic/" +gameId+"/player/" + playerId + "/controlsEnabled/emergencyMeetingReport", canCallEmergencyMeetingReport(gameId, player.getPosition()));
                    }
                }
            });
            if(game.isRunning()) {
                gameOver(gameId); //TODO: move to Kill and if player is voted in emergency meeting, game over
            }
        });
    }

    public void gameWon(String gameId){
        _games.get(gameId).setWon(true);
    }

    private int GameWonByPlayersAlive(String gameId) {
        Game game = getGame(gameId);
        int imposterAlive = 0;
        int crewAlive = 0;
        for(Player player : game.getPlayers().values()){
            if(player.isAlive()){
                if(player.getRole() == 0){
                    crewAlive++;
                }else{
                    imposterAlive++;
                }
            }
        }

        if(imposterAlive == 0){
            return 0;
        }else if(crewAlive == imposterAlive){
            return 1;
        } else if (imposterAlive > crewAlive) {
            return 1;
        }
        return -1;
    }

    private void gameOver(String gameId){
        int won = GameWonByPlayersAlive(gameId);
        gameOver(gameId, won);
    }

    public void gameOver(String gameId, int won){
        if(won == -1){
            return;
        }
        if(won == 0){
            gameWon(gameId);
            messagingTemplate.convertAndSend("/topic/"+gameId+"/gameOver", "Crewmate");
        }else if(won == 1){
            gameWon(gameId);
            messagingTemplate.convertAndSend("/topic/"+gameId+"/gameOver", "Imposter");
        }

        getGame(gameId).setRunning(false);
        getGame(gameId).getPlayers().values().forEach(player -> {player.setRole(0);  player.setAlive(true); player.setPosition(new Position(5,5));});
        getGame(gameId).setGameMap(new LobbyMap());
        List<Position> wallPositions = getWallPositions(gameId);
        List<TaskPosition> taskPositions = getTaskPositions(gameId);
        Map<String, Object> mapLayout = new HashMap<>();
        mapLayout.put("wallPositions", wallPositions);
        mapLayout.put("taskPositions", taskPositions);
        mapLayout.put("width", getMap(gameId).getWidth());
        mapLayout.put("height", getMap(gameId).getHeight());

        messagingTemplate.convertAndSend("/topic/" +gameId+"/mapLayout", mapLayout);
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

    private boolean canGhostMoveTo(String gameId, Position position) {
        GameMap map =getMap(gameId);
        return map.isWithinBounds((int)position.getX(), (int)position.getY());
    }

    private boolean canDoTask(String gameId, Position position){
        return getMap(gameId).isTask((int)position.getX(), (int)position.getY());
    }

    private boolean canCallEmergencyMeeting(String gameId, Position position){
        return getMap(gameId).isMeetingPoint((int)position.getX(),(int)position.getY());
    }

    private boolean canCallEmergencyMeetingReport(String gameId, Position position) {
        Boolean corpe= getPlayers(gameId).values().stream()
                .anyMatch(player -> ((int)position.getX()==(int)player.getDeathPosition().getX()&&(int)position.getY()==(int)player.getDeathPosition().getY()));
        System.out.println(corpe);
        return corpe;
    }

    private Position calculateNewPosition(Position currentPosition, Direction direction, float speed) {
        float newX = currentPosition.getX() + direction.getDx() * speed;
        float newY = currentPosition.getY() + direction.getDy() * speed;
        return new Position(newX, newY);
    }

    public ConcurrentHashMap<String, Player> getPlayers(String gameId){
        return getGame(gameId).getPlayers();
    }

    public boolean playerExists(String gameId, String playerId) {
        return getPlayers(gameId).containsKey(playerId);
    }

    public void registerPlayer(String gameID, String playerId) {
        Position startPosition = new Position(6f, 6f);
        Colors color=_games.get(gameID).getAvailableColor();
        getPlayers(gameID).put(playerId, new Player(playerId, startPosition, color));
        messagingTemplate.convertAndSend("/topic/"+gameID+"/occupiedColors", getOccupiedColors(gameID));
        //Draws the player on the map as soon as they enter the game
        getPlayers(gameID).forEach((id, player) -> {
            messagingTemplate.convertAndSend("/topic/" + gameID + "/position", new PlayerPosition(id, player.getPosition(), player.getColor().getHexValue(), player.isAlive(), player.getDeathPosition()));
        });
    }

    public String registerGame(String hostName) {
        String gameId=generateGameId();
        GameMap map=new LobbyMap();
        _games.put(gameId, new Game(gameId, map, hostName));
        messagingTemplate.convertAndSend("/topic/"+gameId+"/occupiedColors", getOccupiedColors(gameId));
        return gameId;
    }

    private String generateGameId() {
        int codeLength = 5;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = new Random().nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }
        return code.toString();
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

    public List<TaskPosition> getSabotagePositions(String gameId){
        GameMap map = getMap(gameId);
        List<TaskPosition> sabotagePositions = new ArrayList<>();
        for(int y = 0; y < map.getHeight(); y++){
            for(int x = 0; x < map.getWidth(); x++){
                if(map.isTask(x, y)){
                    TaskPosition taskPosition = new TaskPosition(x, y, map.getContent(x, y));
                    if(taskPosition.getIsSabotage()){
                        sabotagePositions.add(taskPosition);
                    }
                }
            }
        }
        return sabotagePositions;
    }

    public Game getGame(String gameId) {
        return _games.get(gameId);
    }
    public GameMap getMap(String gameId) {
        return getGame(gameId).getMap();
    }

    public boolean killPlayer(String gameId, String killerId) {
        Game session = getGame(gameId);
        if (session == null) return false;

        Player killer = session.getPlayers().get(killerId);
        if (killer == null || !killer.canKillAgain() || killer.getRole() != 1) return false;

        for (Player target : session.getPlayers().values()) {
            if (!target.getId().equals(killerId) && target.isAlive() && target.getRole() != 1) {
                if (isInKillRange(killer.getPosition(), target.getPosition())) {
                    target.kill();
                    killer.recordKill();
                    System.out.println("kill succesful");
                    messagingTemplate.convertAndSend("/topic/" + gameId + "/position", new PlayerPosition(target.getId(), target.getPosition(), target.getColor().toString(), target.isAlive(), target.getDeathPosition()));
                    return true;
                }
            }
        }
        return false;
    }

    public void kickOutPlayer(String gameId, String kickedOutPlayer){
        Player kickedOut=getGame(gameId).getPlayers().get(kickedOutPlayer);
        kickedOut.kill();
        int kickedOutPlayerRoll= kickedOut.getRole();
        System.out.println("Roll of kicked player was:"+ kickedOutPlayerRoll);
        if(kickedOutPlayerRoll==1) {
            System.out.println("/topic/" + gameId + "/kickedOutRoll");
            messagingTemplate.convertAndSend("/topic/" + gameId + "/kickedOutRoll", "Imposter");
        }        else
            messagingTemplate.convertAndSend("/topic/" + gameId + "/kickedOutRoll", "Crew Mate");
    }
    private boolean isInKillRange(Position killer, Position target) {
        int distance = (int)(Math.abs(killer.getX() - target.getX()) + Math.abs(killer.getY() - target.getY()));
        return distance <= 1;
    }


    public boolean isAlive(String gameId, String payerId) {
        return _games.get(gameId).getPlayers().get(payerId).isAlive();
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

        spawnPlayers(gameId);

        System.out.println("Game started!");
        getGame(gameId).setGameMap(new PolusMap());
        getGame(gameId).setRunning(true);
    }

    private void spawnPlayers(String gameId){
        final double distance = 1.5;
        final double centerX = 44.5;
        final double centerY = 11.5;
        final int n = getGame(gameId).getPlayers().values().size();
        final double radius = distance / (2 * Math.sin(Math.PI / n));
        int i = 0;
        for(Player player : getGame(gameId).getPlayers().values()) {
            double angle = (2 * Math.PI * i++ / n) - Math.PI / 2;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            player.setPosition(new Position((float) x, (float) y));
            messagingTemplate.convertAndSend("/topic/" + gameId + "/" + player.getId(), player.getRole());
            messagingTemplate.convertAndSend("/topic/" + gameId + "/position", new PlayerPosition(player.getId(), player.getPosition(), player.getColor().getHexValue(), player.isAlive(), player.getDeathPosition()));
        }
    }

    public void resetDeathPositions(String gameId) {
        Game game = _games.get(gameId);
        if (game != null) {
            game.getPlayers().values().forEach(player -> {
                player.setDeathPosition(new Position(-1, -1));
            });
            updateAllPlayers(gameId);
        }
    }

    private void updateAllPlayers(String gameId) {
        Game game = _games.get(gameId);
        game.getPlayers().forEach((id, player) -> {
            messagingTemplate.convertAndSend("/topic/" + gameId + "/position", new PlayerPosition(id, player.getPosition(), player.getColor().getHexValue(), player.isAlive(), player.getDeathPosition()));
        });
    }

    public Settings getSettings(String gameId) {
        return _games.get(gameId).getSettings();
    }

    public void setImposters(String gameId, int i) {
        _games.get(gameId).getSettings().getRoles().put(1, i);
    }

    public void setMaxPlayers(String gameId, int i) {
        _games.get(gameId).getSettings().setMaxPlayers(i);
    }
}
