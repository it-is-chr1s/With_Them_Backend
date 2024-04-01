package at.fhv.withthem.GameLogic.tasks.task;

import at.fhv.withthem.GameLogic.tasks.TaskMessage;

public class OutgoingFileDownloadUploadMessage extends TaskMessage {
    private String _status;

    public OutgoingFileDownloadUploadMessage(String status){
        super("TaskFileDownloadUpload");
        _status = status;
    }
}
