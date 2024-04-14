package at.fhv.withthem.tasks;

import at.fhv.withthem.tasks.task.IncomingConnectingWiresMessage;
import at.fhv.withthem.tasks.task.IncomingFileDownloadUploadMessage;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = TaskMessage.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IncomingConnectingWiresMessage.class, name = "incomingConnectingWires"),
        @JsonSubTypes.Type(value = IncomingFileDownloadUploadMessage.class, name = "incomingFileDownloadUpload")
})
public class TaskMessage {
    private String _lobby;
    private String _task;
    private String _player;
    private int _id;

    @JsonCreator
    public TaskMessage(String lobby, String task, String player) {
        _lobby = lobby;
        _task = task;
        _player = player;
    }

    public TaskMessage(String task, int id){
        _task = task;
        _id = id;
    }

    public TaskMessage(String task) {
        _task = task;
    }

    public String getLobby() {
        return _lobby;
    }

    public void setLobby(String _lobby) {
        this._lobby = _lobby;
    }

    public String getTask() {
        return _task;
    }

    public void setTask(String _task) {
        this._task = _task;
    }

    public String getPlayer() {
        return _player;
    }

    public void setPlayer(String _player) {
        this._player = _player;
    }

    public int getId(){
        return _id;
    }

    public void setId(int id){
        _id = id;
    }
}
