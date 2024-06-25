package at.fhv.withthem.sabotages;

import at.fhv.withthem.sabotages.sabotage.InitTaskMessage;
import ch.qos.logback.core.joran.sanity.Pair;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @PostMapping("sabotages/loadAvailableSabotages")
    public void loadLobby(@RequestBody List<InitTaskMessage> initTaskMessages) throws JsonProcessingException {
        System.out.println(initTaskMessages.get(0).getLobby() + " -> " + _sabotageHandler.lobbyExists(initTaskMessages.get(0).getLobby()));
        if(!_sabotageHandler.lobbyExists(initTaskMessages.get(0).getLobby())) {
            ObjectMapper mapper = new ObjectMapper();
            for (InitTaskMessage initTaskMessage : initTaskMessages) {
                _sabotageHandler.addTaskToLobby(initTaskMessage.getLobby(), initTaskMessage.getType(), initTaskMessage.getId());
            }

            System.out.println(mapper.writeValueAsString(_sabotageHandler.getAvailableSabotages(initTaskMessages.get(0).getLobby())));

            sabotageInformation(initTaskMessages.get(0).getLobby());
        }
    }

    @MessageMapping("sabotages/requestInformation")
    public void sabotageInformation(@Payload String lobbyID) {
        System.out.println("requestSabotageInformation for " + lobbyID);

        if(!_sabotageHandler.lobbyExists(lobbyID)){
            System.out.println("Lobby " + lobbyID + " does not exist");
            return;
        }

        List<HashMap<Integer, String>> availableSabotages = new ArrayList<>();
        for (TaskMessage taskMessage : _sabotageHandler.getAvailableSabotages(lobbyID)){
            availableSabotages.add(new HashMap<>(Map.of(taskMessage.getId(), taskMessage.getTask())));
        }

        SabotageInformationMessage message = new SabotageInformationMessage(
                availableSabotages,
                (_sabotageHandler.getCurrentSabotage(lobbyID) == null) ? -1 : _sabotageHandler.getCurrentSabotage(lobbyID).getId(),
                _sabotageHandler.getTimerDuration_sec(lobbyID),
                _sabotageHandler.getTimerCooldown_sec(lobbyID)
        );

        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        _messagingTemplate.convertAndSend("/topic/sabotages/" + lobbyID + "/information", message);
    }

    @MessageMapping("sabotages/startSabotage")
    public void sabotage(@RequestBody StartSabotageMessage startSabotageMessage) {
        _sabotageHandler.startSabotage(startSabotageMessage.getGameId(),
                startSabotageMessage.getSabotageId(),
                () -> sabotageInformation(startSabotageMessage.getGameId()),
                () -> {
                    System.out.println("Reached timeout for " + startSabotageMessage.getGameId());
                    RestTemplate restTemplate = new RestTemplate();
                    String url = "http://localhost:4000/sabotageReachedTimeout/" + startSabotageMessage.getGameId();

                    // Make POST request to the endpoint
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> request = new HttpEntity<>(startSabotageMessage.getGameId(), headers);
                    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                });
    }
}
