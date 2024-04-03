package at.fhv.withthem.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TasksController {

    private final SimpMessagingTemplate _messagingTemplate;

    private final TasksHandler _tasksHandler;

    @Autowired
    public TasksController(SimpMessagingTemplate messagingTemplate, TasksHandler tasksHandler) {
        _messagingTemplate = messagingTemplate;
        _tasksHandler = tasksHandler;
    }
    @MessageMapping("/startTask")
    public void startTask(TaskMessage taskMessage){
        _tasksHandler.startTask(taskMessage.getLobby(), taskMessage.getTask(), taskMessage.getPlayer());
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
