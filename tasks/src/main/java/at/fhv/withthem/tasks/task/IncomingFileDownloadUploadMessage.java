package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class IncomingFileDownloadUploadMessage extends TaskMessage {
    private String _make;

    @JsonCreator
    public IncomingFileDownloadUploadMessage(String lobby, String task, String player, String make) {
        super(lobby, task, player);
        _make = make;
    }

    public String getMake() {
        return _make;
    }
}
