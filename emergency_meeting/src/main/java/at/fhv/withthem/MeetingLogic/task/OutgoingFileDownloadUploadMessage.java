package at.fhv.withthem.MeetingLogic.task;

import at.fhv.withthem.MeetingLogic.EmergencyMeetingMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OutgoingFileDownloadUploadMessage extends EmergencyMeetingMessage {
    private String _status;
    private float _progress;

    @JsonCreator
    public OutgoingFileDownloadUploadMessage(String status, float progress) {
        super("FileDownloadUpload");
        _status = status;
        _progress = progress;
    }

    public String getStatus() {
        return _status;
    }

    public float getProgress() {
        return _progress;
    }
}
