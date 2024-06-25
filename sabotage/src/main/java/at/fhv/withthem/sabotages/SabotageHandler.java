package at.fhv.withthem.sabotages;

import at.fhv.withthem.sabotages.sabotage.Reaction;
import at.fhv.withthem.sabotages.sabotage.Task;
import at.fhv.withthem.sabotages.sabotage.connecting_wires.TaskConnectingWires;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SabotageHandler {
    private final HashMap<String, List<Task>> _availableSabotages = new HashMap<>();
    private HashMap<String, Task> _currentSabotage = new HashMap<>();
    private String _status = "unset";
    private final Set<String> _initializedLobbies = new HashSet<>();

    final private int SABOTAGE_DURATION_SEC = 60;
    final private int SABOTAGE_COOLDOWN_SEC = 90;

    private HashMap<String, Integer> _timerDuration_sec = new HashMap<>();
    private HashMap<String, Integer> _timerCooldown_sec = new HashMap<>();

    public SabotageHandler(){
        if(SABOTAGE_DURATION_SEC > SABOTAGE_COOLDOWN_SEC){
            throw new IllegalArgumentException("Sabotage duration must be less than sabotage cooldown.");
        }
    }

    public void addTaskToLobby(String lobby, String taskType, int taskID){
        if(_availableSabotages.get(lobby) == null) {
            _availableSabotages.put(lobby, new ArrayList<>());

            _timerDuration_sec.put(lobby, SABOTAGE_DURATION_SEC);
            _timerCooldown_sec.put(lobby, SABOTAGE_COOLDOWN_SEC);

            _initializedLobbies.add(lobby);
        }

        if(_availableSabotages.get(lobby).stream().noneMatch(task -> task.getId() == taskID)){
            if(taskType.equals("Connecting Wires")) {
                _availableSabotages.get(lobby).add(new TaskConnectingWires(taskID));
                System.out.println("Added Task Connecting Wires: " + taskID);
            }
        }
    }

    public void startSabotage(String lobbyID, int sabotageId, Reaction updateInformation, Reaction reachedTimeout) {
        if(_timerDuration_sec.get(lobbyID) == SABOTAGE_DURATION_SEC && _timerCooldown_sec.get(lobbyID) == SABOTAGE_COOLDOWN_SEC) {
            for(Task task : _availableSabotages.get(lobbyID)){
                if(task.getId() == sabotageId){
                    _currentSabotage.put(lobbyID, task);
                    break;
                }
            }
            _status = "available";
            Thread timerThread = new Thread(() -> {
                while (_timerCooldown_sec.get(lobbyID) > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;  // Exit the loop if the thread is interrupted
                    }

                    _timerCooldown_sec.replace(lobbyID, _timerCooldown_sec.get(lobbyID) - 1);
                    if (_timerDuration_sec.get(lobbyID) > 0) {
                        _timerDuration_sec.replace(lobbyID, _timerDuration_sec.get(lobbyID) - 1);
                    }

                    updateInformation.react();

                    System.out.println("Sabotage timer: " + _timerDuration_sec.get(lobbyID) + "s");
                    System.out.println("Sabotage cooldown: " + _timerCooldown_sec.get(lobbyID) + "s");

                    if(_timerDuration_sec.get(lobbyID) == 0 && _currentSabotage.get(lobbyID) != null){
                        cleanLobby(lobbyID);
                        reachedTimeout.react();
                    }
                }
                System.out.println("Sabotage cooldown reached.");
            });
            timerThread.start();
        }
    }

    private void cleanLobby(String lobbyID){
        _availableSabotages.remove(lobbyID);
        _currentSabotage.remove(lobbyID);
        _timerDuration_sec.remove(lobbyID);
        _timerCooldown_sec.remove(lobbyID);
        _initializedLobbies.remove(lobbyID);
    }

    public List<TaskMessage> getAvailableSabotages(String lobby){
        List<TaskMessage> availableSabotages = new ArrayList<>();

        for(Task task : _availableSabotages.get(lobby)){
            availableSabotages.add(new TaskMessage(task.getType(), task.getId()));
        }

        return availableSabotages;
    }

    public boolean lobbyExists(String lobbyId){
        return _initializedLobbies.contains(lobbyId);
    }

    public Task getCurrentSabotage(String lobby){
        return _currentSabotage.get(lobby);
    }

    public int getTimerDuration_sec(String lobbyID){
        return _timerDuration_sec.get(lobbyID);
    }

    public int getTimerCooldown_sec(String lobbyID){
        return _timerCooldown_sec.get(lobbyID);
    }

    public String getStatus(String lobbyID){
        return _status;
    }
}
