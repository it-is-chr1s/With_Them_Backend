package at.fhv.withthem.tasks;

import at.fhv.withthem.tasks.task.Reaction;
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

import java.util.ArrayList;
import java.util.HashMap;
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
        stateOfTasks(initTaskMessages.get(0).getLobby());
    }
/*
    @MessageMapping("tasks/requestAvailableTasks")
    public void sendAvailableTasks(@Payload String lobbyID) {
        System.out.println("Available tasks requested for lobby: " + lobbyID);
        _messagingTemplate.convertAndSend("/topic/tasks/availableTasks", _tasksHandler.getAvailableTasks(lobbyID));
    }*/

    @MessageMapping("tasks/requestStateOfTasks")
    public void stateOfTasks(@Payload String lobbyID) {
        HashMap<Integer, String> tasks = new HashMap<>();
        for (TaskMessage taskMessage : _tasksHandler.getAvailableTasks(lobbyID)){
            tasks.put(taskMessage.getId(), "available");
        }
        for (TaskMessage taskMessage : _tasksHandler.getActiveTasks(lobbyID)){
            tasks.put(taskMessage.getId(), "active");
        }

        _messagingTemplate.convertAndSend("/topic/tasks/stateOfTasks", tasks);
    }

    @MessageMapping("tasks/startTask")
    public void startTask(TaskMessage taskMessage){
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("Task started: " + mapper.writeValueAsString(taskMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        _tasksHandler.startTask(taskMessage.getLobby(), taskMessage.getId(), taskMessage.getPlayer());
        stateOfTasks(taskMessage.getLobby());
        _messagingTemplate.convertAndSend("/topic/tasks/currentTask/" + taskMessage.getPlayer(), _tasksHandler.getCurrentState(taskMessage.getLobby(), taskMessage.getPlayer()));
    }

    @MessageMapping("/tasks/playerAction")
    public void playerAction(TaskMessage taskMessage){
        _tasksHandler.playerAction(taskMessage, new Reaction(){
            @Override
            public void react(){
                ObjectMapper mapper = new ObjectMapper();
                try {
                    System.out.println(mapper.writeValueAsString(_tasksHandler.getCurrentState(taskMessage.getLobby(), taskMessage.getPlayer())));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                _messagingTemplate.convertAndSend("/topic/tasks/currentTask/" + taskMessage.getPlayer(), _tasksHandler.getCurrentState(taskMessage.getLobby(), taskMessage.getPlayer()));
            }
        });
    }

    @MessageMapping("/availableTasks")
    public void getAvailableTasks(@Payload String lobbyID){
        System.out.println("Available tasks requested for lobby: " + _tasksHandler.getAvailableTasks(lobbyID).toString());
    }

    @MessageMapping("tasks/closeTask")
    public void cancelTask(TaskMessage taskMessage){
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(taskMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(_tasksHandler.taskCompleted(taskMessage.getLobby(), taskMessage.getPlayer())){
            _tasksHandler.finishTask(taskMessage.getLobby(), taskMessage.getId());
        }else {
            _tasksHandler.cancelTask(taskMessage.getLobby(), taskMessage.getId());
        }

        stateOfTasks(taskMessage.getLobby());
        _messagingTemplate.convertAndSend("/topic/tasks/currentTask/" + taskMessage.getPlayer(), "");
    }

    //publish available tasks to the lobby


}
