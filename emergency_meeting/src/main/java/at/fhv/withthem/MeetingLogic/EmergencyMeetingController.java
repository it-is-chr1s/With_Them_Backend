package at.fhv.withthem.MeetingLogic;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Controller
@RestController
@CrossOrigin(origins = {"http://localhost:5173/", "http://10.0.40.170:8080/"})
public class EmergencyMeetingController {

    private final SimpMessagingTemplate _messagingTemplate;

    private final EmergencyMeetingHandler _emergencyMeetingHandler;

    private final RestTemplate restTemplate = new RestTemplate();

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
        boolean running=_emergencyMeetingHandler.startMeeting(gameId);
        _messagingTemplate.convertAndSend("/topic/meeting/" + gameId + "/running", running);
    }
    @MessageMapping("meeting/endMeeting")
    public void endMeeting(@Payload String gameId) {
        System.out.println("Ended for " + gameId);
        boolean running=_emergencyMeetingHandler.endMeeting(gameId);
        _messagingTemplate.convertAndSend("/topic/meeting/" + gameId + "/running", running);

        // reset corpse positions
        String gameServiceUrl = "http://localhost:4000/meetingEnded/" + gameId;
        try {
            restTemplate.getForEntity(gameServiceUrl, Void.class);
        } catch (RestClientException e) {
            System.out.println("Error when calling game service: " + e.getMessage());
        }
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
        if(suspect!=null){
            _messagingTemplate.convertAndSend("/topic/meeting/" + gameId + "/suspect", suspect);
            kickOut(gameId,suspect);
        }

        return new ResponseEntity<>(suspect, HttpStatus.OK);
    }
    @GetMapping("/meeting/{gameId}/suspect")
    @ResponseBody
    public String getSuspect(@PathVariable String gameId) {
        String suspect=_emergencyMeetingHandler.getSuspect(gameId);
        _messagingTemplate.convertAndSend("/topic/meeting/" + gameId + "/suspect", suspect);
        kickOut(gameId,suspect);
        return suspect;
    }

    public void kickOut(String gameId, String suspect) {
        String killRequest = "{\n" +
                "  \"gameId\": \"" + gameId + "\",\n" +
                "  \"killerId\": \"" + suspect + "\"\n" +
                "}";
        String url = "http://localhost:4000/kickOut";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(killRequest, headers);
        restTemplate.postForEntity(url, requestEntity, String.class);
    }

}
