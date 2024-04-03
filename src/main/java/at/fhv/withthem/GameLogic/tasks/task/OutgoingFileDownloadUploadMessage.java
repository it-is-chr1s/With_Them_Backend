package at.fhv.withthem.GameLogic.tasks.task;

import at.fhv.withthem.GameLogic.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OutgoingFileDownloadUploadMessage extends TaskMessage {
    private String _status;

    @JsonCreator
    public OutgoingFileDownloadUploadMessage(String status){
        super("TaskFileDownloadUpload");
        _status = status;
    }

    public String getStatus() {
        return _status;
    }
}
