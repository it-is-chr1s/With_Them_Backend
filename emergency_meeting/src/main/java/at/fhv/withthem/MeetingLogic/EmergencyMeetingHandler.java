package at.fhv.withthem.MeetingLogic;

import at.fhv.withthem.MeetingLogic.task.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EmergencyMeetingHandler {
    private final HashMap<String, EmergencyMeeting> _emergencyMeetings= new HashMap<>();

    public boolean meetingExists(String gameId) {
        return _emergencyMeetings.containsKey(gameId);
    }
    public void createMeeting(String gameId, List<String>names){
        _emergencyMeetings.put(gameId, new EmergencyMeeting(gameId,names));

        System.out.println("NEW EMERGENCY MEETING CREATED: "+_emergencyMeetings.get(gameId));
    }
    public void updateMeeting(String gameId, List<String>names){
        _emergencyMeetings.get(gameId).setAlivePlayers(names);
    }
    public void startMeeting(String gameId) {
        if(meetingExists(gameId)){
            if(_emergencyMeetings.get(gameId).isStartable()){
                //TODO: logic for starting a meeting
                _emergencyMeetings.get(gameId).setIsRunning(true);
            }
        }
    }
    public void endMeeting(String gameId) {
        _emergencyMeetings.get(gameId).setIsRunning(false);
        _emergencyMeetings.get(gameId).setStartable(false);
        //TODO: count down timer
    }
    public void removeMeeting(String gameId){
        _emergencyMeetings.remove(gameId);
    }
}
