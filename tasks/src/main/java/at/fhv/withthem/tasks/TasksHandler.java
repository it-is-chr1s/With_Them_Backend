package at.fhv.withthem.tasks;

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

    public void addTaskToLobby(String lobby, String taskType, int taskID){
        if(_availableTasks.get(lobby) == null) {
            _availableTasks.put(lobby, new ArrayList<>());
            _activeTasks.put(lobby, new ArrayList<>());
            _finishedTasks.put(lobby, 0);
        }

        for(Task possibleTask : _possibleTasks){
            if(possibleTask.equals(taskType) && _availableTasks.get(lobby).stream().noneMatch(task -> task.getId() == taskID)){
                possibleTask.setId(taskID);
                _availableTasks.get(lobby).add(possibleTask);
                break;
            }
        }
    }

    public void startTask(String lobby, int taskId, String player){
        for(Task taskObj :  _availableTasks.get(lobby)){
            if(taskObj.getId() == taskId){
                taskObj.setPlayer(player);
                _availableTasks.get(lobby).remove(taskObj);
                _activeTasks.get(lobby).add(taskObj);
                System.out.println("started Task: " + taskId);
                break;
            }
        }
    }

    public void playerAction(TaskMessage taskMessage) {
        for(Task task : _activeTasks.get(taskMessage.getLobby())) {
            if(task.getPlayer().equals(taskMessage.getPlayer())){
                task.playerAction(taskMessage);
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
                taskMessage.setId(task.getId());
                return taskMessage;
            }
        }

        return null;
    }

    public void finishTask(String lobby, int id){
        for(Task taskObj : _activeTasks.get(lobby)){
            if(taskObj.getId() == id){
                _activeTasks.get(lobby).remove(taskObj);
                _finishedTasks.put(lobby, _finishedTasks.get(lobby) + 1);
                break;
            }
        }
    }

    public void cancelTask(String lobby, int id){
        System.out.println("Cancel task: " + id);
        for(Task taskObj : _activeTasks.get(lobby)) {
            if (taskObj.getId() == id) {
                System.out.println("Success");
                taskObj.reset();
                _activeTasks.get(lobby).remove(taskObj);
                _availableTasks.get(lobby).add(taskObj);
                break;
            }
        }
    }

    public boolean taskCompleted(String lobby, String player){
        for(Task task : _activeTasks.get(lobby)){
            if(task.getPlayer().equals(player) && task.taskCompleted()){
                return true;
            }
        }

        return false;
    }

    public List<TaskMessage> getAvailableTasks(String lobby){
        List<TaskMessage> availableTasks = new ArrayList<>();

        for(Task task : _availableTasks.get(lobby)){
            availableTasks.add(new TaskMessage(task.getType(), task.getId()));
        }

        return availableTasks;
    }

    public List<TaskMessage> getActiveTasks(String lobby){
        List<TaskMessage> activeTasks = new ArrayList<>();

        for(Task task : _activeTasks.get(lobby)){
            activeTasks.add(new TaskMessage(task.getType(), task.getId()));
        }

        return activeTasks;
    }

    public int getFinishedTasks(String lobby){
        return _finishedTasks.get(lobby);
    }
}
