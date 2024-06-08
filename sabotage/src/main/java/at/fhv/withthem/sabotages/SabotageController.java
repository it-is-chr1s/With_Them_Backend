package at.fhv.withthem.sabotages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;


@Controller
@RestController
public class SabotageController {

    private final SimpMessagingTemplate _messagingTemplate;
    private final SabotageHandler _sabotageHandler;

    @Autowired
    public SabotageController(SimpMessagingTemplate messagingTemplate, SabotageHandler sabotageHandler) {
        _messagingTemplate = messagingTemplate;
        _sabotageHandler = sabotageHandler;
    }

    @MessageMapping("tasks/sabotage/start")
    public void sabotage(@Payload String lobbyID){
        _sabotageHandler.startSabotage(time -> {
            _messagingTemplate.convertAndSend("/topic/tasks/" + lobbyID + "/sabotage/timer", time);
        }, time -> {
            _messagingTemplate.convertAndSend("/topic/tasks/" + lobbyID + "/sabotage/cooldown", time);
        });
    }
}
