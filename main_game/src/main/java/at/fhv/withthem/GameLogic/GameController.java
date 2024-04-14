package at.fhv.withthem.GameLogic;

import at.fhv.withthem.GameLogic.Requests.ChangeColorRequest;
import at.fhv.withthem.GameLogic.Requests.MoveRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/createGame")
    public ResponseEntity<String> createGame() {
        // Call the method to register a new game
        String gameId = gameService.registerGame();
        return new ResponseEntity<>(gameId, HttpStatus.OK);
    }

    @Autowired
    public GameController(GameService gameService, SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    /*

    @MessageMapping("/move")
    public void handleMove(MoveRequest moveRequest) {

        String playerName = moveRequest.getName();
        Direction direction = moveRequest.getDirection();

        System.out.println("moving player");
        System.out.println(playerName);
        System.out.println(direction);

        if (!gameService.playerExists(playerName)) {
            gameService.registerPlayer(playerName, new Position(0, 0));
        }

        boolean success = gameService.movePlayer(playerName, direction, 0.01f);
        if (success) {
            Player player = gameService.getPlayer(playerName);
            messagingTemplate.convertAndSend("/topic/position", new PlayerPosition(playerName, player.getPosition()));
        }
    }


    */
    @MessageMapping("/move")
    public void handleMove(MoveRequest moveRequest) {
        String gameId=moveRequest.getGameId();
        String playerName = moveRequest.getName();
        Direction direction = moveRequest.getDirection();

        if (!gameService.playerExists(gameId, playerName)) {
            gameService.registerPlayer(gameId,playerName, new Position(0, 0), Colors.GRAY);/*, colore*/
        }

        gameService.updatePlayerDirection(gameId,playerName, direction);
    }
    @MessageMapping("/changeColor")
    public void handleColorChange(ChangeColorRequest colorRequest) {
        String gameId=colorRequest.getGameId();
        String playerName = colorRequest.getName();
        Colors color = colorRequest.getColor();

        if (!gameService.playerExists(gameId,playerName)) {
            gameService.registerPlayer(gameId,playerName, new Position(0, 0), Colors.GRAY);/*, colore*/
        }

        gameService.updatePlayerColor(gameId, playerName, color);
    }
    @MessageMapping("/requestMap")
    public void sendMapLayout(String gameId) {
        List<Position> wallPositions = gameService.getWallPositions(gameId);
        List<TaskPosition> taskPositions = gameService.getTaskPositions();//TODO:provide gameId
        Map<String, Object> mapLayout = new HashMap<>();
        mapLayout.put("wallPositions", wallPositions);
        mapLayout.put("taskPositions", taskPositions);
        mapLayout.put("width", gameService.getMap(gameId).getWidth());
        mapLayout.put("height", gameService.getMap(gameId).getHeight());

        messagingTemplate.convertAndSend("/topic/" +gameId+"/mapLayout", mapLayout);
    }

    public void loadTasks(String lobbyID, List<TaskPosition> taskPositions) throws JsonProcessingException {
        List<InitTaskMessage> initTaskMessages = new ArrayList<>();
        for (TaskPosition taskPosition : taskPositions) {
            initTaskMessages.add(new InitTaskMessage(lobbyID, taskPosition.getTaskType(), taskPosition.getId()));
        }
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(initTaskMessages));
        //messagingTemplate.convertAndSend("/tasks/loadAvailableTasks", initTaskMessages);

        String url = "http://localhost:4001/loadAvailableTasks";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<InitTaskMessage>> requestEntity = new HttpEntity<>(initTaskMessages, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }

}
