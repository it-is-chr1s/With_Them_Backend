package at.fhv.withthem.MeetingLogic.task;

import at.fhv.withthem.MeetingLogic.EmergencyMeetingMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class IncomingFileDownloadUploadMessage extends EmergencyMeetingMessage {
    private String _make;

    @JsonCreator
    public IncomingFileDownloadUploadMessage(String lobby, String task, String player, String make) {
        super(lobby, task, player);
        _make = make;
    }

    public void setMake(String make) {
        _make = make;
    }

    public String getMake() {
        return _make;
    }
}
