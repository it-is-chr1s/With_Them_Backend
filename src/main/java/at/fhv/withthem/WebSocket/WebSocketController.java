package at.fhv.withthem.WebSocket;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public String broadcastMessage(String message) {
        return message;
    }
}

