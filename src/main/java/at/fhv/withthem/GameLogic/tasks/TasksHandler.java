package at.fhv.withthem.GameLogic.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TasksHandler {
    private List<String> _possibleTasks = new ArrayList<>();
    private HashMap<String, List<String>> _availableTasks = new HashMap<>();
    private HashMap<String, List<String>> _activeTasks = new HashMap<>();
    private HashMap<String, Integer> _finishedTasks = new HashMap<>();

    public void registerTask(String task){
        _possibleTasks.add(task);
    }

    public void loadLobby(String lobby){
        _availableTasks.put(lobby, new ArrayList<>(_possibleTasks));
        _activeTasks.put(lobby, new ArrayList<>());
        _finishedTasks.put(lobby, 0);
    }

    public void startTask(String lobby, String task){
        _availableTasks.get(lobby).remove(task);
        _activeTasks.get(lobby).add(task);
    }

    public void finishTask(String lobby, String task){
        _activeTasks.get(lobby).remove(task);
        _finishedTasks.put(lobby, _finishedTasks.get(lobby) + 1);
    }

    public void cancelTask(String lobby, String task){
        _activeTasks.get(lobby).remove(task);
        _availableTasks.get(lobby).add(task);
    }

    public List<String> getAvailableTasks(String lobby){
        return _availableTasks.get(lobby);
    }

}
