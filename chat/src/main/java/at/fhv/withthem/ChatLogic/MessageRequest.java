package at.fhv.withthem.ChatLogic;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MessageRequest {
    private boolean _alive;
    private String _gameId;
    private String _sander;
    private String _content;
    @JsonCreator
    public MessageRequest(boolean alive, String gameId, String sander, String content){
        _alive=alive;
        _gameId=gameId;
        _sander=sander;
        _content=content;
    }

    public boolean getAlive() {
        return _alive;
    }

    public void setAlive(boolean _alive) {
        this._alive = _alive;
    }

    public String getGameId() {
        return _gameId;
    }

    public void setGameId(String _gameId) {
        this._gameId = _gameId;
    }

    public String getSander() {
        return _sander;
    }

    public void setSander(String _sander) {
        this._sander = _sander;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String _content) {
        this._content = _content;
    }
}
