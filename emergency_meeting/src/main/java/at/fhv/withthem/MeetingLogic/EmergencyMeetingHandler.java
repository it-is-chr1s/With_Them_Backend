package at.fhv.withthem.MeetingLogic;

import at.fhv.withthem.MeetingLogic.task.*;
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
    public void removeMeeting(String gameId){
        _emergencyMeetings.remove(gameId);
    }
}
