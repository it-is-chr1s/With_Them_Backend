package at.fhv.withthem.tasks;

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

import java.util.List;

@Controller
@RestController
public class TasksController {

    private final SimpMessagingTemplate _messagingTemplate;

    private final TasksHandler _tasksHandler;

    @Autowired
    public TasksController(SimpMessagingTemplate messagingTemplate, TasksHandler tasksHandler) {
        _messagingTemplate = messagingTemplate;
        _tasksHandler = tasksHandler;
    }

    @PostMapping("/loadAvailableTasks")
    public void loadLobby(@RequestBody List<InitTaskMessage> initTaskMessages) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        for (InitTaskMessage initTaskMessage : initTaskMessages) {
            _tasksHandler.addTaskToLobby(initTaskMessage.getLobby(), initTaskMessage.getType(), initTaskMessage.getId());
        }

        System.out.println(mapper.writeValueAsString(_tasksHandler.getAvailableTasks(initTaskMessages.get(0).getLobby())));
        //_messagingTemplate.convertAndSend("/topic/tasks/availableTasks", _tasksHandler.getAvailableTasks(initTaskMessages.get(0).getLobby()));;
    }

    @MessageMapping("/startTask")
    public void startTask(TaskMessage taskMessage){
        _tasksHandler.startTask(taskMessage.getLobby(), taskMessage.getTask(), taskMessage.getPlayer());
        //sendBack to player current status
    }

    @MessageMapping("/playerAction")
    public void playerAction(TaskMessage taskMessage){
        _tasksHandler.playerAction(taskMessage);
    }

    @MessageMapping("/availableTasks")
    public void getAvailableTasks(@Payload String lobbyID){
        System.out.println("Available tasks requested for lobby: " + _tasksHandler.getAvailableTasks(lobbyID).toString());
    }

    //publish available tasks to the lobby


}
