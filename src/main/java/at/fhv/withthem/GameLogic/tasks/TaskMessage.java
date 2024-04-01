package at.fhv.withthem.GameLogic.tasks;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TaskMessage {
    private String _lobby;
    private String _task;
    private String _player;

    @JsonCreator
    public TaskMessage(String lobby, String task, String player) {
        _lobby = lobby;
        _task = task;
        _player = player;
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
}
