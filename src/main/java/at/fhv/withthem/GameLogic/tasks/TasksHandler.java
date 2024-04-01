package at.fhv.withthem.GameLogic.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TasksHandler {
    private List<String> possibleTasks= new ArrayList<>();
    private HashMap<String, List<String>> availableTasks = new HashMap<>();
    private HashMap<String, List<String>> activeTasks = new HashMap<>();
    private HashMap<String, Integer> finishedTasks = new HashMap<>();

    public void registerTask(String task){
        possibleTasks.add(task);
    }

    public void loadLobby(String lobby){
        availableTasks.put(lobby, new ArrayList<>(possibleTasks));
        activeTasks.put(lobby, new ArrayList<>());
        finishedTasks.put(lobby, 0);
    }

    public void startTask(String lobby, String task){
        availableTasks.get(lobby).remove(task);
        activeTasks.get(lobby).add(task);
    }

    public void finishTask(String lobby, String task){
        activeTasks.get(lobby).remove(task);
        finishedTasks.put(lobby, finishedTasks.get(lobby) + 1);
    }

    public void cancelTask(String lobby, String task){
        activeTasks.get(lobby).remove(task);
        availableTasks.get(lobby).add(task);
    }

    public List<String> getAvailableTasks(String lobby){
        return availableTasks.get(lobby);
    }

}
