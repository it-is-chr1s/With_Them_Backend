package at.fhv.withthem.ChatLogic;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String _gameId;
    private List<ChatMessage> _chatMessages;

    public Chat(String gameId) {
        _gameId = gameId;
        _chatMessages = new ArrayList<>();
    }

    public void addMessage(ChatMessage message) {
        _chatMessages.add(message);
    }

    public List<ChatMessage> getMessages() {
        return _chatMessages;
    }
}
