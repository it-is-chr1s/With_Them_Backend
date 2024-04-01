package at.fhv.withthem.GameLogic.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TasksController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TasksController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @MessageMapping("/startTask")
    public void startTask(StartTaskRequest startTaskRequest){
        System.out.println("Task started" + startTaskRequest.getTask() + " " + startTaskRequest.getLobby());
        //messagingTemplate.convertAndSend("/topic/task", "Task started");
    }

    @MessageMapping("/availableTasks")
    public void getAvailableTasks(@Payload String lobbyID){
        System.out.println("Available tasks requested for lobby: " + lobbyID);
        //messagingTemplate.convertAndSend("/topic/availableTasks", "Available tasks requested");
    }

    //publish available tasks to the lobby


}
