package at.fhv.withthem.ChatLogic;

import java.security.Timestamp;

public class Message {
    private String _gameId;
    private String _sander;
    private String _content;
    public Message(String gameId,String sander, String content){
        _gameId=gameId;
        _sander=sander;
        _content=content;
    }

    public void setGame(String gameId) {_gameId=gameId;
    }
}
