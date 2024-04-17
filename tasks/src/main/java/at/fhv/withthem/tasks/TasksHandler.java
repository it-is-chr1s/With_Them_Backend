package at.fhv.withthem.tasks;

import at.fhv.withthem.tasks.task.Reaction;
import at.fhv.withthem.tasks.task.TaskConnectingWires;
import at.fhv.withthem.tasks.task.TaskFileDownloadUpload;
import at.fhv.withthem.tasks.task.Task;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TasksHandler {
    private final HashMap<String, List<Task>> _availableTasks = new HashMap<>();
    private final HashMap<String, List<Task>> _activeTasks = new HashMap<>();
    private final HashMap<String, Integer> _finishedTasks = new HashMap<>();
    private final Set<String> _initializedLobbies = new HashSet<>();

    public void addTaskToLobby(String lobby, String taskType, int taskID){
        if(_availableTasks.get(lobby) == null) {
            _availableTasks.put(lobby, new ArrayList<>());
            _activeTasks.put(lobby, new ArrayList<>());
            _finishedTasks.put(lobby, 0);

            _initializedLobbies.add(lobby);
        }

        if(_availableTasks.get(lobby).stream().noneMatch(task -> task.getId() == taskID)){
            if(taskType.equals("Connecting Wires")) {
                _availableTasks.get(lobby).add(new TaskConnectingWires(taskID));
                System.out.println("Added Task Connecting Wires: " + taskID);
            }else if(taskType.equals("File Upload") || taskType.equals("File Download")){
                _availableTasks.get(lobby).add(new TaskFileDownloadUpload(taskID));
                System.out.println("Added Task File Upload/Download: " + taskID);
            }
        }
    }

    public boolean lobbyExists(String lobbyId){
        return _initializedLobbies.contains(lobbyId);
    }

    public void startTask(String lobby, int taskId, String player){
        Iterator<Task> iterator = _availableTasks.get(lobby).iterator();
        while (iterator.hasNext()) {
            Task taskObj = iterator.next();
            if (taskObj.getId() == taskId) {
                taskObj.setPlayer(player);
                System.out.println(taskObj.getPlayer());
                iterator.remove();
                _activeTasks.get(lobby).add(taskObj);
                System.out.println("Started Task: " + taskId);
                break;
            }
        }
    }

    public void playerAction(TaskMessage taskMessage, Reaction reaction) {
        for(Task task : _activeTasks.get(taskMessage.getLobby())) {
            if(task.getPlayer().equals(taskMessage.getPlayer())){
                task.playerAction(taskMessage, reaction);
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
        Iterator<Task> iterator = _activeTasks.get(lobby).iterator();
        while (iterator.hasNext()) {
            Task taskObj = iterator.next();
            if (taskObj.getId() == id) {
                System.out.println("Finish task: " + id);
                iterator.remove();
                _finishedTasks.put(lobby, _finishedTasks.get(lobby) + 1);
                break;
            }
        }
    }

    public void cancelTask(String lobby, int id){
        System.out.println("Cancel task: " + id);
        Iterator<Task> iterator = _activeTasks.get(lobby).iterator();
        while (iterator.hasNext()) {
            Task taskObj = iterator.next();
            if (taskObj.getId() == id) {
                System.out.println("Success");
                taskObj.reset();
                iterator.remove();
                _availableTasks.get(lobby).add(taskObj);
                break;
            }
        }
    }

    public boolean taskCompleted(String lobby, int id){
        for(Task task : _activeTasks.get(lobby)){
            if(task.getId() == id && task.taskCompleted()){
                System.out.println("Task completed: " + id);
                return true;
            }
        }
        System.out.println("Task not completed: " + id);
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
