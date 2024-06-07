package at.fhv.withthem.ChatLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@CrossOrigin(origins = {"http://localhost:5173/", "http://10.0.40.170:8080/"})
public class ChatController {
    private final ChatHandler _chatHandler;
    private final SimpMessagingTemplate messagingTemplate;


    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatHandler chatHandler, SimpMessagingTemplate messagingTemplate1) {
        _chatHandler = chatHandler;
        this.messagingTemplate = messagingTemplate1;
    }
    @PostMapping("/chat/create")
    public void createChat(@RequestBody String gameId){
        _chatHandler.createChat(gameId);
    }
    @MessageMapping("/chat/message")
    public void sendMessage(@RequestBody ChatMessage chatMessage) {
        System.out.println("GRR: "+chatMessage.get_gameId());
        System.out.println("GRR: "+chatMessage.get_sender());
        System.out.println("GRR: "+chatMessage.get_content());
        _chatHandler.addMessage(chatMessage.get_gameId(), chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/" + chatMessage.get_gameId() + "/message", chatMessage);
    }
    @GetMapping("/chat/messages/{gameId}")
    @ResponseBody
    public List<ChatMessage> getChatMessages(@PathVariable String gameId) {
        System.out.println("IN get all messages");
        return _chatHandler.getMessages(gameId);
    }
}
