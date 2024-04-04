package at.fhv.withthem.tasks;

import at.fhv.withthem.tasks.task.TaskCompletedListener;
import at.fhv.withthem.tasks.task.TaskConnectingWires;
import at.fhv.withthem.tasks.task.TaskFileDownloadUpload;
import at.fhv.withthem.tasks.task.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class TasksHandler {
    private final List<Task> _possibleTasks = new ArrayList<>(Arrays.asList(
            new TaskConnectingWires(),
            new TaskFileDownloadUpload()
    ));
    private final HashMap<String, List<Task>> _availableTasks = new HashMap<>();
    private final HashMap<String, List<Task>> _activeTasks = new HashMap<>();
    private final HashMap<String, Integer> _finishedTasks = new HashMap<>();

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

    public void playerAction(TaskMessage taskMessage) {
        for(Task task : _activeTasks.get(taskMessage.getLobby())) {
            if(task.getPlayer().equals(taskMessage.getPlayer())){
                task.playerAction(taskMessage, new TaskCompletedListener(){
                    @Override
                    public void taskCompleted() {
                        finishTask(taskMessage.getLobby(), taskMessage.getTask(), taskMessage.getPlayer());
                    }
                });
                break;
            }
        }
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

    private void finishTask(String lobby, String task, String player){
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

    public int getFinishedTasks(String lobby){
        return _finishedTasks.get(lobby);
    }
}
