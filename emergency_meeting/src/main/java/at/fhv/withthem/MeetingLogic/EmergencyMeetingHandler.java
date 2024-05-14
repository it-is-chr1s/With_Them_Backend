package at.fhv.withthem.MeetingLogic;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class EmergencyMeetingHandler {
    private final HashMap<String, EmergencyMeeting> _emergencyMeetings= new HashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public boolean meetingExists(String gameId) {
        return _emergencyMeetings.containsKey(gameId);
    }
    public void createMeeting(String gameId, List<String>names){
        _emergencyMeetings.put(gameId, new EmergencyMeeting(gameId,names));
    }
    public void updateLivePlayers(String gameId, List<String>names){
        _emergencyMeetings.get(gameId).setAlivePlayers(names);
    }
    public boolean startMeeting(String gameId) {
        System.out.println("exist:"+meetingExists(gameId));
        if(!meetingExists(gameId))
            return false;
        System.out.println("startsable:"+_emergencyMeetings.get(gameId).isStartable());
        if(_emergencyMeetings.get(gameId).isStartable()){
            _emergencyMeetings.get(gameId).setIsRunning(true);
            System.out.println("is runnin:"+ _emergencyMeetings.get(gameId).getIsRunning());
            _emergencyMeetings.get(gameId).setStartable(false);
            return true;
        }
        return false;
    }
    public boolean endMeeting(String gameId) {
        System.out.println("exist:"+meetingExists(gameId));
        if(meetingExists(gameId) && _emergencyMeetings.get(gameId).getIsRunning()) {
            _emergencyMeetings.get(gameId).setIsRunning(false);
            scheduler.schedule(() -> {
                _emergencyMeetings.get(gameId).setStartable(true);
                System.out.println("startable set to true");
            }, 10, TimeUnit.SECONDS);
            return false;
        }
        return _emergencyMeetings.get(gameId).getIsRunning();
    }
    public void startVoting(String gameId){
        if (meetingExists(gameId) && _emergencyMeetings.get(gameId).getIsRunning() && !_emergencyMeetings.get(gameId).hasStarted()){
            _emergencyMeetings.get(gameId).startVoting();
            scheduler.schedule(() -> {
                if(_emergencyMeetings.get(gameId).getVotedStarted()){
                    System.out.println("Voting ended");
                    _emergencyMeetings.get(gameId).endVoting();
                    //TODO:Kill suspect
                }
            }, 60, TimeUnit.SECONDS);
        }
    }
    public String vote(String gameId, String voter, String nominated){
        _emergencyMeetings.get(gameId).addVote(voter,nominated);
        if(_emergencyMeetings.get(gameId).everyoneVoted()){
            System.out.println("Voting ended");
            _emergencyMeetings.get(gameId).endVoting();
            //TODO:Kill suspect
            return getSuspect(gameId);
        }
        return null;
    }
    public String getSuspect(String gameId){
        List<String> nominatedPlayers =_emergencyMeetings.get(gameId).getVotes().values().stream().toList();
        if (nominatedPlayers == null || nominatedPlayers.isEmpty()) {
            return null; // No suspect if the list is empty
        }

        Map<String, Integer> playerCounts = new HashMap<>();

        // Count the occurrences of each nominated player
        for (String player : nominatedPlayers) {
            playerCounts.put(player, playerCounts.getOrDefault(player, 0) + 1);
        }

        // Find the player(s) with the highest count
        int maxCount = 0;
        Set<String> suspects = new HashSet<>();
        for (Map.Entry<String, Integer> entry : playerCounts.entrySet()) {
            int count = entry.getValue();
            if (count > maxCount) {
                maxCount = count;
                suspects.clear(); // Clear previous suspects
                suspects.add(entry.getKey()); // Add new suspect
            } else if (count == maxCount) {
                // If another player has the same count, they're also a suspect
                suspects.add(entry.getKey());
            }
        }
        // If there's only one suspect, return their name
        if (suspects.size() == 1) {
            return suspects.iterator().next();
        } else {
            return "NO ONE WAS KICKED"; // No suspect if multiple players have the same count
        }
    }
    public boolean getStartable(String gameId){
        return _emergencyMeetings.get(gameId).isStartable();
    }
    public void removeMeeting(String gameId){
        _emergencyMeetings.remove(gameId);
    }
}
