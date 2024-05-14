package at.fhv.withthem.MeetingLogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
            _emergencyMeetingHandler.createMeeting(loadEmergencyMeetingMessage.get_gameId(),loadEmergencyMeetingMessage.get_name());
            System.out.println("Created:"+loadEmergencyMeetingMessage.get_gameId());
        }
        else
        {
            _emergencyMeetingHandler.updateLivePlayers(loadEmergencyMeetingMessage.get_gameId(),loadEmergencyMeetingMessage.get_name());
            System.out.println("Updated:"+loadEmergencyMeetingMessage.get_gameId());
        }
        System.out.println(String.join(" , ", loadEmergencyMeetingMessage.get_name()));
    }
    @MessageMapping("meeting/startMeeting")
    public void startMeeting(@Payload String gameId) {
        System.out.println("Started for " + gameId);
        //_messagingTemplate.convertAndSend("/topic/meeting/" + gameId + "/startable", true);
        boolean running=_emergencyMeetingHandler.startMeeting(gameId);
        _messagingTemplate.convertAndSend("/topic/meeting/" + gameId + "/running", running);
    }
    @MessageMapping("meeting/endMeeting")
    public void endMeeting(@Payload String gameId) {
        System.out.println("Ended for " + gameId);
        //_messagingTemplate.convertAndSend("/topic/meeting/" + gameId + "/startable", true);
        boolean running=_emergencyMeetingHandler.endMeeting(gameId);
        _messagingTemplate.convertAndSend("/topic/meeting/" + gameId + "/running", running);
    }

    @GetMapping("/meeting/{gameId}/startable")
    @ResponseBody
    public boolean getStartable(@PathVariable String gameId) {
        return _emergencyMeetingHandler.getStartable(gameId);
    }


    @PostMapping("/meeting/startVoting")
    public void startVoting(@RequestBody String gameId){
        _emergencyMeetingHandler.startVoting(gameId);
    }
    @PostMapping("/meeting/vote")
    public ResponseEntity<String> vote(@RequestBody VoteRequest request) {
        String gameId = request.getGameId();
        String voter = request.getVoter();
        String nominated = request.getNominated();

        String suspect=_emergencyMeetingHandler.vote(gameId, voter, nominated);
        return new ResponseEntity<>(suspect, HttpStatus.OK);
    }
    @GetMapping("/meeting/{gameId}/suspect")
    @ResponseBody
    public String getSuspect(@PathVariable String gameId) {
        System.out.println(_emergencyMeetingHandler.getSuspect(gameId));
        return _emergencyMeetingHandler.getSuspect(gameId);
    }
}
