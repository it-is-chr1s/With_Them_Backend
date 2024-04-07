package at.fhv.withthem.GameLogic;

public class InitTaskMessage {
    private String _lobby;
    private String _type;
    private int _id;

    public InitTaskMessage(String lobby, String type, int id) {
        _lobby = lobby;
        _type = type;
        _id = id;
    }

    public String getLobby() {
        return _lobby;
    }

    public String getType() {
        return _type;
    }

    public int getId() {
        return _id;
    }
}
