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
    private final Set<String> _initializedLobbies = new HashSet<>();

    final private int SABOTAGE_DURATION_SEC = 60;
    final private int SABOTAGE_COOLDOWN_SEC = 90;

    private HashMap<String, Integer> timerDuration_sec = new HashMap<>();
    private HashMap<String, Integer> timerCooldown_sec = new HashMap<>();

    public SabotageHandler(){
        if(SABOTAGE_DURATION_SEC > SABOTAGE_COOLDOWN_SEC){
            throw new IllegalArgumentException("Sabotage duration must be less than sabotage cooldown.");
        }
    }

    public void addTaskToLobby(String lobby, String taskType, int taskID){
        if(_availableSabotages.get(lobby) == null) {
            _availableSabotages.put(lobby, new ArrayList<>());

            timerDuration_sec.put(lobby, SABOTAGE_DURATION_SEC);
            timerCooldown_sec.put(lobby, SABOTAGE_COOLDOWN_SEC);

            _initializedLobbies.add(lobby);
        }

        if(_availableSabotages.get(lobby).stream().noneMatch(task -> task.getId() == taskID)){
            if(taskType.equals("Connecting Wires")) {
                _availableSabotages.get(lobby).add(new TaskConnectingWires(taskID));
                System.out.println("Added Task Connecting Wires: " + taskID);
            }
        }
    }

    public void startSabotage(String lobbyID, int sabotageId, Reaction reaction) {
        if(timerDuration_sec.get(lobbyID) == SABOTAGE_DURATION_SEC && timerCooldown_sec.get(lobbyID) == SABOTAGE_COOLDOWN_SEC) {
            for(Task task : _availableSabotages.get(lobbyID)){
                if(task.getId() == sabotageId){
                    _currentSabotage.put(lobbyID, task);
                    break;
                }
            }

            Thread timerThread = new Thread(() -> {
                while (timerCooldown_sec.get(lobbyID) > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;  // Exit the loop if the thread is interrupted
                    }

                    timerCooldown_sec.replace(lobbyID, timerCooldown_sec.get(lobbyID) - 1);
                    if (timerDuration_sec.get(lobbyID) > 0) {
                        timerDuration_sec.replace(lobbyID, timerDuration_sec.get(lobbyID) - 1);
                    }

                    reaction.react();

                    System.out.println("Sabotage timer: " + timerDuration_sec.get(lobbyID) + "s");
                    System.out.println("Sabotage cooldown: " + timerCooldown_sec.get(lobbyID) + "s");
                }
                System.out.println("Sabotage completed.");

                timerDuration_sec.replace(lobbyID, SABOTAGE_DURATION_SEC);
                timerCooldown_sec.replace(lobbyID, SABOTAGE_COOLDOWN_SEC);
            });
            timerThread.start();
        }
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
        return timerDuration_sec.get(lobbyID);
    }

    public int getTimerCooldown_sec(String lobbyID){
        return timerCooldown_sec.get(lobbyID);
    }
}
