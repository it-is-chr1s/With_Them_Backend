package at.fhv.withthem.ChatLogic;

import java.util.List;

public class Chat {
    private String _gameId;
    private List<Message>_messages;
    public Chat(String gameId){
        _gameId=gameId;
    }
    public void addMessage(Message m){
        _messages.add(m);
    }
}
