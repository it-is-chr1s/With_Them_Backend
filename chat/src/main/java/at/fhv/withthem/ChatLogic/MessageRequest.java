package at.fhv.withthem.ChatLogic;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MessageRequest {
    private String _gameId;
    private String _sander;
    private String _content;
    @JsonCreator
    public MessageRequest(String gameId, String sander, String content){
        _gameId=gameId;
        _sander=sander;
        _content=content;
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
