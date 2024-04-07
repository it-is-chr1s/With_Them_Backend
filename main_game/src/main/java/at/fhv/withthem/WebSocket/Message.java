package at.fhv.withthem.WebSocket;

public class Message {

    private String _content;

    public Message(String content) {
        _content = content;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        _content = content;
    }
}
