package at.fhv.withthem.MeetingLogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
@Controller
@RestController
public class EmergencyMeetingController {

    private final SimpMessagingTemplate _messagingTemplate;

    private final EmergencyMeetingHandler _emergencyMeetingHandler;

    @Autowired
    public EmergencyMeetingController(SimpMessagingTemplate messagingTemplate, EmergencyMeetingHandler emergencyMeetingHandler) {
        _messagingTemplate = messagingTemplate;
        _emergencyMeetingHandler = emergencyMeetingHandler;
    }

    @PostMapping("/loadEmergencyMeeting")
    public void loadLobby(@RequestBody LoadEmergencyMeetingMessage loadEmergencyMeetingMessage) throws JsonProcessingException {

        if(!_emergencyMeetingHandler.meetingExists(loadEmergencyMeetingMessage.get_gameId())){
            _emergencyMeetingHandler.createMeeting(loadEmergencyMeetingMessage.get_gameId(),loadEmergencyMeetingMessage.get_names());
        }
        else
        {
            _emergencyMeetingHandler.updateMeeting(loadEmergencyMeetingMessage.get_gameId(),loadEmergencyMeetingMessage.get_names());
        }
    }

  /*  @MessageMapping("meeting/requestStateOfPlayers")
    public void stateOfPlayers(@Payload String lobbyID) {
        System.out.println("requestStateOfTasks for " + lobbyID);

        HashMap<Integer, String> tasks = new HashMap<>();
        for (EmergencyMeetingMessage emergencyMeetingMessage : _emergencyMeetingHandler.getAvailableTasks(lobbyID)) {
            tasks.put(emergencyMeetingMessage.getId(), "available");
        }
        for (EmergencyMeetingMessage emergencyMeetingMessage : _emergencyMeetingHandler.getActiveTasks(lobbyID)) {
            tasks.put(emergencyMeetingMessage.getId(), "active");
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(tasks));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        _messagingTemplate.convertAndSend("/topic/meeting/" + lobbyID + "/stateOfTasks", tasks);
    }

    @MessageMapping("meeting/start")
    public void startTask(EmergencyMeetingMessage emergencyMeetingMessage) {

       ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("Task started: " + mapper.writeValueAsString(emergencyMeetingMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        _emergencyMeetingHandler.startTask(emergencyMeetingMessage.getLobby(), emergencyMeetingMessage.getId(), emergencyMeetingMessage.getPlayer());
        stateOfPlayers(emergencyMeetingMessage.getLobby());
        _messagingTemplate.convertAndSend("/topic/meeting/" + emergencyMeetingMessage.getLobby() + "/currentTask/" + emergencyMeetingMessage.getPlayer(), _emergencyMeetingHandler.getCurrentState(emergencyMeetingMessage.getLobby(), emergencyMeetingMessage.getPlayer()));
    }

    @MessageMapping("/meeting/playerAction")
    public void playerAction(EmergencyMeetingMessage emergencyMeetingMessage) {
        _emergencyMeetingHandler.playerAction(emergencyMeetingMessage, new Reaction() {
            @Override
            public void react() {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    System.out.println(mapper.writeValueAsString(_emergencyMeetingHandler.getCurrentState(emergencyMeetingMessage.getLobby(), emergencyMeetingMessage.getPlayer())));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                _messagingTemplate.convertAndSend("/topic/meeting/" + emergencyMeetingMessage.getLobby() + "/currentTask/" + emergencyMeetingMessage.getPlayer(), _emergencyMeetingHandler.getCurrentState(emergencyMeetingMessage.getLobby(), emergencyMeetingMessage.getPlayer()));
            }
        });
    }

    @MessageMapping("meeting/end")
    public void cancelTask(EmergencyMeetingMessage emergencyMeetingMessage) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(emergencyMeetingMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (_emergencyMeetingHandler.taskCompleted(emergencyMeetingMessage.getLobby(), emergencyMeetingMessage.getId())) {
            _emergencyMeetingHandler.finishTask(emergencyMeetingMessage.getLobby(), emergencyMeetingMessage.getId());
        } else {
            _emergencyMeetingHandler.cancelTask(emergencyMeetingMessage.getLobby(), emergencyMeetingMessage.getId());
        }

        stateOfPlayers(emergencyMeetingMessage.getLobby());
        _messagingTemplate.convertAndSend("/topic/meeting/" + emergencyMeetingMessage.getLobby() + "/currentTask/" + emergencyMeetingMessage.getPlayer(), "");
    }
*/

}
