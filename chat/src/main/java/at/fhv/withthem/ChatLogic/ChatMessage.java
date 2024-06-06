package at.fhv.withthem.ChatLogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage {
    private String _gameId;
    private String _sender;
    private String _content;

    @JsonCreator
    public ChatMessage(
            @JsonProperty("gameId") String gameId,
            @JsonProperty("sender") String sender,
            @JsonProperty("content") String content) {
        _gameId = gameId;
        _sender = sender;
        _content = content;
    }

    public String get_gameId() {
        return _gameId;
    }

    public void set_gameId(String _gameId) {
        this._gameId = _gameId;
    }

    public String get_sender() {
        return _sender;
    }

    public void set_sender(String _sender) {
        this._sender = _sender;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }
}
