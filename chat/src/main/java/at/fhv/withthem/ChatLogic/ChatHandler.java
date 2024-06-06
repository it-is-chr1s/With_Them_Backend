package at.fhv.withthem.ChatLogic;

import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
public class ChatHandler {
    private final HashMap<String, Chat> _chats= new HashMap<>();
    private boolean chatExists(String gameId){
        return _chats.containsKey(gameId);
    }
    public void createChat(String gameId){
        if(chatExists(gameId))
            return;
        System.out.println("Chat created"+gameId);
        _chats.put(gameId,new Chat(gameId));
    }
    public void addMessage(String gameId, ChatMessage m){
        if(chatExists(gameId)){
            System.out.println("Message Added");
            _chats.get(gameId).addMessage(m);
        }
        else
            System.out.println("Chat does not exist");
    }
}
