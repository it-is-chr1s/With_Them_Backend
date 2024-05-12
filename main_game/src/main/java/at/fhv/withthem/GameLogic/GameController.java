package at.fhv.withthem.GameLogic;

import at.fhv.withthem.GameLogic.Requests.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "http://localhost:5173")
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

    @RequestMapping(method= RequestMethod.POST, value="/createGame")
    public ResponseEntity<String> createGame(@RequestParam("hostName") String requestBody) {
        //@PostMapping("/createGame")
        //public ResponseEntity<String> createGame(@RequestBody HostNameRequest requestBody) {
        System.out.println(requestBody);
        String gameId = gameService.registerGame(requestBody);
        System.out.println("HOAST:"+gameService.getGame(gameId).getHost());



        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @PostMapping("/TasksFinished")
    public ResponseEntity<String> tasksFinished(@RequestBody String gameId) {
        gameService.gameWon(gameId);
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @PostMapping("/startGame")
    public ResponseEntity<String> startGame(@RequestBody String gameId) {
        gameService.startGame(gameId);
        loadTasks(gameId, gameService.getTaskPositions(gameId));
        loadEmergencyMeeting(gameId,gameService.getPlayers(gameId));
        sendMapLayout(new MapRequest(gameId));
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @Autowired
    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/move")
    public void handleMove(MoveRequest moveRequest) {
        String gameId=moveRequest.getGameId();
        String playerName = moveRequest.getName();
        Direction direction = moveRequest.getDirection();

        if (!gameService.playerExists(gameId, playerName)) {
            gameService.registerPlayer(gameId, playerName, new Position(0, 0), Colors.GRAY);
        }

        gameService.updatePlayerDirection(gameId, playerName, direction);
    }

    @MessageMapping("/changeColor")
    public void handleColorChange(ChangeColorRequest colorRequest) {
        String gameId=colorRequest.getGameId();
        String playerName = colorRequest.getName();
        Colors color = colorRequest.getColor();

        if (!gameService.playerExists(gameId, playerName)) {
            gameService.registerPlayer(gameId, playerName, new Position(0, 0), Colors.GRAY);/*, colore*/
        }

        gameService.updatePlayerColor(gameId, playerName, color);
    }
    @MessageMapping("/requestMap")
    public void sendMapLayout(MapRequest mapRequest) {
        String gameId = mapRequest.getGameId();
        List<Position> wallPositions = gameService.getWallPositions(gameId);
        List<TaskPosition> taskPositions = gameService.getTaskPositions(gameId);
        Position meetingPosition = gameService.getMeetingPositions(gameId);
        Map<String, Object> mapLayout = new HashMap<>();
        mapLayout.put("wallPositions", wallPositions);
        mapLayout.put("taskPositions", taskPositions);
        mapLayout.put("width", gameService.getMap(gameId).getWidth());
        mapLayout.put("height", gameService.getMap(gameId).getHeight());
        mapLayout.put("meetingPosition", meetingPosition);

        messagingTemplate.convertAndSend("/topic/" +gameId+"/mapLayout", mapLayout);
    }

    public void loadTasks(String lobbyID, List<TaskPosition> taskPositions) {
        List<InitTaskMessage> initTaskMessages = new ArrayList<>();
        for (TaskPosition taskPosition : taskPositions) {
            initTaskMessages.add(new InitTaskMessage(lobbyID, taskPosition.getTaskType(), taskPosition.getId()));
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(initTaskMessages));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //messagingTemplate.convertAndSend("/tasks/loadAvailableTasks", initTaskMessages);

        String url = "http://localhost:4001/loadAvailableTasks";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<InitTaskMessage>> requestEntity = new HttpEntity<>(initTaskMessages, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }
    @GetMapping("/game/{gameId}/players")
    @ResponseBody
    public PlayersRequest getPlayers(@PathVariable String gameId) {
        Collection<Player> allPlayers = gameService.getGame(gameId).getPlayers().values();
        List<Player> alivePlayers = allPlayers.stream().filter(Player::isAlive).toList();
        List<Player> deadPlayers = allPlayers.stream().filter(player -> !player.isAlive()).toList();

        List<PlayerInfo> alivePlayerInfo = alivePlayers.stream()
                .map(player -> new PlayerInfo(player.getId(), player.getColor().getHexValue())).collect(Collectors.toList());

        return new PlayersRequest(alivePlayerInfo, deadPlayers.stream().map(Player::getId).collect(Collectors.toList()));
    }
    @MessageMapping("/kill")
    public void handleKill(KillRequest killRequest) {
        String gameId = killRequest.getGameId();
        String killerId = killRequest.getKillerId();

        System.out.println("kill request. KillerID: " + killerId + "    game id: " + gameId);

        boolean success = gameService.killPlayer(gameId, killerId);
        if(success)
            loadEmergencyMeeting(gameId,gameService.getPlayers(gameId));

       /* if (success) {
            messagingTemplate.convertAndSend("/topic/" + gameId + "/kill", "Player " + killerId + " made a kill");
        } else {
            messagingTemplate.convertAndSend("/topic/" + gameId + "/killFailed", "Kill failed for player " + killerId);
        }*/
    }
    public void loadEmergencyMeeting(String gameId, ConcurrentHashMap<String, Player> players){
        List<String>alivePlayers=new LinkedList<>();
        players.forEach((playerId, player) -> {
            System.out.println("Players:"+ player.getName());
            if(player.isAlive()){
                alivePlayers.add(player.getName());
            }
        });
        LoadEmergencyMeetingMessage loadEmergencyMeetingMessages=new LoadEmergencyMeetingMessage(gameId,alivePlayers);
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("Message to EmergencyMeeting:"+mapper.writeValueAsString(loadEmergencyMeetingMessages));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String url = "http://localhost:4002/loadEmergencyMeeting";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoadEmergencyMeetingMessage> requestEntity = new HttpEntity<>(loadEmergencyMeetingMessages, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }
}
