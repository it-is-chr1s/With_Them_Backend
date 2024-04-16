package at.fhv.withthem.GameLogic;

import at.fhv.withthem.GameLogic.Requests.ChangeColorRequest;
import at.fhv.withthem.GameLogic.Requests.MoveRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate messagingTemplate;

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
        String playerName = moveRequest.getName();
        Direction direction = moveRequest.getDirection();

        if (!gameService.playerExists(playerName)) {
            gameService.registerPlayer(playerName, new Position(0, 0), Colors.GRAY);/*, colore*/
        }

        gameService.updatePlayerDirection(playerName, direction);
    }
    @MessageMapping("/changeColor")
    public void handleColorChange(ChangeColorRequest colorRequest) {
        String playerName = colorRequest.get_name();
        Colors color = colorRequest.get_color();

        if (!gameService.playerExists(playerName)) {
            gameService.registerPlayer(playerName, new Position(0, 0), Colors.GRAY);/*, colore*/
        }

        gameService.updatePlayerColor(playerName, color);
    }
    @MessageMapping("/requestMap")
    public void sendMapLayout() {
        List<Position> wallPositions = gameService.getWallPositions();
        List<TaskPosition> taskPositions = gameService.getTaskPositions();
        Map<String, Object> mapLayout = new HashMap<>();
        mapLayout.put("wallPositions", wallPositions);
        mapLayout.put("taskPositions", taskPositions);
        mapLayout.put("width", gameService.getMap().getWidth());
        mapLayout.put("height", gameService.getMap().getHeight());

        messagingTemplate.convertAndSend("/topic/mapLayout", mapLayout);
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
