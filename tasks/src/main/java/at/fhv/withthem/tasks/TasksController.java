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
        System.out.println(initTaskMessages.get(0).getLobby() + " -> " + _tasksHandler.lobbyExists(initTaskMessages.get(0).getLobby()));
        if(!_tasksHandler.lobbyExists(initTaskMessages.get(0).getLobby())) {
            ObjectMapper mapper = new ObjectMapper();
            for (InitTaskMessage initTaskMessage : initTaskMessages) {
                _tasksHandler.addTaskToLobby(initTaskMessage.getLobby(), initTaskMessage.getType(), initTaskMessage.getId());
            }

            System.out.println(mapper.writeValueAsString(_tasksHandler.getAvailableTasks(initTaskMessages.get(0).getLobby())));
            stateOfTasks(initTaskMessages.get(0).getLobby());
        }
    }

    @MessageMapping("tasks/requestStateOfTasks")
    public void stateOfTasks(@Payload String lobbyID) {
        System.out.println("requestStateOfTasks for " + lobbyID);

        if(!_tasksHandler.lobbyExists(lobbyID)){
            System.out.println("Lobby " + lobbyID + " does not exist");
            return;
        }

        List<TaskState> taskStates = new ArrayList<>();
        for (TaskMessage taskMessage : _tasksHandler.getAvailableTasks(lobbyID)){
            taskStates.add(new TaskState(taskMessage.getId(), "available", taskMessage.getTask()));
        }
        for (TaskMessage taskMessage : _tasksHandler.getActiveTasks(lobbyID)){
            taskStates.add(new TaskState(taskMessage.getId(), "active", taskMessage.getTask()));
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(taskStates));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        _messagingTemplate.convertAndSend("/topic/tasks/" + lobbyID + "/stateOfTasks", taskStates);
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
        _messagingTemplate.convertAndSend("/topic/tasks/" + taskMessage.getLobby() + "/currentTask/" + taskMessage.getPlayer(), _tasksHandler.getCurrentState(taskMessage.getLobby(), taskMessage.getPlayer()));
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
                _messagingTemplate.convertAndSend("/topic/tasks/" + taskMessage.getLobby() + "/currentTask/" + taskMessage.getPlayer(), _tasksHandler.getCurrentState(taskMessage.getLobby(), taskMessage.getPlayer()));
            }
        });
    }

    @MessageMapping("tasks/closeTask")
    public void cancelTask(TaskMessage taskMessage){
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println(mapper.writeValueAsString(taskMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if(_tasksHandler.taskCompleted(taskMessage.getLobby(), taskMessage.getId())){
            _tasksHandler.finishTask(taskMessage.getLobby(), taskMessage.getId());
        }else {
            _tasksHandler.cancelTask(taskMessage.getLobby(), taskMessage.getId());
        }

        stateOfTasks(taskMessage.getLobby());
        _messagingTemplate.convertAndSend("/topic/tasks/" + taskMessage.getLobby() + "/currentTask/" + taskMessage.getPlayer(), "");
    }

    @MessageMapping("tasks/sabotage")
    public void sabotage(){
        System.out.println("sabotage");
    }


}
