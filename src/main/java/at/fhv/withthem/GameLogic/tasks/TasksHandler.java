package at.fhv.withthem.GameLogic.tasks;

import at.fhv.withthem.GameLogic.tasks.task.TaskConnectingWires;
import at.fhv.withthem.GameLogic.tasks.task.TaskFileDownloadUpload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import at.fhv.withthem.GameLogic.tasks.task.Task;

@Component
public class TasksHandler {
    private List<Task> _possibleTasks = new ArrayList<>(Arrays.asList(
            new TaskConnectingWires(),
            new TaskFileDownloadUpload()
    ));
    private HashMap<String, List<Task>> _availableTasks = new HashMap<>();
    private HashMap<String, List<Task>> _activeTasks = new HashMap<>();
    private HashMap<String, Integer> _finishedTasks = new HashMap<>();

    public void loadLobby(String lobby){
        _availableTasks.put(lobby, new ArrayList<>(_possibleTasks));
        _activeTasks.put(lobby, new ArrayList<>());
        _finishedTasks.put(lobby, 0);
    }

    public void startTask(String lobby, String task, String player){
        for(Task taskObj :  _availableTasks.get(lobby)){
            if(taskObj.getType().equals(task)){
                taskObj.setPlayer(player);
                _availableTasks.get(lobby).remove(taskObj);
                _activeTasks.get(lobby).add(taskObj);
                break;
            }
        }
    }

    public int playerAction(TaskMessage taskMessage) {
        for(Task task : _activeTasks.get(taskMessage.getLobby())) {
            if(task.getPlayer().equals(taskMessage.getPlayer())){
                return task.playerAction(taskMessage);
            }
        }

        return -1;
    }

    public TaskMessage getCurrentState(String lobby, String player){
        for(Task task : _activeTasks.get(lobby)){
            if(task.getPlayer().equals(player)){
                TaskMessage taskMessage =  task.getCurrentState();
                taskMessage.setLobby(lobby);
                taskMessage.setPlayer(player);
                return taskMessage;
            }
        }

        return null;
    }

    public void finishTask(String lobby, String task, String player){
        for(Task taskObj : _activeTasks.get(lobby)){
            if(taskObj.getType().equals(task) && taskObj.getPlayer().equals(player)){
                _activeTasks.get(lobby).remove(taskObj);
                _finishedTasks.put(lobby, _finishedTasks.get(lobby) + 1);
                break;
            }
        }
    }

    public void cancelTask(String lobby, String task, String player){
        for(Task taskObj : _activeTasks.get(lobby)) {
            if (taskObj.getType().equals(task) && taskObj.getPlayer().equals(player)) {
                _activeTasks.get(lobby).remove(taskObj);
                _availableTasks.get(lobby).add(taskObj);
                break;
            }
        }
    }

    public List<String> getAvailableTasks(String lobby){
        List<String> availableTasks = new ArrayList<>();

        for(Task task : _availableTasks.get(lobby)){
            availableTasks.add(task.getType());
        }

        return availableTasks;
    }
}
