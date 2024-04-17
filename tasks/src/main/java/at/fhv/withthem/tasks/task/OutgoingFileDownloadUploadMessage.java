package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OutgoingFileDownloadUploadMessage extends TaskMessage {
    private String _status;
    private float _progress;

    @JsonCreator
    public OutgoingFileDownloadUploadMessage(String status, float progress){
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
