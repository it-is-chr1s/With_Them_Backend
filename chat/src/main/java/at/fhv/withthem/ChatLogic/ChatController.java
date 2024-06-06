package at.fhv.withthem.ChatLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.Timestamp;
import java.util.HashMap;

import static java.lang.System.currentTimeMillis;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    private final SimpMessagingTemplate _messagingTemplate;

    private final ChatHandler _chatHandler;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatHandler chatHandler) {
        _messagingTemplate = messagingTemplate;
        _chatHandler = chatHandler;
    }
    @PostMapping("/chat/create")
    public void createChat(@RequestBody String gameId){
        _chatHandler.createChat(gameId);
    }

    @PostMapping("/chat/message")
    public void sandMessage(@RequestBody MessageRequest request){
        String gameId=request.getGameId();
        if(request.getAlive()) {
            Message m = new Message(gameId, request.getSander(), request.getContent());
            _chatHandler.addMessage(gameId, m);
            _messagingTemplate.convertAndSend("/topic/chat/" + gameId + "/message", m);
        }
        else
            System.out.println("Sander is dead");
    }
}
