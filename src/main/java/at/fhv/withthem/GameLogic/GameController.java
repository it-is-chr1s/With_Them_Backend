package at.fhv.withthem.GameLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
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
            gameService.registerPlayer(playerName, new Position(0, 0));
        }

        gameService.updatePlayerDirection(playerName, direction);
    }
    @MessageMapping("/requestMap")
    public void sendMapLayout() {
        List<Position> wallPositions = gameService.getWallPositions();
        Map<String, Object> mapLayout = new HashMap<>();
        mapLayout.put("wallPositions", wallPositions);
        mapLayout.put("width", gameService.getMap().getWidth());
        mapLayout.put("height", gameService.getMap().getHeight());

        messagingTemplate.convertAndSend("/topic/mapLayout", mapLayout);
    }

}
